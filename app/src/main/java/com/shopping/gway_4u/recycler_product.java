package com.shopping.gway_4u;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class recycler_product extends RecyclerView.Adapter<recycler_product.MyViewHolder> {



    @Override
    public recycler_product.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productsimages,parent,false);

        recycler_product.MyViewHolder vh = new recycler_product.MyViewHolder(view);


        return vh;
    }


    @Override
    public void onBindViewHolder(final recycler_product.MyViewHolder holder, final int pos) {



    }

    @Override
    public int getItemCount() {


        return 0;


    }





    public class MyViewHolder extends RecyclerView.ViewHolder

    {

        ImageView imageView;

        TextView textView; TextView Tprice;


        public MyViewHolder( View itemView) {

            super(itemView);

            imageView = itemView.findViewById(R.id.imageContainer);

            textView = itemView.findViewById(R.id.details);

            Tprice = itemView.findViewById(R.id.price);


        }

    }



}
