package com.example.harryvo.cleanermates;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.harryvo.cleanermates.Common.Common;
import com.example.harryvo.cleanermates.Interface.ItemClickListener;
import com.example.harryvo.cleanermates.Model.Category;
import com.example.harryvo.cleanermates.Model.User;
import com.example.harryvo.cleanermates.ViewHolder.NewsfeedViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference category, userDB;

    TextView txtFullName;
    CircleImageView User_pic;

    RecyclerView recycler_newsfeed;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category,NewsfeedViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("News Feed");
        setSupportActionBar(toolbar);

        //Init Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        category = database.getReference("Category");
        userDB = database.getReference("Users");





        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Home.this, Cart.class);
                startActivity(cartIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Set Name for user
        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);

        User_pic = (CircleImageView)headerView.findViewById(R.id.profilepic);

        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(mAuth.getCurrentUser().getUid())) {
                        Common.currentUser = ds.getValue(User.class);
                        String fullName = ds.child("name").getValue().toString();
                        txtFullName.setText(fullName);
                    }
                    Glide.clear(User_pic);
                    if (ds.getKey().equals(mAuth.getCurrentUser().getUid())) {
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


        //Load news feed
        recycler_newsfeed=(RecyclerView)findViewById(R.id.recycle_menu);
        recycler_newsfeed.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_newsfeed.setLayoutManager(layoutManager);

        loadNewsfeed();




    }

    private void loadNewsfeed() {

        adapter = new FirebaseRecyclerAdapter<Category, NewsfeedViewHolder>(Category.class,R.layout.menu_item,NewsfeedViewHolder.class,category) {
            @Override
            protected void populateViewHolder(NewsfeedViewHolder viewHolder, Category model, int position) {
            viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                .into(viewHolder.imageView);
                viewHolder.ratingBar.setRating(model.getRating());

                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                       //Get CategoryId and send to new Activity

                        Intent cleanList= new Intent(Home.this,CleanList.class);
                        cleanList.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(cleanList);
                    }
                });

            }
        };
        recycler_newsfeed.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_newsfeed) {
            Intent homeIntent = new Intent(Home.this,Home.class);
            startActivity(homeIntent);

        } else if (id == R.id.nav_profile) {
            Intent profileIntent = new Intent(Home.this,Profile.class);
            startActivity(profileIntent);
        } else if (id == R.id.nav_booking) {
            Intent bookingIntent = new Intent(Home.this,BookingStatus.class);
            startActivity(bookingIntent);

        } else if (id == R.id.nav_logout) {

       Intent signIn = new Intent(Home.this,MainActivity.class);
       signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
       startActivity(signIn);




    }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;



    }



}
