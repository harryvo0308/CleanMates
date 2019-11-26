package com.example.harryvo.cleanermates.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harryvo.cleanermates.Interface.ItemClickListener;
import com.example.harryvo.cleanermates.R;

public class CleanListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView clean_name;
    public ImageView clean_image;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CleanListViewHolder(View itemView) {
        super(itemView);

        clean_name = (TextView)itemView.findViewById(R.id.clean_name);
         clean_image = (ImageView) itemView.findViewById(R.id.clean_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
