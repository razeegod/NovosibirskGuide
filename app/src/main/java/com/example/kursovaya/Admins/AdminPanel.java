package com.example.kursovaya.Admins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kursovaya.MainActivity;
import com.example.kursovaya.R;
import com.example.kursovaya.Users.Profile;
import com.example.kursovaya.Users.Settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class AdminPanel extends AppCompatActivity {

    private Button gotoAddPlace_btn, gotoCategoryAdm_btn;
    private ImageView logoutBtn;
    private ImageView settingsBtn;
    private CircleImageView ProfileImage;
    private String currentUserId;
    private TextView HelloUser, countUsers, countPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        init();
        getCountUsers();
        getCountPlaces();

        gotoCategoryAdm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goCategoryAdmIntent = new Intent(AdminPanel.this, AdminChangePlacesChooseCategory.class);
                startActivity(goCategoryAdmIntent);
            }
        });
        gotoAddPlace_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPlaceIntent = new Intent(AdminPanel.this, AddNewPlace.class);
                startActivity(addPlaceIntent);
            }
        });

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue() != null ? snapshot.child("username").getValue().toString() : "";
                HelloUser.setText("Администратор " + username);
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

                Intent logoutIntent = new Intent(AdminPanel.this, MainActivity.class);
                startActivity(logoutIntent);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(AdminPanel.this, AdminSettings.class);
                startActivity(settingsIntent);
            }
        });
    }

    private void getCountUsers()
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("User");
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long totalUsers = snapshot.getChildrenCount();
                countUsers.setText("" + totalUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCountPlaces()
    {
        DatabaseReference PlacesRef = FirebaseDatabase.getInstance().getReference().child("Places");

        PlacesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long totalPlaces = 0;
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    totalPlaces += categorySnapshot.getChildrenCount();
                }
                countPlaces.setText("" + totalPlaces);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void init()
    {
        gotoCategoryAdm_btn = findViewById(R.id.gotochangeordelete_btn);
        gotoAddPlace_btn = (Button) findViewById(R.id.gotoadd_btn);
        logoutBtn = (ImageView) findViewById(R.id.logout_btn);
        settingsBtn = (ImageView) findViewById(R.id.settings_btn);
        ProfileImage = (CircleImageView) findViewById(R.id.user_profile_image);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        HelloUser = (TextView) findViewById(R.id.username);
        countPlaces = findViewById(R.id.countplaces_txt);
        countUsers = findViewById(R.id.count_users);
    }
}