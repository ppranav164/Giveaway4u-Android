package com.shopping.gway_4u;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link editAddress#newInstance} factory method to
 * create an instance of this fragment.
 */
public class editAddress extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout linearLayout;

    LinearLayout deliveryGroup;

    TextView[] textViews = new TextView[5];
    RelativeLayout[] layouts = new RelativeLayout[5];
    ImageButton[] delbutton = new ImageButton[5];
    TextView[] infotext = new TextView[5];
    SharedPreferences.Editor editor ;

    config_hosts hosts = new config_hosts();

    String URL_DELETE_ADDRESS = hosts.DeletepaymentAddress;

    String WARNING = "";

    String TAG = "editAddress.java";


    String getAddress = "";

    Button saveBtn;



    Dialog dialog;



    Button addnewbtn;

    public editAddress() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment editAddress.
     */
    // TODO: Rename and change types and number of parameters
    public static editAddress newInstance(String param1, String param2) {
        editAddress fragment = new editAddress();
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


        Log.e(getString(R.string.which_actv),this.getClass().getSimpleName());

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_edit_address, container, false);


        dialog = new Dialog(getContext());

        editor = getContext().getSharedPreferences("addresses",Context.MODE_PRIVATE).edit();

        deliveryGroup = view.findViewById(R.id.deliveryGroup);
        linearLayout = view.findViewById(R.id.showaddress);

        saveBtn = view.findViewById(R.id.save);

        addnewbtn = view.findViewById(R.id.addnew);

        openDialog();

        getPaymentAddress();

        saveAddress();

        addNewAddress();

        return view;


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




    public void getPaymentAddress()
    {

        new syncPayment(getContext(), new info() {
            @Override
            public void getInfo(String data) {

                if (data.length() > 0)
                {
                    closeDialog();
                }

                try {

                    JSONObject object = new JSONObject(data);

                   if (object.getString("addresses").equals("[]"))
                   {
                       closeDialog();
                       saveBtn.setEnabled(false);
                       saveBtn.setAlpha(0.5f);
                   }else
                   {
                       saveBtn.setEnabled(true);
                       saveBtn.setAlpha(1);
                       JSONObject addresses = object.getJSONObject("addresses");
                       setAddress(addresses);
                       closeDialog();
                   }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).execute();

    }


    public void setAddress(JSONObject objects)
    {

        JSONObject fields = null;

        SharedPreferences preferences = getActivity().getSharedPreferences("cookie", Context.MODE_PRIVATE);

        SharedPreferences getaddress = getActivity().getSharedPreferences("addresses",Context.MODE_PRIVATE);


        String defaddress = preferences.getString("address_id",null);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,30,0,0);

        RelativeLayout.LayoutParams infoparam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams delparam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        delparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        delparam.setMargins(0,50,0,0);

        getAddress = defaddress;

        try {


            Iterator iterator = objects.keys();

            int index = 0;

            while (iterator.hasNext())
            {

                index++;

                String val = iterator.next().toString();

                fields = objects.getJSONObject(val);

                String address_id = fields.getString("address_id");

                String fname = fields.getString("firstname");

                String lname = fields.getString("lastname");

                String address1 = fields.getString("address_1");

                String city = fields.getString("city");

                String zone = fields.getString("zone");

                String country = fields.getString("country");

                String postcode = fields.getString("postcode");

                String fulladderess =  fname +" "+ lname + System.lineSeparator() + address1 + System.lineSeparator() + city + System.lineSeparator() + zone + System.lineSeparator() + country +System.lineSeparator() + "PIN : " + postcode;




                layouts[index] = new RelativeLayout(getContext());
                layouts[index].setBackgroundResource(R.drawable.border);
                layouts[index].setLayoutParams(layoutParams);
                layouts[index].setTag(address_id);
                layouts[index].setClickable(true);
                linearLayout.addView(layouts[index]);


                delbutton[index] = new ImageButton(getContext());
                delbutton[index].setTag(address_id);
                delbutton[index].setImageResource(R.drawable.ic_delete);
                delbutton[index].setVisibility(View.INVISIBLE);
                delbutton[index].setBackgroundColor(getResources().getColor(R.color.white));
                delbutton[index].setLayoutParams(delparam);
                layouts[index].addView(delbutton[index]);

                delbutton[index].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteAddress(v);
                    }
                });



                if (getaddress.getString("address_id",defaddress).equals(address_id))
                {
                    layouts[index].setBackgroundResource(R.drawable.border_pressed);
                    layouts[index].setSelected(true);

                    infotext[index] = new TextView(getContext());
                    infotext[index].setLayoutParams(infoparam);
                    infotext[index].setPadding(10,10,10,10);
                    infotext[index].setTextColor(getResources().getColor(R.color.grey));
                    infotext[index].setText("Selected");
                    infotext[index].setGravity(Gravity.RIGHT);
                    layouts[index].addView(infotext[index]);
                    getAddress = layouts[index].getTag().toString();

                }


                textViews[index] = new TextView(getContext());
                textViews[index].setText(fulladderess);
                textViews[index].setPadding(20,20,20,20);
                layouts[index].addView(textViews[index]);


                layouts[index].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                      int childs = linearLayout.getChildCount();

                      String tag = v.getTag().toString();

                      for (int i=0; i < childs; i++)
                        {
                            View view = linearLayout.getChildAt(i);

                            view.setSelected(false);
                            view.setBackgroundResource(R.drawable.border);
                        }


                      if (v.isSelected() != true)
                      {
                          v.setBackgroundResource(R.drawable.border_pressed);
                          v.setSelected(true);
                          getAddress = tag;
                      }else
                      {
                          v.setBackgroundResource(R.drawable.border);
                          v.setSelected(false);
                      }

                    }
                });


            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }



    public void deleteAddress(final View view)
    {
       String deleteWhereId = view.getTag().toString();

       Log.e("deleteWhereId",deleteWhereId);

       String param = "address_id="+deleteWhereId;

       new syncAsyncTask(getContext(), "POST", URL_DELETE_ADDRESS, param, new jsonObjects() {
           @Override
           public void getObjects(String object) {
               Log.e(TAG,object);
               try {

                   JSONObject jsonObject = new JSONObject(object);
                   if (jsonObject.has("warning"))
                   {
                       WARNING = jsonObject.getString("warning");
                       Snackbar.make(view,WARNING,Snackbar.LENGTH_SHORT).show();
                   }else {

                       linearLayout.removeAllViews();
                       getPaymentAddress();
                   }

               }catch (Exception e)
               {
                   e.printStackTrace();
               }
           }
       }).execute();



    }


    public void addNewAddress()
    {
        addnewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             FragmentManager manager = getFragmentManager();
             FragmentTransaction transaction = manager.beginTransaction();
             transaction.addToBackStack("editAddress");
             transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
             transaction.replace(R.id.mainframeL,new editDelivery(),"editDelivery");
             transaction.commit();

            }
        });
    }



    public void saveAddress()
    {

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog();

                final String existing = "payment_address=existing&address_id="+getAddress;

                editor.putString("address_id",getAddress);
                editor.putString("default_address",getAddress);
                editor.apply();

                new syncSavePayment(getContext(), existing, new info() {
                    @Override
                    public void getInfo(String data) {

                        closeDialog();
                        Log.e("editAddress Address",data);

                        try {

                            JSONObject jsonObject = new JSONObject(data);
                            if (jsonObject.has("isSaved"))
                            {
                                if (jsonObject.getBoolean("isSaved"))
                                {
                                    checkOutScreen();

                                }else {

                                    String message = jsonObject.getString("message");
                                    message = message.replace("Warning:","");
                                    alert(message,Toast.LENGTH_SHORT);
                                }
                            }

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).execute();

            }
        });

    }



    public void checkOutScreen()
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainframeL,new checkOut(true,getAddress));
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void closeScreen()
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainframeL,new cartFragment());
        transaction.addToBackStack(null);
        transaction.commit();

    }




    public void alert(String message,int duration)
    {
        Toast.makeText(getContext(),message,duration).show();
    }



}