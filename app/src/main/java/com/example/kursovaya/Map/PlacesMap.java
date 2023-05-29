package com.example.kursovaya.Map;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.kursovaya.Places.PlaceDetailsActivity;
import com.example.kursovaya.R;
import com.example.kursovaya.Users.CategoryPlace;
import com.example.kursovaya.Users.Profile;
import com.example.kursovaya.model.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.location.FilteringMode;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.runtime.image.ImageProvider;

public class PlacesMap extends AppCompatActivity {
    private MapView mapView;
    private BottomNavigationView navigationMenu;
    private static final int LOCATION_REQUEST_CODE = 1337;
    private boolean isInitialLocationSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.initialize(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_places_map);
        navigationMenu = findViewById(R.id.navigation_panel);
        mapView = findViewById(R.id.map_guide);

        if(getIntent().getExtras() != null) {
            int menuItemId = getIntent().getExtras().getInt("MenuItemId");
            navigationMenu.setSelectedItemId(menuItemId);
        }

        double latitude = getIntent().getDoubleExtra("latitude", 0.0);
        double longitude = getIntent().getDoubleExtra("longitude", 0.0);


        if (latitude != 0.0 && longitude != 0.0) {
            mapView.getMap().move(new CameraPosition(new Point(latitude, longitude), 16, 0, 0));
        }
        else
        {
            mapView.getMap().move(new CameraPosition(new Point(55.0084, 82.9357), 12, 0, 0));
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            } else {
                subscribeToUserLocationUpdates();
            }
        }



        navigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_profile) {
                    Intent intent = new Intent(PlacesMap.this, Profile.class);
                    intent.putExtra("MenuItemId", itemId);
                    startActivity(intent);
                } else if (itemId == R.id.navigation_map) {

                } else if (itemId == R.id.navigation_category) {
                    Intent intent = new Intent(PlacesMap.this, CategoryPlace.class);
                    intent.putExtra("MenuItemId", itemId);
                    startActivity(intent);
                }
                return true;
            }
        });



        // Здесь мы вызываем функцию добавления маркеров для каждой категории
        addMarkersFromFirebase("Places/MuseumTheatre");
        addMarkersFromFirebase("Places/Cinema");
        addMarkersFromFirebase("Places/Restaraunt");
        addMarkersFromFirebase("Places/Park");
        addMarkersFromFirebase("Places/Event");
        addMarkersFromFirebase("Places/Master-Class");
        addMarkersFromFirebase("Places/Shopping");
        addMarkersFromFirebase("Places/Nature");
        addMarkersFromFirebase("Places/History");

    }



    @Override
    protected void onStop(){

        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart(){

        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    private void subscribeToUserLocationUpdates() {
        LocationManager locationManager = MapKitFactory.getInstance().createLocationManager();
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {
            }

            @Override
            public void onLocationUpdated(@NonNull Location location) {
                if (!isInitialLocationSet) {
                    mapView.getMap().move(new CameraPosition(location.getPosition(), 14.0f, 0.0f, 0.0f));
                    isInitialLocationSet = true;
                }
            }
        };
        locationManager.subscribeForLocationUpdates(0, 0, 0, false, FilteringMode.OFF, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                subscribeToUserLocationUpdates();
            } else {
                Toast.makeText(this, "Для корректной работы приложения требуется разрешение на геолокацию", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void addMarkersFromFirebase(String categoryPath) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(categoryPath);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        String coordinatesString = postSnapshot.child("Coordinates").getValue(String.class);
                        String title = postSnapshot.child("Name").getValue(String.class);
                        String number = postSnapshot.child("Number").getValue(String.class);
                        String schedule = postSnapshot.child("Schedule").getValue(String.class);
                        String placeID = postSnapshot.child("PlaceID").getValue(String.class);

                        String[] coordinatesParts = coordinatesString.split(", ");

                        if (coordinatesParts.length == 2) {
                            try {
                                double lat = Double.parseDouble(coordinatesParts[0]);
                                double lon = Double.parseDouble(coordinatesParts[1]);

                                PlacemarkMapObject placemark = mapView.getMap().getMapObjects().addPlacemark(new Point(lat, lon));
                                placemark.setOpacity(1f);
                                placemark.setIcon(ImageProvider.fromResource(getApplicationContext(), R.drawable.location_marker));
                                placemark.setDraggable(true);

                                placemark.addTapListener(new MapObjectTapListener() {
                                    @Override
                                    public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                                        BottomSheetDialog dialog = new BottomSheetDialog(PlacesMap.this);
                                        View view = getLayoutInflater().inflate(R.layout.info_window_onmap, null);
                                        dialog.setContentView(view);

                                        TextView placeTitle = view.findViewById(R.id.placeNameTextView);
                                        placeTitle.setText(title);

                                        TextView placeSchedule = view.findViewById(R.id.placeScheduleTextView);
                                        placeSchedule.setText(schedule);

                                        TextView placeNumber = view.findViewById(R.id.placeNumberTextView);
                                        placeNumber.setText(number);

                                        Button moreInfoButton = view.findViewById(R.id.viewDetailsButton);
                                        moreInfoButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showPlaceDetails(placeID, categoryPath.split("/")[1]);
                                                dialog.dismiss();
                                            }
                                        });

                                        dialog.show();
                                        return true;
                                    }
                                });



                            } catch (NumberFormatException e) {
                                Log.e(TAG, "Invalid coordinate format: " + coordinatesString);
                            }
                        } else {
                            Log.e(TAG, "Invalid coordinate format: " + coordinatesString);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void showPlaceDetails(String placeID, String category) {
        Intent intent = new Intent(PlacesMap.this, PlaceDetailsActivity.class);
        intent.putExtra("PlaceID", placeID);
        intent.putExtra("category", category);
        startActivity(intent);
    }

}
