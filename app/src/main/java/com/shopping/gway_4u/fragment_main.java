package com.shopping.gway_4u;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_main extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

     private OnFragmentInteractionListener mListener;

     private  loadMainData mainData;

     private static int[] Extimages;
     private Handler handler;
     private Runnable runnable;
     private boolean connected = false;

     config_hosts hosts  = new config_hosts();
    ViewPager pager;
    SliderView sliderView;
    JSONArray sliderstotal;
    RecyclerView recyclerView;
    RecyclerView specialRecycler;
    ArrayList<String> wishlist_products = new ArrayList<>();
    LinearLayout featuredHeader;
    LinearLayout specialLayout;
    LinearLayout latestHeader;
    RecyclerView categoryview;
    JSONArray cats;

    Button btn_featured,btn_latest,btn_special;

    public fragment_main() {
        // Required empty public constructor
    }


    public fragment_main(int[] pics) {


        this.Extimages = pics;


    }




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_main.
     */
    // TODO: Rename and change types and number of parameters


    public static fragment_main newInstance(String param1, String param2) {
        fragment_main fragment = new fragment_main();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        checkInternet();

        loadData();


    }



    public void checkInternet()
    {
        new CHECK_CONNECTION(getContext(), "GET", null, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                if (object.equals("error"))
                {
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.mainframeL,new CONNECTION_FAILED());
                    transaction.addToBackStack(null);
                    transaction.commit();
                    isConnected(false);
                    connected = false;
                }
            }
        }).execute();
    }





    public void checkCoonection()
    {

        new syncInfo(getContext(), new info() {
            @Override
            public void getInfo(String data) {




            }
        }).execute();

    }


    public boolean isConnected(boolean isCon)
    {
        return isCon;
    }



    public void loadData()
    {

        category();

        getWishlistData();

        new syncFeatured(getContext(), new featured() {
            @Override
            public void loadFeatured(String data) {

               getFeturedData(data);

            }
        }).execute();


        new syncLatest(getContext(), new latest() {
            @Override
            public void loadLatest(String data) {
                getLatestData(data);
                Log.e("fragmentmain syncLatest",data);
            }
        }).execute();

        new syncSlideshow(getContext(), new slideshow() {
            @Override
            public void loadSliders(String data) {
                getSlideShowImage(data);
            }
        }).execute();


        new syncAsyncTask(getContext(), "GET", hosts.SPECIAL, null, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                getSpecialProduct(object);
            }
        }).execute();

    }




    public void getWishlistData()
    {
        new sync_get_wishlist(getContext(), new wishlist() {

            @Override
            public void loadWishlist(String data) {
                try {

                    JSONObject object = new JSONObject(data);

                    JSONArray products = object.getJSONArray("products");

                    setWishlistData(products);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }).execute();

    }



    public void setWishlistData(JSONArray products)
    {

        try {

            for (int i=0; i < products.length(); i++)
            {
                JSONObject id = products.getJSONObject(i);

                String product_id = id.getString("product_id");

                wishlist_products.add(product_id);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }




    public void getSlideShowImage(String info)
    {

        try {

            JSONObject object = new JSONObject(info);

            JSONArray array = object.getJSONArray("banners");

            setSlideshowImage(array);

            //Toast.makeText(getContext(),""+array,Toast.LENGTH_LONG).show();


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public void setSlideshowImage(JSONArray array)
    {

        View view = getView();

        sliderstotal = array;

        if (view !=null)
        {

            sliderView = view.findViewById(R.id.imageSlider);
            sliderView.setSliderAdapter(new SliderAdapterExample(getContext(),array));
            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            sliderView.setIndicatorSelectedColor(Color.WHITE);
            sliderView.setIndicatorUnselectedColor(Color.GRAY);
            sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
            sliderView.startAutoCycle();

        }


    }



    public void getLatestData(String info)
    {

        try {

            JSONObject object = new JSONObject(info);

            JSONArray array = object.getJSONArray("products");

            setLatestInfo(array);


            //Toast.makeText(getContext(),""+array,Toast.LENGTH_LONG).show();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }




    public void getFeturedData(String info)
    {
        int size = info.length();
        Log.e("featured",String.valueOf(size));
       if (size > 0)
       {
           featuredHeader.setVisibility(View.VISIBLE);
       }
        try {
            JSONObject object = new JSONObject(info);
            JSONArray array = object.getJSONArray("products");
            setFeaturedInfo(array);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }



    public void getSpecialProduct(String products)
    {
        try {

            JSONObject jsonObject = new JSONObject(products);

            if (jsonObject.has("products"))
            {
                JSONArray specials = jsonObject.getJSONArray("products");
                showSpecialProduct(specials);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void showSpecialProduct(JSONArray array)
    {
        if (array.length() > 0)
        {
            specialLayout.setVisibility(View.VISIBLE);
        }else {

            specialLayout.setVisibility(View.GONE);
        }

        specialRecycler.setLayoutManager(new GridLayoutManager(getContext(),2,LinearLayoutManager.VERTICAL,false));
        specialRecycler.smoothScrollToPosition(0);


        special_recycler_adapter recadapter = new special_recycler_adapter(getContext(),array,wishlist_products);
        specialRecycler.setAdapter(recadapter);
        specialRecycler.setFocusable(false);
        specialRecycler.setNestedScrollingEnabled(false);
        Log.e("showSpecialProduct",array.toString());


    }



    public void showMoreProductByCode(String code)
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainframeL,new more_product(code,getContext()),"more");
        transaction.addToBackStack("main");
        transaction.commit();
    }




    public void setLatestInfo(JSONArray array)

    {
        View view = getView();
        if (array.length() > 0)
        {
            latestHeader.setVisibility(View.VISIBLE);
        }

        recyclerView  =  view.findViewById(R.id.latest_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,LinearLayoutManager.VERTICAL,false));
        recyclerView.smoothScrollToPosition(0);


        featured_recycler_adapter recadapter = new featured_recycler_adapter(getContext(),array,wishlist_products);
        recyclerView.setAdapter(recadapter);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        Log.e("Latest",array.toString());

    }




    public void setFeaturedInfo(JSONArray array)

    {

        View view = getView();

        recyclerView  =  view.findViewById(R.id.vrecyclerview);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,LinearLayoutManager.VERTICAL,false));
        recyclerView.smoothScrollToPosition(0);
        recycleradapter recadapter = new recycleradapter(getContext(),array,wishlist_products);
        recyclerView.setAdapter(recadapter);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);


    }



    public JSONArray  category()
    {

        String Cat_link = hosts.GET_CATEGORY;

        new syncAsyncTask(getContext(), "GET", Cat_link, null, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                try {

                    JSONObject jsonObject = new JSONObject(object);
                    JSONArray array = jsonObject.getJSONArray("categories");
                    showCategory(array);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();

        return  cats;
    }


    public void showCategory(JSONArray array)
    {

        categoryview.setLayoutManager(new GridLayoutManager(getContext(),3,LinearLayoutManager.VERTICAL,false));
        categoryview.smoothScrollToPosition(0);
        categoriesAdapter categoriesAdapter = new categoriesAdapter(getContext(),array);
        categoryview.setAdapter(categoriesAdapter);
        categoryview.setFocusable(false);
        categoryview.setNestedScrollingEnabled(false);

    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_layout_main, container, false);

        featuredHeader = view.findViewById(R.id.headL);
        latestHeader = view.findViewById(R.id.latestText);
        specialLayout = view.findViewById(R.id.specialHead);
        categoryview = view.findViewById(R.id.categoryLayout);
        specialRecycler = view.findViewById(R.id.special_recyclerview);

        //BUTTONs

        btn_featured = view.findViewById(R.id.featured_more);
        btn_latest = view.findViewById(R.id.latest_more);
        btn_special = view.findViewById(R.id.special_more);

        //listener

        btn_featured.setOnClickListener(this);
        btn_special.setOnClickListener(this);
        btn_latest.setOnClickListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onClick(View v) {

        showMoreProductByCode(v.getTag().toString());
    }




    public boolean menuSearch(String tag)
    {
        startActivity(new Intent(getContext(),searchProduct.class).putExtra("tag",tag));
        return true;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
