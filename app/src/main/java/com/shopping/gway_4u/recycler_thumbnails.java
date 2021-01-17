package com.shopping.gway_4u;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class recycler_thumbnails extends RecyclerView.Adapter<recycler_thumbnails.myviewholder> {


    Context mcontext;

    JSONArray images;

    Context customContext;

    PhotoView photoView;

    View transferView;






    public recycler_thumbnails(Context context, JSONArray imgs,View cvuew)
    {
        this.mcontext = context;
        this.images = imgs;

        this.transferView = cvuew;


    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumbnails,parent,false);

        final myviewholder vh = new myviewholder(view);


        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, int position) {


        final ArrayList<String> thumbs = new ArrayList<>();


        for (int i=0; i<images.length(); i++)

        {

          try {

              JSONObject object = images.getJSONObject(i);

              String url  = object.getString("popup");

              url = url.replace("localhost","10.0.2.2");

              thumbs.add(url);


          }catch (Exception e)
          {
              e.printStackTrace();
          }




        }


        photoView = transferView.findViewById(R.id.previewer);



        Picasso.get().load(thumbs.get(position)).into(holder.imageView);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = holder.getPosition();

                String url = thumbs.get(id);

               Picasso.get().load(url).into(photoView);

            }
        });



    }

    @Override
    public int getItemCount() {

        return images.length();
    }

    public class myviewholder extends RecyclerView.ViewHolder

    {

        ImageView imageView;



        public myviewholder(@NonNull View itemView) {

            super(itemView);

            imageView = itemView.findViewById(R.id.thumbImageview);


        }
    }


}
