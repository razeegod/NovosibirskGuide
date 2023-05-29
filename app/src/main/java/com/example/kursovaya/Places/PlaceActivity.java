package com.example.kursovaya.Places;

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
import com.example.kursovaya.R;
import com.example.kursovaya.Users.CategoryPlace;
import com.example.kursovaya.ViewHolder.PlaceViewHolder;
import com.example.kursovaya.model.Favourite;
import com.example.kursovaya.model.Places;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class PlaceActivity extends AppCompatActivity {

    private Map<String, Boolean> favouritePlaces = new HashMap<>();

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
        setContentView(R.layout.activity_place);

        categoryName = getIntent().getStringExtra("category");

        init();

        checkCategoy();

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(PlaceActivity.this, CategoryPlace.class);

                startActivity(backIntent);
            }
        });

        loadFavouritePlaces();


    }

    private void loadFavouritePlaces() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference favouritesRef = FirebaseDatabase.getInstance().getReference().child("Favourites");
        Query favouritePlacesQuery = favouritesRef.orderByKey().startAt(userId+"_").endAt(userId+ "\uf8ff");

        favouritePlacesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot favouriteSnapshot : dataSnapshot.getChildren()) {
                    Favourite favourite = favouriteSnapshot.getValue(Favourite.class);
                    if (favourite != null) {
                        favouritePlaces.put(favourite.getPlaceId(), true);
                    }
                }
                loadPlaces();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработать ошибку
            }
        });
    }

    private void checkCategoy() {
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



    protected void  loadPlaces()
    {
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

                if (favouritePlaces.containsKey(model.getPlaceID())) {
                    boolean isFavourite = favouritePlaces.get(model.getPlaceID());
                    model.setFavourite(isFavourite);
                    updateFavouriteIcon(holder.favouriteIcon, isFavourite);
                }

                holder.favouriteIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isFavourite = model.isFavourite();
                        model.setFavourite(!isFavourite);
                        favouritePlaces.put(model.getPlaceID(), !isFavourite);
                        updateFavouriteIcon(holder.favouriteIcon, !isFavourite);

                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String placeId = model.getPlaceID();
                        String userId_placeId = userId + "_" + placeId;

                        DatabaseReference favouritesRef = FirebaseDatabase.getInstance().getReference().child("Favourites");

                        if (!isFavourite) {
                            Favourite favourite = new Favourite(userId, placeId, categoryName);
                            favouritesRef.child(userId_placeId).setValue(favourite);
                        } else {
                            favouritesRef.child(userId_placeId).removeValue();
                        }
                    }
                });

                holder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(PlaceActivity.this, PlaceDetailsActivity.class);
                        intent.putExtra("PlaceID", model.getPlaceID());
                        intent.putExtra("category", categoryName);
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

    private void updateFavouriteIcon(ImageView favouriteIcon, boolean isFavourite) {
        if (isFavourite) {
            favouriteIcon.setImageResource(R.drawable.favourite);
        } else {
            favouriteIcon.setImageResource(R.drawable.unfavourite);
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