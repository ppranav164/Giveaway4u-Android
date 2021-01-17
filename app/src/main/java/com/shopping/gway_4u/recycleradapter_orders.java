package com.shopping.gway_4u;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class recycleradapter_orders extends RecyclerView.Adapter <recycleradapter_orders.MyViewHolder>  {


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

    ArrayList<String> orderId = new ArrayList<>();

    ArrayList<String> dateOfOrder = new ArrayList<>();

    ArrayList<String> brand = new ArrayList<>();

    ArrayList<String> images = new ArrayList<>();

    ArrayList<Boolean> isReviewed = new ArrayList<>();

    JSONArray thumblink;

    public recycleradapter_orders(Context context, JSONArray mdata)
    {
        this.ctx = context;

        this.array = mdata;

        try {

            for (int i=0; i< array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                String order_id = object.getString("order_id");
                String stock = object.getString("status");
                String dated = object.getString("date_added");
                String price = object.getString("total");
                String name = object.getString("name");

                boolean reviewed = object.getBoolean("isreviewed");

                images.add(object.getJSONArray("images").toString());


//              String images = object.getString("image");
//              String branddetail = object.getString("brand");

                orderId.add(order_id);
                dateOfOrder.add(dated);
                stock_avl.add(stock);
                price_tag.add(price);
                product_name.add(name);
                isReviewed.add(reviewed);

//              thumbnails.add(images);
//              brand.add(branddetail);


            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }




    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders,parent,false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {


        //holder.name.setText(brand.get(pos));

        holder.dateviewTv.setText("Order Date : "+dateOfOrder.get(pos));

        holder.orderIdtv.setText("Order ID : #"+orderId.get(pos));

        holder.stockTv.setText(stock_avl.get(pos));

        holder.priceTv.setText(price_tag.get(pos));

        holder.reviewBtn.setTag(orderId.get(pos));


        if (isReviewed.get(pos) == false && stock_avl.get(pos).equals("Complete"))
        {
            holder.reviewBtn.setText("Write a Feedback");
            holder.reviewBtn.setBackground(ctx.getDrawable(R.drawable.dusk_button));
        }


        if (isReviewed.get(pos) == true)
        {

            holder.reviewBtn.setVisibility(View.GONE);

        }else {

            holder.reviewBtn.setClickable(true);
        }

        if (stock_avl.get(pos).equals("Complete"))
        {
            holder.reviewBtn.setVisibility(View.VISIBLE);

        }else {

            holder.reviewBtn.setVisibility(View.INVISIBLE);
        }


        //Picasso.get().load(thumbnails.get(pos)).into(holder.imageView);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView[] imageViews = new ImageView[5];

        try {

            String arrays = images.get(pos);

            JSONArray array = new JSONArray(arrays);

            for (int i=0; i < array.length(); i++)
            {
                JSONObject jsonObject = array.getJSONObject(i);

                String thum = jsonObject.getString("thumb");
                imageViews[i] = new ImageView(ctx.getApplicationContext());
                holder.relativeLayout.addView(imageViews[i]);
                Picasso.get().load(thum).resize(300,300).into(imageViews[i]);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        holder.setIsRecyclable(false);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String orderIds = orderId.get(holder.getAdapterPosition());
                Intent intent = new Intent(ctx,orderInfo.class);
                intent.putExtra("order_id",orderIds);
                ctx.startActivity(intent);
            }
        });


        holder.trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String orderIds = orderId.get(holder.getAdapterPosition());
                String orderDate = dateOfOrder.get(holder.getAdapterPosition());

                Intent intent = new Intent(ctx,TrackStepper.class);
                intent.putExtra("order_id",orderIds);
                intent.putExtra("order_date",orderDate);
                ctx.startActivity(intent);

            }
        });





        holder.reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int position = holder.getAdapterPosition();

                boolean isReview = isReviewed.get(position);

                if (isReview == true)
                {
                    getDetails(position);

                }else {

                    writeReview(v.getTag().toString());
                }

            }
        });


    }



    public  void writeReview(String orderid)
    {
        String ORDER_ID = orderid;
        Intent intent = new Intent(ctx,ReviewOrder.class);
        intent.putExtra("order_id",ORDER_ID);
        ctx.startActivity(intent);
    }


    public void getDetails(int position) {

        String orderIds = orderId.get(position);
        Intent intent = new Intent(ctx,orderInfo.class);
        intent.putExtra("order_id",orderIds);
        ctx.startActivity(intent);
    }

    public void openReviews()
    {

        ctx.startActivity(new Intent(ctx,customer_reviews.class));
    }




    @Override
    public int getItemCount() {


        return array.length();

    }



    public class MyViewHolder extends RecyclerView.ViewHolder

    {


        TextView name,modelTv,stockTv,priceTv,shipto,orderIdtv,dateviewTv;

        ImageView imageView,cart,remove;

        LinearLayout linearLayout; LinearLayout relativeLayout;

        Button trackButton,reviewBtn;



        public MyViewHolder( View itemView) {

            super(itemView);


            name = itemView.findViewById(R.id.name);

            imageView = itemView.findViewById(R.id.thumbs);

            modelTv = itemView.findViewById(R.id.models);

            stockTv = itemView.findViewById(R.id.status);

            priceTv = itemView.findViewById(R.id.prices);

            cart = itemView.findViewById(R.id.cart);

            linearLayout = itemView.findViewById(R.id.llot);

            orderIdtv = itemView.findViewById(R.id.orderid);

            dateviewTv = itemView.findViewById(R.id.datedoforder);

            relativeLayout = itemView.findViewById(R.id.productImageshow);

            trackButton = itemView.findViewById(R.id.track);

            reviewBtn = itemView.findViewById(R.id.revieworder);

        }

    }


}
