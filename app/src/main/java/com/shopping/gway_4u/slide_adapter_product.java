package com.shopping.gway_4u;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class slide_adapter_product extends PagerAdapter {

    Context context;

    ArrayList<String> imageList = new ArrayList<>();

    product_slider_listener listener;

    public slide_adapter_product(Context context, ArrayList<String> image,product_slider_listener listener) {

        this.context = context;
        this.imageList = image;
        this.listener = listener;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == ((ImageView) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {


        PhotoView photoView = new PhotoView(context);

        Picasso.get().load(imageList.get(position)).into(photoView);

        ((ViewPager) container).addView(photoView,0);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.isClicked(true,0);
            }
        });

        return photoView;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }

    @Override
    public int getCount() {

        return imageList.size();
    }
}
