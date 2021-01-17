package com.shopping.gway_4u;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class recycleradapter_cart extends RecyclerView.Adapter <recycleradapter_cart.MyViewHolder>  {


    Context ctx;

    config_hosts hosts = new config_hosts();

    String link;

    View view;

    ListView listView;


    JSONArray array,Totals;

    ArrayList<String> product_name = new ArrayList<>();

    ArrayList<String> model_no = new ArrayList<>();

    ArrayList<String> stock_avl = new ArrayList<>();

    ArrayList<String> special = new ArrayList<>();

    ArrayList<String> price_tag = new ArrayList<>();

    ArrayList<String> prod_id = new ArrayList<>();

    ArrayList<String> thumbnails = new ArrayList<>();

    ArrayList<String> removeLink = new ArrayList<>();

    ArrayList<String> cartid = new ArrayList<>();

    ArrayList<String> options = new ArrayList<>();

    ArrayList<String> recurring = new ArrayList<>();

    ArrayList<String> quantity_arr = new ArrayList<>();

    ArrayList<String> reward = new ArrayList<>();

    ArrayList<String> total_values = new ArrayList<>();



    ArrayList<String> title = new ArrayList<>();


    private Listener listener;


    public recycleradapter_cart(Listener listener)
    {
        this.listener = listener;
    }


    public recycleradapter_cart(Context context, JSONArray mdata, JSONArray Totals)
    {
        this.ctx = context;

        this.array = mdata;

        this.Totals = Totals;

        try {


            for (int i =0; i < array.length(); i++){

                JSONObject object = array.getJSONObject(i);

                String cart_id = object.getString("cart_id");
                String thumb = object.getString("thumb");
                String name = object.getString("name");
                String model = object.getString("model");
                String option = object.getString("option");
                String recurring = object.getString("recurring");
                String quantity = object.getString("quantity");
                String stock = object.getString("stock");
                String reward = object.getString("reward");
                String price = object.getString("price");
                String total = object.getString("total");



                product_name.add(name);
                thumbnails.add(thumb);
                model_no.add(model);
                options.add(option);
                quantity_arr.add(quantity);
                stock_avl.add(stock);
                price_tag.add(price);
                total_values.add(total);
                cartid.add(cart_id);


            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }





    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carts,parent,false);

        MyViewHolder vh = new MyViewHolder(view);


        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {



        if (stock_avl.get(pos) == "false")
        {

            holder.stockTv.setText("Not Available");
            holder.stockTv.setTextColor(Color.parseColor("#ED1C24"));

        }else {

            holder.stockTv.setVisibility(View.GONE);
        }



        holder.name.setText(product_name.get(pos));

        holder.modelTv.setText(model_no.get(pos));

        holder.priceTv.setText(price_tag.get(pos));

        holder.quantityTv.setText("Quantity :" + quantity_arr.get(pos));

        Picasso.get().load(thumbnails.get(pos)).into(holder.imageView);


        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int key = Integer.parseInt(cartid.get(pos));

                syncRemove(key,holder);

            }
        });


    }




    public void syncRemove(int key, final MyViewHolder holder)
    {

         link = hosts.removeCart;

         new syncEvent(ctx, link, "POST", key, new info() {
             @Override
             public void getInfo(String data) {

                 try {
                     JSONObject object = new JSONObject(data);

                     int total = object.getInt("total");

                     //Toast.makeText(ctx,"Total"+total,Toast.LENGTH_SHORT).show();

                     redirectCart(holder);

                 }catch (Exception e)
                 {
                     e.printStackTrace();
                 }

             }
         }).execute();


    }



    public void redirectCart(MyViewHolder holder)
    {

        FragmentManager manager = ((FragmentActivity)ctx).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainframeL,new cartFragment(true),"cart");
        transaction.addToBackStack("cart");
        transaction.commit();

        Snackbar.make(holder.remove,"Removed Successfully",Snackbar.LENGTH_SHORT).show();

    }



    @Override
    public int getItemCount() {


        return array.length();

    }



    public class MyViewHolder extends RecyclerView.ViewHolder

    {


        TextView name,modelTv,stockTv,priceTv,quantityTv;

        ImageView imageView,cart,remove;

        LinearLayout linearLayout;




        public MyViewHolder( View itemView) {

            super(itemView);


            name = itemView.findViewById(R.id.name);

            imageView = itemView.findViewById(R.id.thumbs);

            modelTv = itemView.findViewById(R.id.models);

            stockTv = itemView.findViewById(R.id.status);

            priceTv = itemView.findViewById(R.id.prices);

            quantityTv = itemView.findViewById(R.id.quantity);

            cart = itemView.findViewById(R.id.cart);

            linearLayout = itemView.findViewById(R.id.llot);

            remove = itemView.findViewById(R.id.removebtn);

        }

    }


}
