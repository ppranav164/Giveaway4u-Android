package com.shopping.gway_4u;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class featured_recycler_adapter extends RecyclerView.Adapter <featured_recycler_adapter.MyViewHolder>  {


    Context ctx;


    JSONArray array;

    config_hosts hosts = new config_hosts();

    HashMap<String,String> keyvalue = new HashMap<>();

    ArrayList<String> currentProductId = new ArrayList<>(); // products listed by recyclerview

    HashMap<String,String> keyvals = new HashMap<>();

    ArrayList<String> wishlists;


    public featured_recycler_adapter(Context context, JSONArray data,ArrayList<String> wishlistdata)
    {
        this.ctx = context;

        this.array = data;

        this.wishlists = wishlistdata;

        //This is a module for "Latest Products" Part

    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productsimages_featured,parent,false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {


        ArrayList<String> urlist = new ArrayList<>();

        ArrayList<String> tlist = new ArrayList<>();

        ArrayList<String> plist = new ArrayList<>();

        final ArrayList<String> Llist = new ArrayList<>();

        final ArrayList<String> ids = new ArrayList<>();



        try {

           for (int i=0; i<array.length(); i++)
           {
               JSONObject idobj = array.getJSONObject(i);

               String prod = idobj.getString("thumb");

               String name = idobj.getString("name");

               String price = idobj.getString("price");

               String link = idobj.getString("href");

               String id = idobj.getString("product_id");

               urlist.add(prod);
               tlist.add(name.replace("&quot;",""));
               plist.add(price);
               ids.add(id);
               currentProductId.add(id);

               Log.e("Latest Product Name",idobj.getString("name"));

           }

           //Toast.makeText(ctx,"Data "+urlist,Toast.LENGTH_LONG).show();

           Picasso.get().load(urlist.get(pos)).into(holder.imageView);

           holder.textView.setText(tlist.get(pos));

           holder.Tprice.setText(plist.get(pos));



       }catch (Exception e )
       {
           e.printStackTrace();
       }



        //Picasso.get().load(data[pos]).into( holder.imageView);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(ctx,"Link :"+ids.get(holder.getPosition()),Toast.LENGTH_LONG).show();

                String id = ids.get(holder.getPosition());

                Bundle bundle = new Bundle();

                bundle.putString("_id",id);

                bundle.putString("product_id","Iphone X");

                Fragment fragment = new products_fragment(ctx);

                fragment.setArguments(bundle);

                FragmentManager manager = ((FragmentActivity)ctx).getSupportFragmentManager();

                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.mainframeL,fragment,"product");
                transaction.addToBackStack("main");

                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                transaction.commit();
            }
        });




       holder.linearLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               //Toast.makeText(ctx,"Link :"+ids.get(holder.getPosition()),Toast.LENGTH_LONG).show();

               String id = ids.get(holder.getPosition());

               Bundle bundle = new Bundle();

               bundle.putString("_id",id);

               bundle.putString("product_id","Iphone X");

               Fragment fragment = new products_fragment(ctx);

               fragment.setArguments(bundle);

               FragmentManager manager = ((FragmentActivity)ctx).getSupportFragmentManager();

               FragmentTransaction transaction = manager.beginTransaction();

               transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
               transaction.replace(R.id.mainframeL,fragment,"product");
               transaction.addToBackStack("main");

               transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

               transaction.commit();
           }
       });



        holder.wishbutton.setId(Integer.parseInt(ids.get(pos)));

        holder.wishbutton.setBackgroundResource(R.drawable.wishlist);

        holder.wishbutton.setTag("yellow");

       Iterator<String> iterator = wishlists.iterator();

       while (iterator.hasNext())
       {

           String idee = iterator.next();

           if (ids.get(pos).equals(idee))
           {

               holder.wishbutton.setBackgroundResource(R.drawable.wishlistp);
               holder.wishbutton.setTag("red");

           }

       }



       holder.wishbutton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               String tag = String.valueOf(v.getId());

               String color = v.getTag().toString();

               ImageButton buttonw = v.findViewById(v.getId());

               buttonw.setBackgroundResource(R.drawable.wishlistp);
               buttonw.setTag("red");

               switch (color)
               {
                   case "yellow" :  buttonw.setBackgroundResource(R.drawable.wishlistp);
                       buttonw.setTag("red");
                       addWishlist(Integer.parseInt(tag));
                       break;

                   case "red"    :  buttonw.setBackgroundResource(R.drawable.wishlist);
                       buttonw.setTag("yellow");
                       removeWishlist(Integer.parseInt(tag));
                       break;
               }

           }
       });



    }





    public void addWishlist(int id)
    {

        new syncWishlist(ctx,id).execute();

        Log.e("wishlistadd",String.valueOf(id));

    }



    public void removeWishlist(int id)
    {

        String link = hosts.wishlist_remove+id;

        new syncRemoveWishlist(ctx,link,"GET").execute();

    }









    @Override
    public int getItemCount() {


        return array.length();


    }








    public class MyViewHolder extends RecyclerView.ViewHolder

    {

        ImageView imageView;

        TextView textView; TextView Tprice;

        RelativeLayout linearLayout;

        ImageButton wishbutton;


        public MyViewHolder( View itemView) {

            super(itemView);

            imageView = itemView.findViewById(R.id.imageContainer);

            textView = itemView.findViewById(R.id.details);

            Tprice = itemView.findViewById(R.id.price);

            linearLayout = itemView.findViewById(R.id.lu);

            wishbutton = itemView.findViewById(R.id.wishlistB);


        }

    }


}
