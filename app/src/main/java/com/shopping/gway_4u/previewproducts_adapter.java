package com.shopping.gway_4u;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class previewproducts_adapter extends RecyclerView.Adapter <previewproducts_adapter.MyViewHolder>  {


    Context ctx;
    ArrayList<String> array;

    RecyclerViewClickListener listener;


    public previewproducts_adapter(Context context, ArrayList<String> data,RecyclerViewClickListener listener)
    {
        this.ctx = context;
        this.array = data;
        this.listener = listener;
    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.previewproducts,parent,false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {

      if (array.get(pos) != null)
      {
          Picasso.get().load(array.get(pos)).into( holder.imageView);
      }

    }

    @Override
    public int getItemCount() {

        return array.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {

        ImageView imageView;
        public MyViewHolder( View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageContainer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.recyclerViewListClicked(v,this.getLayoutPosition());
        }
    }


}
