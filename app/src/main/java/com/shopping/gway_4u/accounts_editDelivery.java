package com.shopping.gway_4u;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link accounts_editDelivery.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link accounts_editDelivery#newInstance} factory method to
 * create an instance of this fragment.
 */
public class accounts_editDelivery extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView idview;

    Button saveaddressbtn;

    config_hosts hosts = new config_hosts();

    String URL_GET_ADDRESS_SAVE = hosts.MY_ACCOUNT_ADDR_SAVE;
    String URL_ADDR_BY = hosts.MY_ACCOUNT_ADDR_BY_ID;

    ArrayList<String> countryNames = new ArrayList<>();
    ArrayList<String> countryIds = new ArrayList<>();

    ArrayList<String> stateNames = new ArrayList<>();
    ArrayList<String> stateIds = new ArrayList<>();

    EditText firstName,lastName,companYname,address1,address2,city,postCode;

    Spinner countrySpinner,regionsSpinner;

    Dialog dialog;

    String fname,lname,company,address_1,address_2,citys,postcodes,countryID,stateID,addressID;

    String ADDRESS_ID = "";
    boolean isEdit = false;

    boolean isCountryLoaded = false;
    boolean isStateLoaded = false;

    int Countrypos = 0;
    int statePos = 0;

    String country_id = "";
    String state_id = "";

    public accounts_editDelivery() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment editDelivery.
     */
    // TODO: Rename and change types and number of parameters
    public static accounts_editDelivery newInstance(String param1, String param2) {
        accounts_editDelivery fragment = new accounts_editDelivery();
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

        Bundle bundle = getArguments();

        if (bundle !=null) {

            ADDRESS_ID = bundle.getString("address_id");
            isEdit = bundle.getBoolean("isEdit");
        }

        Log.e(getString(R.string.which_actv),this.getClass().getSimpleName());


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dialog = new Dialog(getContext());
        return inflater.inflate(R.layout.fragment_edit_delivery, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();



        idview = view.findViewById(R.id.addressID);
        saveaddressbtn = view.findViewById(R.id.savenewAddress);
        countrySpinner = view.findViewById(R.id.countrySpinner);
        regionsSpinner = view.findViewById(R.id.regionsSpinner);


        firstName = view.findViewById(R.id.fname);
        lastName = view.findViewById(R.id.lname);
        companYname = view.findViewById(R.id.company);
        address1 = view.findViewById(R.id.address1);
        address2 = view.findViewById(R.id.address2);
        city = view.findViewById(R.id.city);
        postCode = view.findViewById(R.id.postCode);


        SharedPreferences addressgroup = getActivity().getSharedPreferences("cookie",Context.MODE_PRIVATE);

        String idee = addressgroup.getString("address_id",null);

        idview.setText(idee);

        addressID = idee;

        getCountry();
        getStates();

        saveAddress();

        if (isEdit)
        {
            getAddressById(ADDRESS_ID);
        }

        openDialog();

    }



    public void  getAddressById(String addressId)
    {

        String PARAM = "address_id="+addressId;

        Log.e("Address ID : ",addressId);
        openDialog();

        new syncAsyncTask(getContext(), "POST", URL_ADDR_BY, PARAM, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("ADDR BY ID",object);

                if (object.length() > 0)
                {
                    closeDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(object);
                        setClientAddress(jsonObject);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).execute();
    }




    public void setClientAddress(JSONObject jsonObject)
    {
        try {

            firstName.setText(jsonObject.getString("firstname"));
            lastName.setText(jsonObject.getString("lastname"));
            companYname.setText(jsonObject.getString("company"));
            address1.setText(jsonObject.getString("address_1"));
            address2.setText(jsonObject.getString("address_2"));
            postCode.setText(jsonObject.getString("postcode"));
            city.setText(jsonObject.getString("city"));

             country_id = jsonObject.getString("country_id");
             state_id = jsonObject.getString("zone_id");

            Log.e("Country ID ",country_id);
            Log.e("Country Name",jsonObject.getString("country"));

            Log.e("State ID",state_id);
            Log.e("State Name",jsonObject.getString("zone"));

            if (loadCountriesToSpinner())
            {
                countrySpinner.setSelection(getCustomerCountry());
            }



        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public int getCustomerCountry()
    {
        for (int i=0; i< countryIds.size(); i++)
        {
            if (countryIds.get(i).equals(country_id))
            {
                Countrypos = i;
                return i;
            }
        }

        return Countrypos;
    }


    public int getCustomerState()
    {
        for (int i=0; i< stateIds.size(); i++)
        {
            if (stateIds.get(i).equals(state_id))
            {
                statePos = i;
                return statePos;
            }
        }
        return statePos;
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





    public  void  getCountry()
    {

        String country = hosts.country;

        new syncCountry(getContext(),country, new info() {
            @Override
            public void getInfo(String data) {

                try {

                    JSONObject object = new JSONObject(data);

                    JSONArray array = object.getJSONArray("countries");

                    setCountryData(array);

                }catch (Exception e)
                {

                }

            }
        }).execute();
    }



    public void setCountryData(JSONArray jsonArray){

        try {

            for (int i=0; i < jsonArray.length(); i++)
            {
                JSONObject object = jsonArray.getJSONObject(i);
                String countryname = object.getString("name");
                String countryId = object.getString("country_id");
                getCountries(countryname,countryId);
            }

        }catch (Exception e)
        {

            e.printStackTrace();
        }

    }


    public void getCountries(String countryName,String countryid)
    {

        countryNames.add(countryName);
        countryIds.add(countryid);

        loadCountriesToSpinner();


    }




    public boolean loadCountriesToSpinner()
    {



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,countryNames);
        countrySpinner.setAdapter(adapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String countryId = countryIds.get(position);
                getStateByCountry(countryId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return true;

    }




    public boolean loadStatestoSpinner()
    {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,stateNames);

        regionsSpinner.setAdapter(arrayAdapter);

        regionsSpinner.setSelection(getCustomerState());

        regionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String stateId = stateIds.get(position);

                Log.e("accounts_edit_stateid",stateId);

                closeDialog();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return true;
    }



    public void getStates(){


        String url = hosts.states+"99";

        stateNames.clear();
        stateIds.clear();

        new syncCountry(getContext(), url, new info() {
            @Override
            public void getInfo(String data) {
                try {
                    JSONObject object = new JSONObject(data);
                    JSONArray array = object.getJSONArray("zone");
                    SetStateData(array);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }



    public void getStateByCountry(String countryId){

        String url = hosts.states+countryId;

        stateNames.clear();
        stateIds.clear();

        new syncCountry(getContext(), url, new info() {
            @Override
            public void getInfo(String data) {
                try {
                    JSONObject object = new JSONObject(data);
                    JSONArray array = object.getJSONArray("zone");
                    SetStateData(array);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }


    public void SetStateData(JSONArray array)
    {
        //use : zone (array)

        try {

            for (int i=0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                String Statenames = object.getString("name");
                String zoneId = object.getString("zone_id");
                listStates(Statenames,zoneId);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public void listStates(String statename,String zoneId)
    {

        stateNames.add(statename);
        stateIds.add(zoneId);
        loadStatestoSpinner();
    }



    public void saveAddress()
    {
        if (isEdit)
        {
            saveaddressbtn.setText("Update");
        }else {

            saveaddressbtn.setText("Save");
        }

        saveaddressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
                saveNewAddress();
            }
        });

    }


    public void saveNewAddress()
    {
        int coutrySelection = countrySpinner.getSelectedItemPosition();

        int statePos = regionsSpinner.getSelectedItemPosition();

        fname = firstName.getText().toString();
        lname = lastName.getText().toString();
        company = companYname.getText().toString();
        address_1 = address1.getText().toString();
        address_2 = address2.getText().toString();
        citys = city.getText().toString();
        postcodes = postCode.getText().toString();
        countryID = countryIds.get(coutrySelection);
        stateID = stateIds.get(statePos);


        if (formVerify() != false)
        {

            String param = "address_id="+addressID+"&";
            param += "payment_address=new&";
            param += "firstname="+fname+"&";
            param += "lastname="+lname+"&";
            param += "company="+company+"&";
            param += "address_1="+address_1+"&";
            param += "address_2="+address_2+"&";
            param += "city="+citys+"&";
            param += "postcode="+postcodes+"&";
            param += "country_id="+countryID+"&";
            param += "zone_id="+stateID;

            if (isEdit == true)
            {
                param += "&existing_address_id="+ADDRESS_ID;
            }

            Log.e("params",param);

            sendToserver(param);

        }


    }



    public void sendToserver(String PARAMS)
    {


        new syncAsyncTask(getContext(), "POST",URL_GET_ADDRESS_SAVE, PARAMS, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                if (object.length() > 0)
                {
                    closeDialog();
                    success();
                }
            }
        }).execute();
    }



    public void alert(String message,int duration)
    {
        Toast.makeText(getContext(),message,duration).show();
    }


    public void success()
    {
      Intent intent = new Intent(getContext(),MyAccount.class);
      intent.putExtra("updated",true);
      startActivity(intent);
    }


    public boolean formVerify()
    {

        if (TextUtils.isEmpty(firstName.getText().toString()))
        {
            firstName.setError("First Name can't be empty");
            closeDialog();
            return false;
        }

        if (TextUtils.isEmpty(lastName.getText().toString()))
        {
            lastName.setError("Last Name can't be empty");
            closeDialog();
            return false;
        }

        if (TextUtils.isEmpty(address1.getText().toString()))
        {
            address1.setError("This address field required");
            closeDialog();
            return false;
        }


        if (TextUtils.isEmpty(city.getText().toString()))
        {
            city.setError("Please enter city name");
            closeDialog();
            return false;
        }

        if (TextUtils.isEmpty(postCode.getText().toString()))
        {
            postCode.setError("Please enter postcode ");
            closeDialog();
            return false;
        }

        return true;

    }



    public void openDialog() {

        dialog.setContentView(R.layout.dialog_demo);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.setCancelable(false);
        dialog.show();
    }


    public void closeDialog()
    {
        dialog.dismiss();
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
