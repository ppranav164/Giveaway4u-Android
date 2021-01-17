package com.shopping.gway_4u;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link cartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link cartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cartFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    JSONArray Jsondata,Totals;

    ListView listView;

    View view;

    EditText entCoupon;

    Button appCoupon;


    LinearLayout linearLayout;


    ArrayList<list_totals> totaldata = new ArrayList<>();

    ArrayList<String> text = new ArrayList<>();

    String message;

    TextView alertbox;

     View alertview;

     ImageView iconview;


    View menutabs;

    Boolean updateCart;

    Button cartButton;

    TextView cartcount;

    Dialog dialog;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public cartFragment() {
        // Required empty public constructor
    }



    public cartFragment(Boolean updates)
    {
        this.updateCart = updates;
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WishlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static cartFragment newInstance(String param1, String param2) {
        cartFragment fragment = new cartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        alertview = getView();


        dialog = new Dialog(getContext());

        openDialog();

        View cartbuttonViews = getView();

        addtoCart(cartbuttonViews);

        setCartCounts();

        cartcount = alertview.findViewById(R.id.cartCounts);


        if (updateCart != null)
        {

            if (updateCart == true)
            {
                setHasOptionsMenu(true);
            }

        }

        syncCartData();


    }



    public  void syncCartData()
    {
        new sync_get_cart(getContext(), new cart() {
            @Override
            public void loadCarts(String data) {

                setCartData(data);

                try {

                    JSONObject object = new JSONObject(data);

                    Log.e("syncCartData",object.toString());

                    if (object.length() > 0)
                    {
                        closeDialog();
                    }

                    if (object.has("isAvailable"))
                    {
                        if (!object.getBoolean("isAvailable"))
                        {
                            disableCheckout();
                        }else {
                            allowCheckout();
                        }
                    }

                    if (object.has("text_error"))
                    {

                        FragmentManager manager = getFragmentManager();

                        FragmentTransaction transaction = manager.beginTransaction();

                        transaction.replace(R.id.mainframeL,new ERROR("Your cart is empty !")).commit();

                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).execute();
    }



    @SuppressLint("ResourceAsColor")

    public void disableCheckout()
    {
        cartButton.setEnabled(false);
        cartButton.setBackground(getResources().getDrawable(R.drawable.grey_button));
    }


    @SuppressLint("ResourceAsColor")
    public void allowCheckout()
    {
        cartButton.setEnabled(true);
        cartButton.setBackgroundColor(R.color.white);
    }



    public void setCartData(String data)
    {

        try {


            JSONObject object = new JSONObject(data);

            JSONArray array = object.getJSONArray("products");

            JSONArray totals = object.getJSONArray("totals");

            setJsonData(array,totals);


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public void setJsonData(JSONArray array, JSONArray totals)
    {
        this.Jsondata = array;
        this.Totals = totals;
        View view = getView();
        RecyclerView recyclerView = view.findViewById(R.id.cart_recyclerview);
        recycleradapter_cart adapter = new recycleradapter_cart(getContext(),Jsondata,Totals);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.smoothScrollToPosition(0);
        recyclerView.setFocusable(true);
        recyclerView.setNestedScrollingEnabled(false);
        adapter.notifyDataSetChanged();
        closeDialog();
        try {

          for (int i=0; i < Totals.length(); i++)
          {
              JSONObject object = Totals.getJSONObject(i);

              String titles = object.getString("title");
              String texts = object.getString("text");

              totaldata.add(new list_totals(titles,texts));

          }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        CustomAdapter ladapter = new CustomAdapter(getContext(),R.layout.listviews,totaldata);
        listView = view.findViewById(R.id.listviews);
        listView.setAdapter(ladapter);
        ladapter.notifyDataSetChanged();
        listView.invalidateViews();
    }


    public void openDialog() {

        dialog.setContentView(R.layout.dialog_demo);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.show();
    }



    public void closeDialog()
    {

        dialog.dismiss();
    }



    public void couponButton()
    {

        view = getView();



        appCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                String code = "couponcode";


                new syncCoupon(getContext(), code, new message() {
                    @Override
                    public void setMessage(String message) {

                       setMessages(message);

                    }
                }).execute();

            }
        });

    }


    public void setMessages(String data)

    {


        //alertbox =  alertview.findViewById(R.id.messagealert);

        //iconview = alertview.findViewById(R.id.iconAlert);

        alertbox.setText("Applying Coupon ...");


        try {

            JSONObject mess = new JSONObject(data);


            if (mess.has("redirect"))
            {


                iconview.setImageResource(R.drawable.ic_done);

                alertbox.setText("Your coupon discount has been applied!");
                alertbox.setTextColor(Color.parseColor("#00e348"));

            }

            if (mess.has("error"))
            {

                String error = mess.getString("error");
                iconview.setImageResource(R.drawable.ic_error_black_24dp);
                alertbox.setText(error);
                alertbox.setTextColor(Color.parseColor("#eb0000"));
            }



        }catch (Exception e)
        {

        }

        new sync_get_cart(getContext(), new cart() {
            @Override
            public void loadCarts(String data) {

                CustomAdapter ladapter = new CustomAdapter(getContext(),R.layout.listviews,totaldata);

                ladapter.clear();

                UpdateTotal(data);


            }
        }).execute();



    }



    public void UpdateTotal(String data) {


        // coupon  2767017897

        view = getView();

        try {

            JSONObject object = new JSONObject(data);

            JSONArray array = object.getJSONArray("totals");

            for (int k=0; k < data.length(); k++)
            {
                JSONObject totals = array.getJSONObject(k);

                String title = totals.getString("title");

                String text = totals.getString("text");

                totaldata.add(new list_totals(title,text));

            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        CustomAdapter ladapter = new CustomAdapter(getContext(),R.layout.listviews,totaldata);


        listView = view.findViewById(R.id.listviews);

        listView.setAdapter(ladapter);


    }



    public  void  addtoCart(View view)
    {

        cartButton = view.findViewById(R.id.addToCart);

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAddressScreen();

            }
        });


    }

    public void showAddressScreen()
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.mainframeL,new editAddress(),"editAddress");
        transaction.addToBackStack("checkout");
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    public void checkOutScreen()
    {
        Fragment checkOut = new checkOut();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.mainframeL,checkOut,"checkout");
        transaction.addToBackStack("checkout");
        transaction.commit();
    }








    public void setCartCounts()
    {

        new syncInfo(getContext(), new info() {
            @Override
            public void getInfo(String data) {

                try {

                    JSONObject jsonObject = new JSONObject(data);

                    String items = jsonObject.getString("text_items");



                    if (items.matches("0"))
                    {
                        cartcount.setText("0");

                    }else {

                        cartcount.setText("Cart ("+items+")");
                    }


                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).execute();
    }









    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        //super.onCreateOptionsMenu(menu, inflater);

        //getActivity().getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.cart);

        menutabs = MenuItemCompat.getActionView(menuItem);


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
