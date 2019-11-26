package com.example.harryvo.cleanermates;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.harryvo.cleanermates.Common.Common;
import com.example.harryvo.cleanermates.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference userDB;

    TextView User_name;

    CircleImageView User_pic;

    TextView User_address;

    TextView User_phone;

    TextView User_email;


    TextView User_suburb;

    ImageView User_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //Init Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        userDB = database.getReference("Users");


        User_address= (TextView)findViewById(R.id.user_address);
        User_email=(TextView)findViewById(R.id.user_email);
        User_name= (TextView)findViewById(R.id.user_name);
        User_phone=(TextView)findViewById(R.id.user_phone);
        User_suburb= (TextView)findViewById(R.id.user_suburb);
        User_pic=(CircleImageView)findViewById(R.id.user_pic);

        User_setting=(ImageView) findViewById(R.id.user_setting);

        //Edit profile

        User_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, EditProfile.class);
                startActivity(intent);
                finish();
                return;
            }
        });


        //Profile
        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.getKey().equals(mAuth.getCurrentUser().getUid())){
                        Common.currentUser = ds.getValue(User.class);
                        String userName= ds.child("name").getValue().toString();
                        User_name.setText(userName);
                    }

                    if(ds.getKey().equals(mAuth.getCurrentUser().getUid())){
                        Common.currentUser = ds.getValue(User.class);
                        String userAddress= ds.child("address").getValue().toString();
                        User_address.setText(userAddress);

                    }

                    if(ds.getKey().equals(mAuth.getCurrentUser().getUid())){
                        Common.currentUser = ds.getValue(User.class);
                        String userSubrub= ds.child("suburb").getValue().toString();
                        User_suburb.setText(userSubrub);
                    }

                    if(ds.getKey().equals(mAuth.getCurrentUser().getUid())){
                        Common.currentUser = ds.getValue(User.class);
                        String userPhone = ds.child("phone").getValue().toString();
                        User_phone.setText(userPhone);
                    }
                    if(ds.getKey().equals(mAuth.getCurrentUser().getUid())){
                        Common.currentUser = ds.getValue(User.class);
                        String userEmail = ds.child("email").getValue().toString();
                        User_email.setText(userEmail);
                    }

                    Glide.clear(User_pic);
                    if(ds.getKey().equals(mAuth.getCurrentUser().getUid())){
                        Common.currentUser = ds.getValue(User.class);
                        String userPic = ds.child("userpic").getValue().toString();
                        switch (userPic) {
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(User_pic);
                                break;
                            default:
                                Glide.with(getApplication()).load(userPic).into(User_pic);
                                break;
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
