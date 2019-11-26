package com.example.harryvo.cleanermates;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.harryvo.cleanermates.Common.Common;
import com.example.harryvo.cleanermates.Model.Booking;
import com.example.harryvo.cleanermates.Model.Request;
import com.example.harryvo.cleanermates.ViewHolder.BookingViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookingStatus extends AppCompatActivity {


    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,BookingViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_status);


        //Firebase

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");


        recyclerView = (RecyclerView)findViewById(R.id.listBooking);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadBooking(Common.currentUser.getPhone());
    }

    private void loadBooking(String phone) {

    adapter = new FirebaseRecyclerAdapter<Request, BookingViewHolder>(
            Request.class,
            R.layout.booking_layout,
            BookingViewHolder.class,
            requests.orderByChild("phone")
            .equalTo(phone)
    ) {
        @Override
        protected void populateViewHolder(BookingViewHolder viewHolder, Request model, int position) {

            viewHolder.txtBookingId.setText(adapter.getRef(position).getKey());
            viewHolder.txtBookingStatus.setText(convertCodeToStatus(model.getStatus()));
            viewHolder.txtBookingAddress.setText(model.getAddress());
            viewHolder.txtBookingPhone.setText(model.getPhone());
        }
    };

    recyclerView.setAdapter(adapter);

    }

    private String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Booked";
        else if (status.equals("1"))
            return "We are confirming your booking";
        else
            return "Confirmed";
    }
}
