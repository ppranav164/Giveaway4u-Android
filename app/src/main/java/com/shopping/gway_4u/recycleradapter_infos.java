package com.shopping.gway_4u;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class recycleradapter_infos extends RecyclerView.Adapter <recycleradapter_infos.MyViewHolder>  {


    Context ctx;

    JSONArray array;



    ArrayList<String> product_name = new ArrayList<>();

    ArrayList<String> model_no = new ArrayList<>();

    ArrayList<String> price_tag = new ArrayList<>();

    ArrayList<String> prod_id = new ArrayList<>();

    ArrayList<String> thumbnails = new ArrayList<>();

    ArrayList<String> quantitys = new ArrayList<>();

    ArrayList<String> options = new ArrayList<>();

    ArrayList<String> uploadedImages = new ArrayList<>();
    ArrayList<String> preloadtitle = new ArrayList<>();

    ArrayList<String> uploads = new ArrayList<>();
    ArrayList<String> fileTitlt = new ArrayList<>();

    ArrayList<String> datas  = new ArrayList<>();


    ImageButton[] files = new ImageButton[5];


    TextView[] textViews = new TextView[5];
    LinearLayout[] optionslayout = new LinearLayout[5];

    SharedPreferences.Editor editor;

    public recycleradapter_infos(Context context, JSONArray mdata)
    {
        this.ctx = context;

        this.array = mdata;

       try {

          for (int i=0; i< array.length(); i++)
          {
              JSONObject object = array.getJSONObject(i);
              String name = object.getString("name");
              String model = object.getString("model");
              String price = object.getString("price");
              String quantity = object.getString("quantity");


              product_name.add(name);
              model_no.add(model);
              price_tag.add(price);
              quantitys.add(quantity);

              JSONArray imagesArray = object.getJSONArray("images");

              for (int k=0; k < imagesArray.length(); k++)
              {
                 JSONObject imgObject = imagesArray.getJSONObject(k);
                 String thumbs = imgObject.getString("thumb");

                 thumbnails.add(thumbs);

              }

              JSONArray optionsArray = object.getJSONArray("option");

              ArrayList<String> data = new ArrayList<>();
              ArrayList<String> preview = new ArrayList<>();
              ArrayList<String> title = new ArrayList<>();


              for (int j=0; j < optionsArray.length(); j++)
              {
                  JSONObject optionObject = optionsArray.getJSONObject(j);
                  String names = optionObject.getString("name");
                  String values = optionObject.getString("value");
                  String optionsvalue = names+": "+values;
                      data.add(optionsvalue);
                      options = data;

                         if (optionObject.has("preview")) {
                             preview.add(optionObject.getString("preview"));
                             title.add(optionObject.getString("name"));
                             uploadedImages = preview;
                             preloadtitle = title;
                         }
              }

              datas.add(options.toString());
              uploads.add(uploadedImages.toString());
              fileTitlt.add(preloadtitle.toString());
          }

       }catch (Exception e)
       {
           e.printStackTrace();
       }


    }





    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.infos,parent,false);

        MyViewHolder vh = new MyViewHolder(view);

        editor = ctx.getSharedPreferences("code",Context.MODE_PRIVATE).edit();

        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder,  int pos) {


        holder.name.setText(product_name.get(pos));
        holder.modelTv.setText(model_no.get(pos));
        holder.priceTv.setText(price_tag.get(pos));
        Picasso.get().load(thumbnails.get(pos)).into(holder.imageView);


        String newString = datas.get(pos).replace("[: ]","");

        String nextLine = newString.replace(",",System.lineSeparator());

        String optionsdata = nextLine.replaceAll("\\[", " ").replaceAll("\\]","");

        holder.textView.setText(optionsdata);

        Log.e("options",datas.get(pos));



        for (int i=0; i < 1; i++)
        {
           if (uploads.get(pos).length() > 6)
           {
               holder.filehead.setVisibility(View.VISIBLE);
               files[i] = new ImageButton(ctx);
               files[i].setBackgroundResource(R.drawable.viewfile);
               files[i].setTag(uploads.get(pos));
               holder.optionsLayout.addView(files[i]);

               files[i].setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       String title = fileTitlt.get(holder.getAdapterPosition());

                       Toast.makeText(ctx,title,Toast.LENGTH_SHORT).show();

                       String[] splits = new String[]{v.getTag().toString()};
                       Intent intent = new Intent(ctx,gallery.class);
                       intent.putExtra("code",splits);
                       intent.putExtra("titles",title);
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       ctx.startActivity(intent);
                   }
               });
           }
        }



    }






    @Override
    public int getItemCount() {


        return array.length();

    }



    public class MyViewHolder extends RecyclerView.ViewHolder

    {


        TextView name,modelTv,stockTv,priceTv;

        ImageView imageView,cart,remove;

        TextView textView; LinearLayout linearLayout,optionsLayout,filehead;

        ImageButton viewfiles;




        public MyViewHolder( View itemView) {

            super(itemView);


            name = itemView.findViewById(R.id.name);

            imageView = itemView.findViewById(R.id.thumbs);

            modelTv = itemView.findViewById(R.id.models);

            stockTv = itemView.findViewById(R.id.status);

            priceTv = itemView.findViewById(R.id.prices);

            cart = itemView.findViewById(R.id.cart);

            remove = itemView.findViewById(R.id.remove);

            textView = itemView.findViewById(R.id.optionsmenu);

            linearLayout = itemView.findViewById(R.id.optionsmenus);

            optionsLayout = itemView.findViewById(R.id.optionsfile);

            filehead = itemView.findViewById(R.id.fileheader);


        }

    }


}
