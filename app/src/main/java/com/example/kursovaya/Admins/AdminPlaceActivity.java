package com.example.kursovaya.Admins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kursovaya.Places.Interface.ItemClickListner;
import com.example.kursovaya.Places.PlaceActivity;
import com.example.kursovaya.Places.PlaceDetailsActivity;
import com.example.kursovaya.R;
import com.example.kursovaya.Users.CategoryPlace;
import com.example.kursovaya.ViewHolder.PlaceViewHolder;
import com.example.kursovaya.model.Favourite;
import com.example.kursovaya.model.Places;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminPlaceActivity extends AppCompatActivity {

    private String categoryName;
    private RecyclerView PlacesList;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference PlacesRef;
    private TextView CategoryPlaceTxt;
    private ImageView BackBtn;
    private String CategoryPlaceTxtSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_place);

        categoryName = getIntent().getStringExtra("category");

        init();

        checkCategory();

        loadPlaces();

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(AdminPlaceActivity.this, AdminChangePlacesChooseCategory.class);
                backIntent.putExtra("category", categoryName);
                startActivity(backIntent);
            }
        });
    }

    protected void  loadPlaces() {
        super.onStart();

        FirebaseRecyclerOptions<Places> options = new FirebaseRecyclerOptions.Builder<Places>()
                .setQuery(PlacesRef, Places.class).build();

        FirebaseRecyclerAdapter<Places, PlaceViewHolder> adapter = new FirebaseRecyclerAdapter<Places, PlaceViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PlaceViewHolder holder, int position, @NonNull Places model) {
                holder.txtPlaceName_List.setText(model.getName());
                holder.txtPlaceSchedule_List.setText(model.getSchedule());
                Picasso.get().load(model.getImage()).into(holder.PlaceImage_List);
                Log.d("FirebaseData", "Name: " + model.getName() + ", Description: " + model.getDescription());
                holder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(AdminPlaceActivity.this, AddActivity.class);
                        intent.putExtra("PlaceID", model.getPlaceID());
                        intent.putExtra("category", categoryName);
                        intent.putExtra("mode", "change");
                        startActivity(intent);
                    }
                });
            }
            @NonNull
            @Override
            public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item_layout, parent, false);
                PlaceViewHolder holder = new PlaceViewHolder(view);
                return holder;
            }
        };
        PlacesList.setAdapter(adapter);
        adapter.startListening();
    }


    private void checkCategory() {
        if (categoryName.equals("MuseumTheatre"))
        {
            CategoryPlaceTxtSet = "Музеи и театры";
            CategoryPlaceTxt.setText(CategoryPlaceTxtSet);
        }
        else if(categoryName.equals("Cinema"))
        {
            CategoryPlaceTxtSet = "Кино";
            CategoryPlaceTxt.setText(CategoryPlaceTxtSet);
        }
        else if(categoryName.equals("Restaraunt"))
        {
            CategoryPlaceTxtSet = "Гастрономия";
            CategoryPlaceTxt.setText(CategoryPlaceTxtSet);
        }
        else if(categoryName.equals("Park"))
        {
            CategoryPlaceTxtSet = "Парки и сады";
            CategoryPlaceTxt.setText(CategoryPlaceTxtSet);
        }
        else if(categoryName.equals("Event"))
        {
            CategoryPlaceTxtSet = "Афиша";
            CategoryPlaceTxt.setText(CategoryPlaceTxtSet);
        }
        else if(categoryName.equals("Master-Class"))
        {
            CategoryPlaceTxtSet = "Мастер-классы";
            CategoryPlaceTxt.setText(CategoryPlaceTxtSet);
        }
        else if(categoryName.equals("Shopping"))
        {
            CategoryPlaceTxtSet = "Покупки";
            CategoryPlaceTxt.setText(CategoryPlaceTxtSet);
        }
        else if(categoryName.equals("Nature"))
        {
            CategoryPlaceTxtSet = "За пределами города N";
            CategoryPlaceTxt.setText(CategoryPlaceTxtSet);
        }
        else if(categoryName.equals("History"))
        {
            CategoryPlaceTxtSet = "История и архитектура";
            CategoryPlaceTxt.setText(CategoryPlaceTxtSet);
        }
    }

    private void init()
    {
        PlacesRef = FirebaseDatabase.getInstance().getReference().child("Places").child(categoryName);

        CategoryPlaceTxt = findViewById(R.id.category_name);
        BackBtn = findViewById(R.id.back_btn);
        PlacesList = findViewById(R.id.recycler_menu);
        layoutManager = new LinearLayoutManager(this);
        PlacesList.setHasFixedSize(true);
        PlacesList.setLayoutManager(layoutManager);
    }

}