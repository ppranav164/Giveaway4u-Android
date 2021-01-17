package com.shopping.gway_4u;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link editAccountInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class editAccountInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText firstName,lastName,eMail,mobileNumber;
    Button saveAcc;
    config_hosts hosts = new config_hosts();

    String url = hosts.accountDetails;
    private SharedPreferences sharedpref;
    String FIRSTNAME,LASTNAME,EMAIL,MOBILENO;
    String custumerId;
    boolean errors;
    String ERROR_FNAME,ERROR_LNAME,ERROR_EMAIL,ERROR_MOBILE;

    TextView errorTV;

    SharedPreferences.Editor editor;

    public editAccountInfo() {
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
    public static editAccountInfo newInstance(String param1, String param2) {
        editAccountInfo fragment = new editAccountInfo();
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

        View view = inflater.inflate(R.layout.fragment_edit_account_info, container, false);

        editor = getContext().getSharedPreferences("cookie",Context.MODE_PRIVATE).edit();

        errorTV = view.findViewById(R.id.errorView);

        firstName = view.findViewById(R.id.fname);
        lastName = view.findViewById(R.id.lname);
        eMail = view.findViewById(R.id.email);
        mobileNumber = view.findViewById(R.id.mobilenumber);
        saveAcc  = view.findViewById(R.id.saveAccount);
        sharedpref = getContext().getSharedPreferences("cookie",Context.MODE_PRIVATE);
        custumerId = sharedpref.getString("customer_id",null);

        getDetails();

        initializeButton();

        return view;
    }


    public void initializeButton()
    {


        saveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAcc.setEnabled(false);
                saveDetails();
            }
        });

    }


    public void getDetails()
    {

        new syncAsyncTask(getContext(), "GET", url, null, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("ob",object);

                try {
                    JSONObject object1 = new JSONObject(object);
                    FIRSTNAME = object1.getString("firstname");
                    LASTNAME = object1.getString("lastname");
                    EMAIL = object1.getString("email");
                    MOBILENO = object1.getString("telephone");

                    ERROR_FNAME = object1.getString("error_firstname");
                    ERROR_LNAME = object1.getString("error_lastname");
                    ERROR_EMAIL = object1.getString("error_email");
                    ERROR_MOBILE = object1.getString("error_telephone");

                    editor.putString("firstname",FIRSTNAME);
                    editor.putString("lastname",LASTNAME);
                    editor.putString("username",FIRSTNAME);
                    editor.putString("email",EMAIL);
                    editor.putString("mobile",MOBILENO);
                    editor.apply();

                    showDetails();

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).execute();
    }


    public void showDetails()
    {
        firstName.setText(FIRSTNAME);
        lastName.setText(LASTNAME);
        eMail.setText(EMAIL);
        mobileNumber.setText(MOBILENO);

    }


    public void saveDetails()
    {



        String getFname = firstName.getText().toString();
        String getLname = lastName.getText().toString();
        String getEmail = eMail.getText().toString();
        String getMobile = mobileNumber.getText().toString();

        String param = "firstname="+getFname+"&lastname="+getLname+"&email="+getEmail+"&telephone="+getMobile;

        Log.e("param",param);

        new syncAsyncTask(getContext(), "POST", url, param, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("feedback",object);
                try {
                    JSONObject object1 = new JSONObject(object);
                    ERROR_FNAME = object1.getString("error_firstname");
                    ERROR_LNAME = object1.getString("error_lastname");
                    ERROR_EMAIL = object1.getString("error_email");
                    ERROR_MOBILE = object1.getString("error_telephone");

                    String errorTEXT = object1.getString("error_warning");

                    errorTV.setText(errorTEXT);

                    if (ERROR_FNAME.length() > 1)
                    {
                        firstName.setError(ERROR_FNAME);
                        errors = true;
                    }

                    if (ERROR_LNAME.length() > 1)
                    {
                        lastName.setError(ERROR_LNAME);
                        errors = true;
                    }

                    if (ERROR_EMAIL.length() > 1)
                    {
                        eMail.setError(ERROR_EMAIL);
                        errors = true;
                    }

                    if (ERROR_MOBILE.length() > 1)
                    {
                        mobileNumber.setError(ERROR_MOBILE);
                        errors = true;
                    }

                    if (object1.getBoolean("success"))
                    {
                        errors = false;
                        getDetails();
                        Snackbar.make(getView(),"Successfully saved",Snackbar.LENGTH_SHORT).show();
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                saveAcc.setEnabled(true);
            }
        }).execute();


    }



}