package com.example.harryvo.cleanermates.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.harryvo.cleanermates.Interface.ItemClickListener;
import com.example.harryvo.cleanermates.R;

public class NewsfeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtMenuName;
    public ImageView imageView;
    public RatingBar ratingBar;


    private ItemClickListener itemClickListener;

    public NewsfeedViewHolder(View itemView) {
        super(itemView);

        txtMenuName = (TextView)itemView.findViewById(R.id.menu_name);
        imageView = (ImageView) itemView.findViewById(R.id.menu_image);
        ratingBar = (RatingBar)itemView.findViewById(R.id.menu_rating);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
     itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
