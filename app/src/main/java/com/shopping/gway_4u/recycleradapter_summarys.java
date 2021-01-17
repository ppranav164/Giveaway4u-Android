package com.shopping.gway_4u;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class recycleradapter_summarys extends RecyclerView.Adapter <recycleradapter_summarys.MyViewHolder>  {


    Context ctx;

    JSONArray array;

    ArrayList<String> titles = new ArrayList<>();

    ArrayList<String> texts = new ArrayList<>();


    public recycleradapter_summarys(Context context, JSONArray mdata)
    {
        this.ctx = context;

        this.array = mdata;

       try {

          for (int i=0; i< array.length(); i++)
          {
              JSONObject object = array.getJSONObject(i);
              String title = object.getString("title");
              String text = object.getString("text");
              titles.add(title);
              texts.add(text);

          }

       }catch (Exception e)
       {
           e.printStackTrace();
       }


    }




    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary,parent,false);

        MyViewHolder vh = new MyViewHolder(view);


        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {



        try {
            JSONObject object = array.getJSONObject(pos);

            holder.titletv.setText(object.getString("title"));

            Log.e("title",object.getString("title"));

            if (object.getString("title").equals("Free Shipping"))
            {
                holder.titletv.setText("Service Charge");
            }

            holder.texttv.setText(object.getString("text"));

        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }




    @Override
    public int getItemCount() {


        return array.length();

    }



    public class MyViewHolder extends RecyclerView.ViewHolder

    {


        TextView titletv,texttv;



        public MyViewHolder( View itemView) {

            super(itemView);


            titletv = itemView.findViewById(R.id.summarytitle);

            texttv = itemView.findViewById(R.id.summarytext);


        }

    }


}
