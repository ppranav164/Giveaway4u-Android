package com.shopping.gway_4u;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link account_returns#newInstance} factory method to
 * create an instance of this fragment.
 */
public class account_returns extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    config_hosts hosts = new config_hosts();

    String API_URL = hosts.productreturns;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public account_returns() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment account_returns.
     */
    // TODO: Rename and change types and number of parameters
    public static account_returns newInstance(String param1, String param2) {
        account_returns fragment = new account_returns();
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
        View view =  inflater.inflate(R.layout.fragment_account_returns, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        getDetails();
        return view;

    }



    public void getDetails()
    {

        new syncAsyncTask(getContext(), "GET", API_URL, null, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("returns",object);

                try {
                    JSONObject objects = new JSONObject(object);
                    if (!objects.getJSONArray("returns").equals("[]"))
                    {
                        JSONArray jsonArray = objects.getJSONArray("returns");
                        setJsonArrays(jsonArray);
                    }else {

                        Toast.makeText(getContext(),"Not records found",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).execute();

    }



    public void setJsonArrays(JSONArray arrays)
    {
        recycleradapter_returns adapter = new recycleradapter_returns(getContext(),arrays);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

    }



}