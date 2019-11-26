package com.example.harryvo.cleanermates;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.harryvo.cleanermates.Interface.ItemClickListener;
import com.example.harryvo.cleanermates.Model.Clean;
import com.example.harryvo.cleanermates.ViewHolder.CleanListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CleanList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference cleanList;
    String categoryId = "";

    FirebaseRecyclerAdapter<Clean, CleanListViewHolder> adapter;


    //Search Function

    FirebaseRecyclerAdapter<Clean, CleanListViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_list);


        //FireBase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        cleanList = database.getReference("Clean");

        recyclerView = (RecyclerView) findViewById(R.id.recycle_clean);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent here

        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");


        if (!categoryId.isEmpty() && categoryId != null) {
            loadListClean(categoryId);

        }

        ///Search

        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Search");
        loadSuggest(); // Function load suggest
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }

                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                ///When search bar close
                // Restore original suggest adapter

                if (!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //When finish search
                //show result search adapter
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Clean, CleanListViewHolder>(
                Clean.class,
                R.layout.cleanitem,
                CleanListViewHolder.class,
                cleanList.orderByChild("Name").equalTo(text.toString()) // Compare name
        ) {
            @Override
            protected void populateViewHolder(CleanListViewHolder viewHolder, Clean model, int position) {
                adapter = new FirebaseRecyclerAdapter<Clean, CleanListViewHolder>(Clean.class,
                        R.layout.cleanitem,
                        CleanListViewHolder.class,
                        cleanList.orderByChild("CleanID").equalTo(categoryId)) {
                    @Override
                    protected void populateViewHolder(CleanListViewHolder viewHolder, Clean model, int position) {
                        viewHolder.clean_name.setText(model.getName());
                        Picasso.with(getBaseContext()).load(model.getImage()).
                                into(viewHolder.clean_image);

                        final Clean local = model;
                        viewHolder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                //New activity

                                Intent cleanDetail = new Intent(CleanList.this, CleanDetail.class);
                                cleanDetail.putExtra("CleanID", adapter.getRef(position).getKey()); //Send clean id to new activity
                                startActivity(cleanDetail);
                            }
                        });
                    }

                };

                recyclerView.setAdapter(searchAdapter);// Set adapter for recyclerview
            }
        };
    }

    private void loadSuggest () {
            cleanList.orderByChild("CleanID").equalTo(categoryId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Clean item = postSnapshot.getValue(Clean.class);
                                suggestList.add(item.getName()); //Add name to suggest list
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

    private void loadListClean (String categoryId){
            adapter = new FirebaseRecyclerAdapter<Clean, CleanListViewHolder>(Clean.class,
                    R.layout.cleanitem,
                    CleanListViewHolder.class,
                    cleanList.orderByChild("CleanID").equalTo(categoryId)) {
                @Override
                protected void populateViewHolder(CleanListViewHolder viewHolder, Clean model, int position) {
                    viewHolder.clean_name.setText(model.getName());
                    Picasso.with(getBaseContext()).load(model.getImage()).
                            into(viewHolder.clean_image);

                    final Clean local = model;
                    viewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            //New activity

                            Intent cleanDetail = new Intent(CleanList.this, CleanDetail.class);
                            cleanDetail.putExtra("CleanID", adapter.getRef(position).getKey()); //Send clean id to new activity
                            startActivity(cleanDetail);
                        }
                    });

                }
            };


            //Set Adapter

            recyclerView.setAdapter(adapter);
        }
}