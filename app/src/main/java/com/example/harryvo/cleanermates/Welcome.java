package com.example.harryvo.cleanermates;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.harryvo.cleanermates.Common.Common;


public class Welcome extends AppCompatActivity {

     //Time to launch the another activity
    AnimationDrawable animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final ImageView loading = findViewById(R.id.imageView5);
        animation = (AnimationDrawable) loading.getDrawable();
        animation.start();


        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(Welcome.this, Home.class);
                startActivity(i);
                finish();
            }

        }, 3000L);

    }
}
