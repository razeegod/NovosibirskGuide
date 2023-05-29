package com.example.kursovaya.Admins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kursovaya.R;

public class AddNewPlace extends AppCompatActivity {

    private ImageView MuseumTheatreCategory;
    private ImageView CinemaCategory;
    private ImageView RestaurantCategory;
    private ImageView ParkCategory;
    private ImageView EventCategory;
    private ImageView MasterClassesCategory;
    private ImageView ShoppingCategory;
    private ImageView NatureCategory;
    private ImageView HistoryCategory;
    private AppCompatButton BackBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_place);

        init();


        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BackIntent = new Intent(AddNewPlace.this, AdminPanel.class);
                startActivity(BackIntent);
            }
        });
        MuseumTheatreCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent museumtheatreIntent = new Intent(AddNewPlace.this, AddActivity.class);
                museumtheatreIntent.putExtra("category", "MuseumTheatre");
                museumtheatreIntent.putExtra("mode", "add");
                startActivity(museumtheatreIntent);
            }
        });

        CinemaCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cinemaIntent = new Intent(AddNewPlace.this, AddActivity.class);
                cinemaIntent.putExtra("category", "Cinema");
                cinemaIntent.putExtra("mode", "add");
                startActivity(cinemaIntent);
            }
        });

        RestaurantCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restarauntIntent = new Intent(AddNewPlace.this, AddActivity.class);
                restarauntIntent.putExtra("category", "Restaraunt");
                restarauntIntent.putExtra("mode", "add");
                startActivity(restarauntIntent);
            }
        });

        ParkCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent parkIntent = new Intent(AddNewPlace.this, AddActivity.class);
                parkIntent.putExtra("category", "Park");
                parkIntent.putExtra("mode", "add");
                startActivity(parkIntent);
            }
        });

        EventCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventIntent = new Intent(AddNewPlace.this, AddActivity.class);
                eventIntent.putExtra("category", "Event");
                eventIntent.putExtra("mode", "add");
                startActivity(eventIntent);
            }
        });

        MasterClassesCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mcIntent = new Intent(AddNewPlace.this, AddActivity.class);
                mcIntent.putExtra("category", "Master-Class");
                mcIntent.putExtra("mode", "add");
                startActivity(mcIntent);
            }
        });

        ShoppingCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shoppingIntent = new Intent(AddNewPlace.this, AddActivity.class);
                shoppingIntent.putExtra("category", "Shopping");
                shoppingIntent.putExtra("mode", "add");
                startActivity(shoppingIntent);
            }
        });

        NatureCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent natureIntent = new Intent(AddNewPlace.this, AddActivity.class);
                natureIntent.putExtra("category", "Nature");
                natureIntent.putExtra("mode", "add");
                startActivity(natureIntent);
            }
        });

        HistoryCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(AddNewPlace.this, AddActivity.class);
                historyIntent.putExtra("category", "History");
                historyIntent.putExtra("mode", "add");
                startActivity(historyIntent);
            }
        });
    }

    private void init() {
        MuseumTheatreCategory = findViewById(R.id.museum_theatre_category);
        CinemaCategory = findViewById(R.id.cinema_category);
        RestaurantCategory = findViewById(R.id.restaurant_category);

        ParkCategory =  findViewById(R.id.park_category);
        EventCategory =  findViewById(R.id.event_category);
        MasterClassesCategory =  findViewById(R.id.masterclasses_category);

        ShoppingCategory = findViewById(R.id.shopping_category);
        NatureCategory = findViewById(R.id.nature_category);
        HistoryCategory = findViewById(R.id.history_category);

        BackBtn = findViewById(R.id.back_btn);
    }
}