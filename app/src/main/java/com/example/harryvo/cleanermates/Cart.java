package com.example.harryvo.cleanermates;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harryvo.cleanermates.Common.Common;
import com.example.harryvo.cleanermates.Database.Database;
import com.example.harryvo.cleanermates.Model.Booking;
import com.example.harryvo.cleanermates.Model.Request;
import com.example.harryvo.cleanermates.ViewHolder.CartAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference requests;


    TextView txtTotalPrice;
    FButton btnBooking;

    List<Booking> cart = new ArrayList<>();

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);



        //Firebase

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Init

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice=(TextView)findViewById(R.id.total);
        btnBooking =(FButton)findViewById(R.id.btnPlaceOrder);

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAlertDialog();
            }
        });

        loadListClean();
    }

    private void showAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter your address: ");

        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        edtAddress.getText().toString(),
                        Common.currentUser.getName(),
                        txtTotalPrice.getText().toString(),
                        cart
                );



                //Submit to Firebase

                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);

                //Delete cart

                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this,"Thank you, Booking successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        alertDialog.show();
    }

    private void loadListClean() {


        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

       //Calculate total price

        int total = 0;
        for(Booking booking:cart)

            total+=(Integer.parseInt(booking.getPrice()))*(Integer.parseInt(booking.getRoom()));

        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));

    }
}
