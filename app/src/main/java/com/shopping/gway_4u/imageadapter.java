package com.shopping.gway_4u;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class imageadapter extends BaseAdapter {


    Context context;
    LayoutInflater inflater;
    int[] imgs;


    public imageadapter(Context context,int[] images) {

        this.context = context;
        this.imgs = images;
        this.inflater = (LayoutInflater.from(context));

    }

    @Override
    public int getCount() {

        return imgs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.productsimages,null);

        ImageView prods = (ImageView) convertView.findViewById(R.id.imageContainer);

        prods.setImageResource(imgs[position]);

        return convertView;
    }



}
