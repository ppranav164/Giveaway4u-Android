package com.shopping.gway_4u;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class recycleradapter_returns extends RecyclerView.Adapter <recycleradapter_returns.MyViewHolder>  {


    Context ctx;
    String images;
    JSONArray array;
    config_hosts hosts = new config_hosts();

    ArrayList<String> return_id = new ArrayList<>();
    ArrayList<String> order_id = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> status = new ArrayList<>();
    ArrayList<String> date_added = new ArrayList<>();

    public recycleradapter_returns(Context context, JSONArray data)
    {
        this.ctx = context;
        this.array = data;

        try {

            for (int i=0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                return_id.add(object.getString("return_id"));
                order_id.add(object.getString("order_id"));
                name.add(object.getString("name"));
                status.add(object.getString("status"));
                date_added.add(object.getString("date_added"));
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.returns,parent,false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {

        holder.returnidtv.setText("Return ID :"+" #"+return_id.get(pos));
        holder.orderidtv.setText("Order ID :"+ " #"+order_id.get(pos));
        holder.datetv.setText(date_added.get(pos));
        holder.statustv.setText(status.get(pos));
        holder.statustv.setTextColor(ctx.getResources().getColor(R.color.black));

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx,""+return_id.get(holder.getAdapterPosition()),Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    public int getItemCount() {


        return array.length();


    }



    public class MyViewHolder extends RecyclerView.ViewHolder

    {

        TextView statustv,datetv,orderidtv,returnidtv;
        LinearLayout linearLayout;

        public MyViewHolder( View itemView) {

            super(itemView);

            statustv = itemView.findViewById(R.id.status);
            datetv = itemView.findViewById(R.id.datedofreturn);
            orderidtv = itemView.findViewById(R.id.orderid);
            returnidtv = itemView.findViewById(R.id.returnid);
            linearLayout = itemView.findViewById(R.id.llot);

        }

    }


}
