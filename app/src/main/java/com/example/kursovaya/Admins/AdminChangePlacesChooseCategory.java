package com.example.kursovaya.Admins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kursovaya.R;

public class AdminChangePlacesChooseCategory extends AppCompatActivity {

    private ImageView MuseumTheatreCategory, EventCategory, ParkCategory, RestaurantCategory, CinemaCategory, HistoryCategory, NatureCategory, ShoppingCategory, MasterClassesCategory;
    private AppCompatButton BackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_change_places_choose_category);

        init();

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BackIntent = new Intent(AdminChangePlacesChooseCategory.this, AdminPanel.class);
                startActivity(BackIntent);
            }
        });
        MuseumTheatreCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent museumtheatreIntent = new Intent(AdminChangePlacesChooseCategory.this, AdminPlaceActivity.class);
                museumtheatreIntent.putExtra("category", "MuseumTheatre");
                startActivity(museumtheatreIntent);
            }
        });

        CinemaCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cinemaIntent = new Intent(AdminChangePlacesChooseCategory.this, AdminPlaceActivity.class);
                cinemaIntent.putExtra("category", "Cinema");
                startActivity(cinemaIntent);
            }
        });

        RestaurantCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restarauntIntent = new Intent(AdminChangePlacesChooseCategory.this, AdminPlaceActivity.class);
                restarauntIntent.putExtra("category", "Restaraunt");
                startActivity(restarauntIntent);
            }
        });

        ParkCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent parkIntent = new Intent(AdminChangePlacesChooseCategory.this, AdminPlaceActivity.class);
                parkIntent.putExtra("category", "Park");
                startActivity(parkIntent);
            }
        });

        EventCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventIntent = new Intent(AdminChangePlacesChooseCategory.this, AdminPlaceActivity.class);
                eventIntent.putExtra("category", "Event");
                startActivity(eventIntent);
            }
        });

        MasterClassesCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mcIntent = new Intent(AdminChangePlacesChooseCategory.this, AdminPlaceActivity.class);
                mcIntent.putExtra("category", "Master-Class");
                startActivity(mcIntent);
            }
        });

        ShoppingCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shoppingIntent = new Intent(AdminChangePlacesChooseCategory.this, AdminPlaceActivity.class);
                shoppingIntent.putExtra("category", "Shopping");
                startActivity(shoppingIntent);
            }
        });

        NatureCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent natureIntent = new Intent(AdminChangePlacesChooseCategory.this, AdminPlaceActivity.class);
                natureIntent.putExtra("category", "Nature");
                startActivity(natureIntent);
            }
        });

        HistoryCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(AdminChangePlacesChooseCategory.this, AdminPlaceActivity.class);
                historyIntent.putExtra("category", "History");
                startActivity(historyIntent);
            }
        });
    }

    private void init() {
        MuseumTheatreCategory = findViewById(R.id.museum_theatre_category);
        CinemaCategory = findViewById(R.id.cinema_category);
        RestaurantCategory = findViewById(R.id.restaurant_category);

        ParkCategory = findViewById(R.id.park_category);
        EventCategory = findViewById(R.id.event_category);
        MasterClassesCategory = findViewById(R.id.masterclasses_category);

        ShoppingCategory = findViewById(R.id.shopping_category);
        NatureCategory = findViewById(R.id.nature_category);
        HistoryCategory = findViewById(R.id.history_category);

        BackBtn = findViewById(R.id.back_btn);
    }
}