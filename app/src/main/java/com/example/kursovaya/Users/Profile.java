package com.example.kursovaya.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kursovaya.MainActivity;
import com.example.kursovaya.Map.PlacesMap;
import com.example.kursovaya.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Profile extends AppCompatActivity {

    private ImageView logoutBtn;
    private ImageView settingsBtn;
    private CircleImageView ProfileImage;
    private BottomNavigationView NavigationMenu;
    private String currentUserId;
    private TextView HelloUser, CityFact, Quote, AuthorQuote;
    private Button toFavouriteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

       init();

       NavigationMenu.setSelectedItemId(R.id.navigation_profile);

        if(getIntent().getExtras() != null) {
            int menuItemId = getIntent().getExtras().getInt("MenuItemId");
            NavigationMenu.setSelectedItemId(menuItemId);
        }


        getRandomCityFact();
       getRandomQuote();

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue() != null ? snapshot.child("username").getValue().toString() : "";
                HelloUser.setText("Здравствуйте, " + username);
                String imageUrl = snapshot.child("image").getValue() != null ? snapshot.child("image").getValue().toString() : "";
                if (!imageUrl.equals("")) {
                    Picasso.get().load(imageUrl).into(ProfileImage);
                } else {
                    Picasso.get().load(R.drawable.userprofile).into(ProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();

                FirebaseAuth.getInstance().signOut();

                Intent logoutIntent = new Intent(Profile.this, MainActivity.class);
                startActivity(logoutIntent);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(Profile.this, Settings.class);
                startActivity(settingsIntent);
            }
        });
        NavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int ItemId = item.getItemId();
                if (ItemId == R.id.navigation_profile)
                {

                }
                else if (ItemId == R.id.navigation_map)
                {
                    Intent intent = new Intent (Profile.this, PlacesMap.class);
                    intent.putExtra("MenuItemId", ItemId);
                    startActivity(intent);
                }
                else if (ItemId == R.id.navigation_category)
                {
                    Intent intent = new  Intent(Profile.this, CategoryPlace.class);
                    intent.putExtra("MenuItemId", ItemId);
                    startActivity(intent);
                }
                return true;
            }
        });

        toFavouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toFavouriteIntent = new Intent(Profile.this, FavouriteActivity.class);
                startActivity(toFavouriteIntent);
            }
        });


    }

    private void getRandomQuote() {
        DatabaseReference quotesRef = FirebaseDatabase.getInstance().getReference().child("Quotes");
        quotesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount();
                int rand = new Random().nextInt(count);
                int i = 0;
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (i == rand) {
                        String quote = childSnapshot.child("quote").getValue().toString();
                        String author = childSnapshot.child("author").getValue().toString();
                        Quote.setText(quote);
                        AuthorQuote.setText(author);
                        break;
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void getRandomCityFact() {
        DatabaseReference cityFactsRef = FirebaseDatabase.getInstance().getReference().child("CityFacts");
        cityFactsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount();
                int rand = new Random().nextInt(count);
                int i = 0;
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (i == rand) {
                        String cityFact = childSnapshot.child("fact").getValue().toString();
                        CityFact.setText(cityFact);
                        break;
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void init()
    {
        logoutBtn = (ImageView) findViewById(R.id.logout_btn);
        settingsBtn = (ImageView) findViewById(R.id.settings_btn);
        ProfileImage = (CircleImageView) findViewById(R.id.user_profile_image);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        HelloUser = (TextView) findViewById(R.id.username);
        CityFact = findViewById(R.id.fact_txt);
        Quote = findViewById(R.id.quote_txt);
        AuthorQuote = findViewById(R.id.author_quote);

        NavigationMenu = (BottomNavigationView) findViewById(R.id.navigation_panel);

        toFavouriteBtn = findViewById(R.id.to_favourite_button);
    }
}