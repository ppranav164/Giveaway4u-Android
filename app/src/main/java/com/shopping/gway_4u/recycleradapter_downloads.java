package com.shopping.gway_4u;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class recycleradapter_downloads extends RecyclerView.Adapter <recycleradapter_downloads.MyViewHolder>  {


    Context ctx;


    JSONArray array;

    ArrayList<String> ORDERID = new ArrayList<>();
    ArrayList<String> DATEOFORDER = new ArrayList<>();
    ArrayList<String> TITLE = new ArrayList<>();
    ArrayList<String> SIZE = new ArrayList<>();
    ArrayList<String> URL = new ArrayList<>();

    adapterClicklistener clicklistener;


    public recycleradapter_downloads(Context context, JSONArray mdata,adapterClicklistener clicklistener)
    {
       this.ctx = context;
       this.array = mdata;
       this.clicklistener = clicklistener;

       try {

           for (int i=0; i < mdata.length(); i++)
           {
               JSONObject nextObject = mdata.getJSONObject(i);
               ORDERID.add(nextObject.getString("order_id"));
               DATEOFORDER.add(nextObject.getString("date_added"));
               TITLE.add(nextObject.getString("name"));
               SIZE.add(nextObject.getString("size"));
               URL.add(nextObject.getString("href"));
           }

       }catch (Exception e)
       {
           e.printStackTrace();
       }
    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloads,parent,false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {

        holder.orderidtv.setText("Order ID :"+ ORDERID.get(pos));
        holder.datetv.setText(DATEOFORDER.get(pos));
        holder.titletv.setText(TITLE.get(pos));
        holder.sizetv.setText(SIZE.get(pos));
        holder.downloadbtn.setTag(URL.get(pos));
        holder.copylink.setTag(URL.get(pos));

        holder.downloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = v.getTag().toString();
                String title = TITLE.get(holder.getAdapterPosition());
                clicklistener.getPosition(holder.getAdapterPosition(),link,title);
            }
        });

        holder.copylink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = v.getTag().toString();
                CopyClipBoard(link);
            }
        });
    }


    @Override
    public int getItemCount() {


        return array.length();

    }


    public void CopyClipBoard(String link)
    {
        ClipboardManager clipboard = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Download Url",link);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(ctx,"Link Copied",Toast.LENGTH_SHORT).show();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder

    {
        TextView orderidtv,datetv,titletv,sizetv;
        Button downloadbtn,copylink;

        public MyViewHolder( View itemView) {

            super(itemView);

            orderidtv = itemView.findViewById(R.id.orderid);
            datetv = itemView.findViewById(R.id.datedoforder);
            titletv = itemView.findViewById(R.id.title);
            sizetv = itemView.findViewById(R.id.size);
            downloadbtn = itemView.findViewById(R.id.downloadBtn);
            copylink = itemView.findViewById(R.id.copylink);
        }

    }


}
