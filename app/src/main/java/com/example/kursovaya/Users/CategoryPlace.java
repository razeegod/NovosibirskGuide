package com.example.kursovaya.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.kursovaya.Places.PlaceActivity;
import com.example.kursovaya.Map.PlacesMap;
import com.example.kursovaya.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CategoryPlace extends AppCompatActivity {

    private ImageView MuseumTheatreCategory;
    private ImageView CinemaCategory;
    private ImageView RestaurantCategory;
    private ImageView ParkCategory;
    private ImageView EventCategory;
    private ImageView MasterClassesCategory;
    private ImageView ShoppingCategory;
    private ImageView NatureCategory;
    private ImageView HistoryCategory;
    private BottomNavigationView NavigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_place);

        init();

        if(getIntent().getExtras() != null) {
            int menuItemId = getIntent().getExtras().getInt("MenuItemId");
            NavigationMenu.setSelectedItemId(menuItemId);
        }


        MuseumTheatreCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent museumtheatreIntent = new Intent(CategoryPlace.this, PlaceActivity.class);
                museumtheatreIntent.putExtra("category", "MuseumTheatre");
                startActivity(museumtheatreIntent);
            }
        });

        CinemaCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cinemaIntent = new Intent(CategoryPlace.this, PlaceActivity.class);
                cinemaIntent.putExtra("category", "Cinema");
                startActivity(cinemaIntent);
            }
        });

        RestaurantCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restarauntIntent = new Intent(CategoryPlace.this, PlaceActivity.class);
                restarauntIntent.putExtra("category", "Restaraunt");
                startActivity(restarauntIntent);
            }
        });

        ParkCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent parkIntent = new Intent(CategoryPlace.this, PlaceActivity.class);
                parkIntent.putExtra("category", "Park");
                startActivity(parkIntent);
            }
        });

        EventCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventIntent = new Intent(CategoryPlace.this, PlaceActivity.class);
                eventIntent.putExtra("category", "Event");
                startActivity(eventIntent);
            }
        });

        MasterClassesCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mcIntent = new Intent(CategoryPlace.this, PlaceActivity.class);
                mcIntent.putExtra("category", "Master-Class");
                startActivity(mcIntent);
            }
        });

        ShoppingCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shoppingIntent = new Intent(CategoryPlace.this, PlaceActivity.class);
                shoppingIntent.putExtra("category", "Shopping");
                startActivity(shoppingIntent);
            }
        });

        NatureCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent natureIntent = new Intent(CategoryPlace.this, PlaceActivity.class);
                natureIntent.putExtra("category", "Nature");
                startActivity(natureIntent);
            }
        });

        HistoryCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(CategoryPlace.this, PlaceActivity.class);
                historyIntent.putExtra("category", "History");
                startActivity(historyIntent);
            }
        });

        NavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int ItemId = item.getItemId();
                if (ItemId == R.id.navigation_profile)
                {
                    Intent intent = new Intent(CategoryPlace.this, Profile.class);
                    intent.putExtra("MenuItemId", ItemId);
                    startActivity(intent);
                }
                else if (ItemId == R.id.navigation_map)
                {
                    Intent intent = new Intent(CategoryPlace.this, PlacesMap.class);
                    intent.putExtra("MenuItemId", ItemId);
                    startActivity(intent);
                }
                else if (ItemId == R.id.navigation_category)
                {

                }
                return true;
            }
        });
    }

    private void init()
    {
        MuseumTheatreCategory = (ImageView) findViewById(R.id.museum_theatre_category);
        CinemaCategory = (ImageView) findViewById(R.id.cinema_category);
        RestaurantCategory = (ImageView) findViewById(R.id.restaurant_category);

        ParkCategory = (ImageView) findViewById(R.id.park_category);
        EventCategory = (ImageView) findViewById(R.id.event_category);
        MasterClassesCategory = (ImageView) findViewById(R.id.masterclasses_category);

        ShoppingCategory = (ImageView) findViewById(R.id.shopping_category);
        NatureCategory = (ImageView) findViewById(R.id.nature_category);
        HistoryCategory = (ImageView) findViewById(R.id.history_category);

        NavigationMenu = (BottomNavigationView) findViewById(R.id.navigation_panel);
    }
}