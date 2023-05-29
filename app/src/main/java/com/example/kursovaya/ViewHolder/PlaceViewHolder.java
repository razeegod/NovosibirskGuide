package com.example.kursovaya.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursovaya.Places.Interface.ItemClickListner;
import com.example.kursovaya.R;

public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtPlaceName_List, txtPlaceSchedule_List;
    public ImageView PlaceImage_List, favouriteIcon;
    public ItemClickListner Listner;


    public PlaceViewHolder(View itemView)
    {
        super(itemView);

        PlaceImage_List = itemView.findViewById(R.id.place_image_list);
        txtPlaceSchedule_List = itemView.findViewById(R.id.place_schedule_list);
        txtPlaceName_List = itemView.findViewById(R.id.place_name_list);
        favouriteIcon = itemView.findViewById(R.id.favourite_icon);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.Listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        Listner.onClick(view, getAdapterPosition(), false);
    }
}
