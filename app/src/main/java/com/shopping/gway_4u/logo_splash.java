package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class logo_splash extends AppCompatActivity {


    private Handler handler;
    ImageView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_splash);

        loader = findViewById(R.id.loading);

        Glide.with(getApplicationContext()).asGif().load(R.drawable.loading).into(loader);


        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(logo_splash.this,MainActivity.class);
                startActivity(intent);
                finish();


            }
        },2000);


    }



}
