package com.example.harryvo.cleanermates.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.harryvo.cleanermates.Interface.ItemClickListener;
import com.example.harryvo.cleanermates.R;

public class BookingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtBookingId, txtBookingStatus, txtBookingPhone, txtBookingAddress;

    private ItemClickListener itemClickListener;

    public BookingViewHolder(View itemView) {
        super(itemView);

        txtBookingAddress = (TextView)itemView.findViewById(R.id.booking_address);
        txtBookingPhone = (TextView)itemView.findViewById(R.id.booking_phone);
        txtBookingId = (TextView)itemView.findViewById(R.id.booking_id);
        txtBookingStatus = (TextView)itemView.findViewById(R.id.booking_status);


        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
