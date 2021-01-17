package com.shopping.gway_4u;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link changePasswords#newInstance} factory method to
 * create an instance of this fragment.
 */
public class changePasswords extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText password,cpass;
    Button savePass;
    config_hosts hosts = new config_hosts();

    String url = hosts.CHANGE_PASSWORDS;

    private SharedPreferences sharedpref;
    String FIRSTNAME,LASTNAME,EMAIL,MOBILENO;
    String custumerId;
    boolean errors;
    String ERROR_PASS,ERROR_CONFIRM;
    TextView errorTV;

    SharedPreferences.Editor editor;

    View mainview;

    SharedPreferences.Editor fbToken;

    private ProgressDialog dialog;

    public changePasswords() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment editAccountInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static changePasswords newInstance(String param1, String param2) {
        changePasswords fragment = new changePasswords();
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

        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Please wait");
        dialog.setCancelable(false);

        editor = getContext().getSharedPreferences("cookie",Context.MODE_PRIVATE).edit();

        fbToken = getActivity().getSharedPreferences("firebase",Context.MODE_PRIVATE).edit();

        sharedpref = getContext().getSharedPreferences("cookie",Context.MODE_PRIVATE);
        custumerId = sharedpref.getString("customer_id",null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.change_password, container, false);

        mainview = view;
        password = view.findViewById(R.id.newpass);
        cpass  = view.findViewById(R.id.newpassconfirm);
        savePass = view.findViewById(R.id.changepasswordbtn);
        savePass.setOnClickListener(this);
        return view;

    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.changepasswordbtn : changePassword();
            break;
        }
    }


    public void changePassword()
    {
        cleartError();
        dialog.show();
        String PASSWORD = password.getText().toString();
        String CPASSWORD = cpass.getText().toString();
        Log.e("changePasswords","Change password "+PASSWORD);

        changePasswordtoServer(PASSWORD,CPASSWORD);
    }


    public void changePasswordtoServer(String password,String confirm)
    {
        String URL = hosts.CHANGE_PASSWORDS;
        String PARAM = "password="+password;
        PARAM += "&confirm="+confirm;

        Log.e("changePasswordtoServer",PARAM);

        new syncAsyncTask(getContext(), "POST", URL, PARAM, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("changePasswordtoServer",object);

                try {
                    JSONObject jsonObject = new JSONObject(object);


                    if (jsonObject.length() > 0)
                    {
                        dialog.dismiss();
                    }

                    if (jsonObject.has("error_password"))
                    {
                        setError(jsonObject.getString("error_password"),jsonObject.getString("error_confirm"));
                    }

                    if (jsonObject.has("abort"))
                    {
                        if (jsonObject.getBoolean("abort"))
                        {
                            invalidateLogin();
                        }
                    }

                    if (jsonObject.has("success")){

                        if (jsonObject.getBoolean("success"))
                        {
                            if (jsonObject.has("message"))
                            {
                                String message = jsonObject.getString("message");
                                Snackbar.make(mainview,message,Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).execute();

    }



    public void invalidateLogin()
    {
        clearFirebaseInstancecId();
        deleteInstanceIdFromServer();
        Intent intent = new Intent(getContext(),Activity_login.class);
        startActivity(intent);
        getActivity().finish();
    }



    public void clearFirebaseInstancecId()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }




    public void deleteInstanceIdFromServer()
    {
        String url = hosts.REMOVE_FIREBASE;

        SharedPreferences tokensSelector = getActivity().getSharedPreferences("firebase",Context.MODE_PRIVATE);

        String token = tokensSelector.getString("fbstring",null);

        String param = "token="+token;

        Log.e("ctoken",token);

        new syncAsyncTask(getContext(), "POST", url, param, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("removeToken",object);

                fbToken.clear();
                fbToken.putBoolean("isUpdated",false);
                fbToken.apply();

                SharedPreferences.Editor preferences = getActivity().getSharedPreferences("cookie",Context.MODE_PRIVATE).edit();
                SharedPreferences.Editor addrepref =    getActivity().getSharedPreferences("addresses",Context.MODE_PRIVATE).edit();

                preferences.putString("token","null");
                preferences.putBoolean("logged_in",false);
                preferences.apply();

                addrepref.putString("address_id",null);
                addrepref.apply();

            }
        }).execute();



    }


    public void setError(String passworderr,String cpassworderr)
    {
        if (!passworderr.isEmpty())
        {
            password.setError(passworderr);
        }

        if (!cpassworderr.isEmpty())
        {
            cpass.setError(cpassworderr);
        }
    }



    public void cleartError()
    {
        password.setError(null);
        cpass.setError(null);

    }


}