package com.shopping.gway_4u;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class previews_adapter extends RecyclerView.Adapter <previews_adapter.MyViewHolder>  {


    Context ctx;


    ArrayList<String> array;
    ArrayList<String> titles;



    public previews_adapter(Context context, ArrayList<String> data,ArrayList<String> title)
    {
        this.ctx = context;
        this.array = data;
        this.titles = title;

    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.previewuploads,parent,false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {


        Picasso.get().load(array.get(pos)).into( holder.imageView);

        holder.textView.setText(titles.get(pos));

    }

    @Override
    public int getItemCount() {


        return array.size();


    }








    public class MyViewHolder extends RecyclerView.ViewHolder

    {

        ImageView imageView;

        TextView textView; TextView Tprice;

        RelativeLayout linearLayout;


        public MyViewHolder( View itemView) {

            super(itemView);

            imageView = itemView.findViewById(R.id.imageContainer);

            textView = itemView.findViewById(R.id.details);

            Tprice = itemView.findViewById(R.id.price);

            linearLayout = itemView.findViewById(R.id.lu);


        }

    }


}
