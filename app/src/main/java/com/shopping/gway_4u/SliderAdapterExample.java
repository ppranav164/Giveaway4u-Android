package com.shopping.gway_4u;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.sliderViewholders> {


    Context context;

    ArrayList<String> urlist = new ArrayList<>();

    ArrayList<String> productId = new ArrayList<>();

    ArrayList<String> TITLE = new ArrayList<>();

    JSONArray array;


    public SliderAdapterExample(Context context, JSONArray array) {

        this.context = context;

        this.array = array;

        try {

            for (int i=0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                String title = object.getString("title");

                String image = object.getString("image");

                //String product_id = object.getString("product_id");

                //productId.add(product_id);
                urlist.add(image);
                TITLE.add(title);

            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public sliderViewholders onCreateViewHolder(ViewGroup parent) {

        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageslider_layout, null);
        return new sliderViewholders(inflate);
    }

    @Override
    public void onBindViewHolder(sliderViewholders viewHolder, int position) {


        //Picasso.get().load(urlist.get(position)).into(viewHolder.imageView);

        //viewHolder.imageView.setTag(productId.get(position));

        Glide.with(viewHolder.itemView).load(urlist.get(position))
                .into(viewHolder.imageView);
        viewHolder.imageView.setTag(TITLE.get(position));

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuSearch(v.getTag().toString());
            }
        });

    }




    public boolean menuSearch(String tag)
    {

        context.startActivity(new Intent(context,searchProduct.class).putExtra("tag",tag));
        return true;
    }



    @Override
    public int getCount() {
        return array.length();
    }

    public class sliderViewholders extends SliderViewAdapter.ViewHolder
    {

        ImageView imageView;

        public sliderViewholders(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageslr);
        }
    }



}
