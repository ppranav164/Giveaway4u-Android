package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class FullscreenSlider extends AppCompatActivity implements product_slider_listener  {


    ViewPager viewpager;

    String images;

    ArrayList<String> imageArrays;

    ImageButton closeBtn;

    TextView countView,totaltv;

    int viewFrom;

    product_slider_listener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();

        final Intent intent = getIntent();

        listener = this;

        imageArrays = intent.getStringArrayListExtra("image");

        int pos  = intent.getIntExtra("position",0);

        int at = pos+1;

        viewFrom = at;

        for (int i=0; i < imageArrays.size(); i++)
        {
            Log.e("images",imageArrays.get(i));
        }


        setContentView(R.layout.activity_fullscreen_slider);

        viewpager = findViewById(R.id.fullpager);

        closeBtn = findViewById(R.id.closeslider);

        countView = findViewById(R.id.counts);

        totaltv = findViewById(R.id.total);

        viewpager.setAdapter(new slide_adapter_product(getApplicationContext(),imageArrays,listener));


        int totalImages = imageArrays.size();

        totaltv.setText(""+totalImages);
        countView.setText(""+viewFrom);

        viewpager.setCurrentItem(pos);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

              int currunt = position+1;

              countView.setText(""+currunt);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public void isClicked(boolean clicked, int position) {

    }

}