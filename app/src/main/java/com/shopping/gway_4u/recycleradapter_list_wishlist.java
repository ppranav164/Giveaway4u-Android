package com.shopping.gway_4u;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class recycleradapter_list_wishlist extends RecyclerView.Adapter <recycleradapter_list_wishlist.MyViewHolder>  {


    Context ctx;

    config_hosts hosts = new config_hosts();

    String link;

    View view;


    JSONArray array;

    ArrayList<String> product_name = new ArrayList<>();

    ArrayList<String> model_no = new ArrayList<>();

    ArrayList<String> stock_avl = new ArrayList<>();

    ArrayList<String> special = new ArrayList<>();

    ArrayList<String> price_tag = new ArrayList<>();

    ArrayList<String> prod_id = new ArrayList<>();

    ArrayList<String> thumbnails = new ArrayList<>();

    ArrayList<String> removeLink = new ArrayList<>();


    public recycleradapter_list_wishlist(Context context, JSONArray mdata)
    {
        this.ctx = context;

        this.array = mdata;

       try {

          for (int i=0; i< array.length(); i++)
          {
              JSONObject object = array.getJSONObject(i);

              String ida = object.getString("product_id");

              String name = object.getString("name");

              String model = object.getString("model");

              String stock = object.getString("stock");

              String price = object.getString("price");

              String offer = object.getString("special");

              String remove = object.getString("remove");

              String images = object.getString("thumb");

              product_name.add(name);
              model_no.add(model);
              stock_avl.add(stock);
              price_tag.add(price);
              special.add(offer);
              prod_id.add(ida);
              thumbnails.add(images);

              removeLink.add(remove);




          }

       }catch (Exception e)
       {
           e.printStackTrace();
       }


    }


    public recycleradapter_list_wishlist(JSONArray mdata)
    {

        this.array = mdata;

        try {

            for (int i=0; i< array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                String ida = object.getString("product_id");

                String name = object.getString("name");

                String model = object.getString("model");

                String stock = object.getString("stock");

                String price = object.getString("price");

                String offer = object.getString("special");

                String remove = object.getString("remove");

                String images = object.getString("thumb");

                product_name.add(name);
                model_no.add(model);
                stock_avl.add(stock);
                price_tag.add(price);
                special.add(offer);
                prod_id.add(ida);
                thumbnails.add(images);

                removeLink.add(remove);


            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist,parent,false);

        MyViewHolder vh = new MyViewHolder(view);


        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {




        holder.name.setText(product_name.get(pos));

        holder.modelTv.setText(model_no.get(pos));

        holder.stockTv.setText(stock_avl.get(pos));

        holder.priceTv.setText(price_tag.get(pos));

        Picasso.get().load(thumbnails.get(pos)).into(holder.imageView);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                syncRemove(prod_id.get(holder.getAdapterPosition()));

                FragmentManager manager = ((FragmentActivity)ctx).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.mainframeL,new WishlistFragment(),"wishlist");
                transaction.addToBackStack("wishlist");
                transaction.commit();

                Snackbar.make(holder.remove,"Removed Successfully",Snackbar.LENGTH_SHORT).show();
            }
        });




        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String product_id = prod_id.get(holder.getAdapterPosition());

                Bundle bundle = new Bundle();

                bundle.putString("_id",product_id);

                Fragment fragment = new products_fragment(ctx);

                fragment.setArguments(bundle);

                FragmentManager manager = ((FragmentActivity)ctx).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.mainframeL,fragment,"product");
                transaction.addToBackStack("product");
                transaction.commit();


            }
        });

    }




    public void syncRemove(String id)
    {


         link = hosts.wishlist_remove+id;

         Log.e("remove wishlist id",id);

         Log.e("link wishlist remove",link);

         new syncRemoveWishlist(ctx,link,"GET").execute();


    }



    @Override
    public int getItemCount() {


        return array.length();

    }



    public class MyViewHolder extends RecyclerView.ViewHolder

    {


        TextView name,modelTv,stockTv,priceTv;

        ImageView imageView,cart,remove;

        LinearLayout linearLayout;




        public MyViewHolder( View itemView) {

            super(itemView);


            name = itemView.findViewById(R.id.name);

            imageView = itemView.findViewById(R.id.thumbs);

            modelTv = itemView.findViewById(R.id.models);

            stockTv = itemView.findViewById(R.id.status);

            priceTv = itemView.findViewById(R.id.prices);

            cart = itemView.findViewById(R.id.cart);

            remove = itemView.findViewById(R.id.remove);

            linearLayout = itemView.findViewById(R.id.llot);

        }

    }


}
