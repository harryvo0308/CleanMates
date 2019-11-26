package com.example.harryvo.cleanermates;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.harryvo.cleanermates.Database.Database;
import com.example.harryvo.cleanermates.Model.Booking;
import com.example.harryvo.cleanermates.Model.Clean;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CleanDetail extends AppCompatActivity {


    TextView clean_name,clean_price, clean_description;
    ImageView clean_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String cleanId="";

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference clean;

    Clean currentClean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_detail);


        //Firebase

        mAuth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        clean=database.getReference("Clean");

        //Init View
        numberButton= (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart= (FloatingActionButton)findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Booking(
                        cleanId,
                        currentClean.getName(),
                        numberButton.getNumber(),
                        currentClean.getPrice(),
                        currentClean.getDiscount()

                ));

                Toast.makeText(CleanDetail.this, "Add to Cart",Toast.LENGTH_SHORT).show();
            }
        });

        clean_description= (TextView)findViewById(R.id.clean_description);
        clean_name= (TextView)findViewById(R.id.clean_name);
        clean_price= (TextView)findViewById(R.id.clean_price);
        clean_image=(ImageView)findViewById(R.id.img_clean);


        collapsingToolbarLayout= (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandeAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);

        //Get clean id from intent

        if(getIntent() !=null)

            cleanId= getIntent().getStringExtra("CleanID");
        if (!cleanId .isEmpty())
        {
            getDetailClean(cleanId);
        }
    }

    private void getDetailClean(String cleanId) {
        clean.child(cleanId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentClean= dataSnapshot.getValue(Clean.class);


                //Set Image
                Picasso.with(getBaseContext()).load(currentClean.getImage())
                .into(clean_image);

                collapsingToolbarLayout.setTitle(currentClean.getName());

                clean_price.setText(currentClean.getPrice());

                clean_name.setText(currentClean.getName());

                clean_description.setText(currentClean.getDecription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
