package com.example.kursovaya.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursovaya.Places.Interface.OnFavouriteClickListener;
import com.example.kursovaya.R;
import com.example.kursovaya.model.Places;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouritePlacesAdapter extends RecyclerView.Adapter<FavouritePlacesAdapter.FavouritePlacesViewHolder> {

    private List<Places> favouritePlacesList;
    private OnFavouriteClickListener onFavouriteClickListener;
    private String userId;

    public FavouritePlacesAdapter(List<Places> favouritePlacesList, OnFavouriteClickListener onFavouriteClickListener, String userId) {
        this.favouritePlacesList = favouritePlacesList;
        this.onFavouriteClickListener = onFavouriteClickListener;
        this.userId = userId;
    }

    @NonNull
    @Override
    public FavouritePlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item_layout, parent, false);
        return new FavouritePlacesViewHolder(itemView, onFavouriteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritePlacesViewHolder holder, int position) {
        Places place = favouritePlacesList.get(position);

        holder.placeName.setText(place.getName());
        holder.placeSchedule.setText(place.getSchedule());

        Picasso.get().load(place.getImage()).into(holder.placeImage);

        checkIfFavourite(place, holder);
    }

    private void checkIfFavourite(Places place, FavouritePlacesViewHolder holder) {
        DatabaseReference favouritesRef = FirebaseDatabase.getInstance().getReference().child("Favourites");
        String favouriteKey = userId + "_" + place.getPlaceID();

        favouritesRef.child(favouriteKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.favouriteIcon.setImageResource(R.drawable.favourite);
                } else {
                    holder.favouriteIcon.setImageResource(R.drawable.unfavourite);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error here
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouritePlacesList.size();
    }

    public void removeFavourite(String userId, String placeId) {
        DatabaseReference favouritesRef = FirebaseDatabase.getInstance().getReference().child("Favourites");
        String favouriteKey = userId + "_" + placeId;
        favouritesRef.child(favouriteKey).removeValue();
    }


    public static class FavouritePlacesViewHolder extends RecyclerView.ViewHolder {
        ImageView favouriteIcon;
        TextView placeName;
        ImageView placeImage;
        TextView placeSchedule;

        public FavouritePlacesViewHolder(View itemView, OnFavouriteClickListener onFavouriteClickListener) {
            super(itemView);
            favouriteIcon = itemView.findViewById(R.id.favourite_icon);
            placeName = itemView.findViewById(R.id.place_name_list);
            placeImage = itemView.findViewById(R.id.place_image_list);
            placeSchedule = itemView.findViewById(R.id.place_schedule_list);

            favouriteIcon.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onFavouriteClickListener.onFavouriteClick(position);
                }
            });
        }
    }
}
