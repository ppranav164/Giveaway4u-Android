package com.shopping.giveaway4u;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link orders.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link orders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class orders extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    JSONArray Jsondata;

    private OnFragmentInteractionListener mListener;

    config_hosts hosts = new config_hosts();

    String orders_url = hosts.orders;

    Dialog dialog;

    public orders() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dialog = new Dialog(getContext());
        openDialog();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment orders.
     */
    // TODO: Rename and change types and number of parameters
    public static orders newInstance(String param1, String param2) {
        orders fragment = new orders();
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

        listdata();
        return inflater.inflate(R.layout.fragment_orders, container, false);
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





    public void openDialog() {

        dialog.setContentView(R.layout.dialog_demo);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.show();
    }



    public void closeDialog()
    {

        dialog.dismiss();
    }





    public void listdata()
    {

       new syncOrders(getContext(), new info() {
           @Override
           public void getInfo(String data) {

               setOrderstData(data);

               try {

                   JSONObject object = new JSONObject(data);

                   if (object.has("empty"))
                   {
                       FragmentManager manager = getFragmentManager();

                       manager.beginTransaction().replace(R.id.mainframeL,new ERROR("Your wishlist is empty")).commit();
                   }


               }catch (Exception e)
               {
                   e.printStackTrace();
               }

           }
       }).execute();

    }







    public void setOrderstData(String data)
    {

        try {

            JSONObject object = new JSONObject(data);

            JSONArray array = object.getJSONArray("orders");

            setJsonData(array);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public void setJsonData(JSONArray array)
    {
        this.Jsondata = array;

        View view = getView();

        RecyclerView recyclerView = view.findViewById(R.id.orders_recyclerview);

        recycleradapter_orders adapter = new recycleradapter_orders(getContext(),Jsondata);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        closeDialog();


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
