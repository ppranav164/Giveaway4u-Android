package com.shopping.gway_4u;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;

public class track_adapter extends RecyclerView.Adapter <track_adapter.MyViewHolder>  {


    Context ctx;
    ArrayList<String> dateArray;
    ArrayList<String> messages;
    ArrayList<String> statuses;




    public track_adapter(Context context, ArrayList<String> dates,ArrayList<String> messages , ArrayList<String> statuses)
    {
        this.ctx = context;
        this.dateArray = dates;
        this.messages = messages;
        this.statuses = statuses;
    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track,parent,false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {

        holder.dateTv.setText(dateArray.get(pos));
        holder.messageTv.setText(messages.get(pos));
        holder.statusTv.setText(statuses.get(pos));


        if (statuses.get(pos).equals("Pending"))
        {
            holder.iconStatus.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_pending));

        }else if (statuses.get(pos).equals("Failed"))
        {
            holder.iconStatus.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_failed));
            holder.statusTv.setTextColor(ctx.getResources().getColor(R.color.red));

        }else if (statuses.get(pos).equals("Processed"))
        {
            holder.iconStatus.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_money));

        }else if (statuses.get(pos).equals("Refunded"))
        {
            holder.iconStatus.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_refunded));

        }else if (statuses.get(pos).equals("Canceled"))
        {
            holder.iconStatus.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_canceled));
            holder.statusTv.setTextColor(ctx.getResources().getColor(R.color.red));

        }else if (statuses.get(pos).equals("Complete"))
        {
            holder.iconStatus.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_done));

        }


        int totalrec = dateArray.size();

        int lastIndex = totalrec-1;

        Log.e("pos",String.valueOf(lastIndex) + " > " + dateArray.size() );

        if (pos == lastIndex)
        {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(51,0,0,0);
            holder.marker = new ImageView(ctx);
            holder.marker.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_round));
            holder.marker.setLayoutParams(params);
            holder.trackEnd.addView(holder.marker);
        }

    }

    @Override
    public int getItemCount() {


        return dateArray.size();

    }



    public class MyViewHolder extends RecyclerView.ViewHolder

    {


      public TimelineView timelineView;

      TextView dateTv,statusTv,messageTv;

      ImageView iconStatus,marker;

      LinearLayout trackEnd;

        public MyViewHolder( View itemView) {

            super(itemView);
            dateTv = itemView.findViewById(R.id.track_date);
            statusTv = itemView.findViewById(R.id.track_shipped);
            messageTv = itemView.findViewById(R.id.track_message);
            iconStatus = itemView.findViewById(R.id.track_icon);
            trackEnd = itemView.findViewById(R.id.track_end);


        }


    }


}
