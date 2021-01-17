package com.shopping.gway_4u;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class recycleradapter_reviews extends RecyclerView.Adapter <recycleradapter_reviews.MyViewHolder>  {


    Context ctx;


    JSONArray array;

    ArrayList<String> name = new ArrayList<>();

    ArrayList<String> feedbacks = new ArrayList<>();

    ArrayList<String> ratings = new ArrayList<>();

    ArrayList<String> addedOn = new ArrayList<>();


    public recycleradapter_reviews(Context context, JSONArray mdata)
    {
        this.ctx = context;

        this.array = mdata;

        try {

           for (int i=0; i < array.length(); i++)
           {
               JSONObject object = mdata.getJSONObject(i);

               name.add(object.getString("author"));
               feedbacks.add(object.getString("text"));
               ratings.add(object.getString("rating"));
               addedOn.add(object.getString("date_added"));
           }


        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews,parent,false);

        MyViewHolder vh = new MyViewHolder(view);


        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {


        //holder.textView.setText(tlist.get(pos));

        holder.client.setText(name.get(pos));

        holder.comments.setText(feedbacks.get(pos));


        holder.ratingBar.setRating(Float.parseFloat(ratings.get(pos)));

        holder.date.setText(addedOn.get(pos));


    }

    @Override
    public int getItemCount() {


        return array.length();

    }



    public class MyViewHolder extends RecyclerView.ViewHolder

    {



        TextView client; TextView comments; TextView date;

        RatingBar ratingBar;


        public MyViewHolder( View itemView) {

            super(itemView);


            client = itemView.findViewById(R.id.client);

            comments = itemView.findViewById(R.id.comment);

            ratingBar = itemView.findViewById(R.id.ratingbutton);

            date = itemView.findViewById(R.id.dates);

        }

    }


}
