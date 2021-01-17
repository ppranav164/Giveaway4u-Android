package com.shopping.gway_4u;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class slide_adapter extends PagerAdapter {

    Context context;

    ArrayList<String> urlist = new ArrayList<>();

    ArrayList<String> productId = new ArrayList<>();

    JSONArray array;


    public slide_adapter(Context context, JSONArray array) {

        this.context = context;

        this.array = array;

       try {

           for (int i=0; i < array.length(); i++)
           {
               JSONObject object = array.getJSONObject(i);

               String title = object.getString("title");

               String image = object.getString("image");

               String product_id = object.getString("product_id");

               productId.add(product_id);
               urlist.add(image);

           }

       }catch (Exception e)
       {
           e.printStackTrace();
       }

    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == ((ImageView) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final ImageView imageView = new ImageView(context);

        imageView.setElevation(2);

        imageView.setLayoutParams(params);

        imageView.setTag(productId.get(position));

        //Picasso.get().load(urlist.get(position)).into(imageView);

        Picasso.get().load(urlist.get(position)).transform(new RoundedCornersTransformation(5,5)).into(imageView);

        ((ViewPager) container).addView(imageView,0);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = v.getTag().toString();

                Bundle bundle = new Bundle();

                bundle.putString("_id",id);

                Fragment fragment = new products_fragment(context);

                fragment.setArguments(bundle);

                FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();

                FragmentTransaction transaction = manager.beginTransaction();

                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.mainframeL,fragment,"product");
                transaction.addToBackStack("main");

                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                transaction.commit();



            }
        });


        return imageView;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }

    @Override
    public int getCount() {

        return array.length();
    }
}
