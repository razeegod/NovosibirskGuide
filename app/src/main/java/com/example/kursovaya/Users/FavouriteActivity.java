package com.example.kursovaya.Users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursovaya.Adapter.FavouritePlacesAdapter;
import com.example.kursovaya.Places.Interface.OnFavouriteClickListener;
import com.example.kursovaya.R;
import com.example.kursovaya.model.Favourite;
import com.example.kursovaya.model.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private RecyclerView FavouritesPlaces;
    private AppCompatButton BackBtnFav;
    private List<Places> favouritePlacesList = new ArrayList<>();
    private FavouritePlacesAdapter favouritePlacesAdapter;

    private String currentUserID, placeId, category;
    private FirebaseAuth mAuth;
    DatabaseReference FavouritesRef, PlacesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        init();



        FavouritesPlaces.setLayoutManager(new LinearLayoutManager(this));
        favouritePlacesAdapter = new FavouritePlacesAdapter(favouritePlacesList, place -> {
            FavouritesRef.child(currentUserID + "_" + placeId).removeValue();
            favouritePlacesList.remove(place);
            favouritePlacesAdapter.notifyDataSetChanged();
        }, currentUserID);
        FavouritesPlaces.setAdapter(favouritePlacesAdapter);

        BackBtnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtoProf = new Intent(FavouriteActivity.this, Profile.class);
                startActivity(backtoProf);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference favouritesRef = FirebaseDatabase.getInstance().getReference().child("Favourites");
        Query favouritePlacesQuery = favouritesRef.orderByKey().startAt(userId+"_").endAt(userId+ "\uf8ff");

        favouritePlacesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot favouriteSnapshot : dataSnapshot.getChildren()) {
                    Favourite favouritePlace = favouriteSnapshot.getValue(Favourite.class);
                    placeId = favouritePlace.getPlaceId();
                    category = favouritePlace.getCategory();
                    getPlaceDetails(placeId, category);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибки
            }
        });

    }

    private void getPlaceDetails(String placeId, String category) {
        DatabaseReference placesRef = FirebaseDatabase.getInstance().getReference().child("Places").child(category);

        placesRef.child(placeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Places place = dataSnapshot.getValue(Places.class);
                if (place != null) {
                    favouritePlacesList.add(place);
                    favouritePlacesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        FavouritesPlaces = findViewById(R.id.recycler_favourites);
        BackBtnFav = findViewById(R.id.back_btn_favourite);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        FavouritesRef = FirebaseDatabase.getInstance().getReference().child("Favourites");

        PlacesRef = FirebaseDatabase.getInstance().getReference().child("Places");
    }
}


