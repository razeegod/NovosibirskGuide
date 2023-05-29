package com.example.kursovaya.Places;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursovaya.Map.PlacesMap;
import com.example.kursovaya.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;
import com.yandex.runtime.view.internal.VulkanSurfaceView;

public class PlaceDetailsActivity extends AppCompatActivity {

    TextView placeName, placeDescription, placeSchedule, placeNumber;
    ImageView placeImage, BackBtn;
    Button placeUrl, placeMap;
    String placeNameTxt, placeDescriptionTxt, placeScheduleTxt, placeNumberTxt, placeImageTxt, placeCoordinatesTxt, categoryName, placeID;
    Uri placeURLTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        init();

        placeID = getIntent().getStringExtra("PlaceID");

        DatabaseReference placeRef = FirebaseDatabase.getInstance().getReference().child("Places").child(categoryName).child(placeID);

        placeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String phone = snapshot.child("Number").getValue() != null ? snapshot.child("Number").getValue().toString() : "";
                    String image = snapshot.child("Image").getValue().toString();
                    String description = snapshot.child("Description").getValue().toString();
                    String name = snapshot.child("Name").getValue().toString();
                    String schedule = snapshot.child("Schedule").getValue().toString();
                    String coordinates = snapshot.child("Coordinates").getValue().toString();
                    String url = snapshot.child("URL").getValue().toString();

                    Picasso.get().load(image).into(placeImage);
                    placeName.setText(name);
                    placeDescription.setText(description);
                    placeNumber.setText(phone);
                    placeSchedule.setText(schedule);
                    placeURLTxt = Uri.parse(url);
                    placeCoordinatesTxt = coordinates;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        placeUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (placeURLTxt != null && !placeURLTxt.toString().isEmpty()) {
                    Intent browserIntent = new Intent();
                    browserIntent.setAction(Intent.ACTION_VIEW);
                    browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                    browserIntent.setData(placeURLTxt);
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(PlaceDetailsActivity.this, "URL отсутствует или недействителен", Toast.LENGTH_SHORT).show();
                }
            }
        });

        placeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] coordinates = placeCoordinatesTxt.split(", ");
                double latitude = Double.parseDouble(coordinates[0]);
                double longitude = Double.parseDouble(coordinates[1]);

                Intent mapIntent = new Intent(PlaceDetailsActivity.this, PlacesMap.class);
                mapIntent.putExtra("latitude", latitude);
                mapIntent.putExtra("longitude", longitude);
                startActivity(mapIntent);
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(PlaceDetailsActivity.this, PlaceActivity.class);
                backIntent.putExtra("category", categoryName);
                startActivity(backIntent);
            }
        });

        placeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = placeNumber.getText().toString();
                if (!phoneNumber.isEmpty())
                {
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse("tel:"+phoneNumber));
                    startActivity(dialIntent);
                }
                else
                {
                    Toast.makeText(PlaceDetailsActivity.this, "Номер телефона отсутствует", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init()
    {
        placeName = findViewById(R.id.place_name);
        placeDescription = findViewById(R.id.place_description);
        placeSchedule = findViewById(R.id.place_schedule);
        placeNumber = findViewById(R.id.place_number);
        placeImage = findViewById(R.id.place_image);
        placeUrl = findViewById(R.id.place_url);
        placeMap = findViewById(R.id.place_map);
        BackBtn = findViewById(R.id.back_btn);
        categoryName = getIntent().getStringExtra("category");
    }
}