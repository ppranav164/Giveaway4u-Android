package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class orderInfo extends AppCompatActivity {

    String orderId;

    config_hosts hosts = new config_hosts();

    String API_URL = hosts.orderInfo;

    String TAG = getClass().getName();

    JSONArray producstArray = null;

    JSONArray summaryArray = null;

    TextView ordersid,ordersDate,delAddress,orderstatus,headerCount,paymentMethod;
    RecyclerView recyclerView,summrecyclerview;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        getSupportActionBar().setTitle("ORDER INFO");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderId = getIntent().getStringExtra("order_id");

        dialog = new Dialog(this);

        openDialog();

        ordersid = findViewById(R.id.orderid);
        ordersDate = findViewById(R.id.orderdate);
        delAddress = findViewById(R.id.address);
        orderstatus = findViewById(R.id.orderstatus);
        headerCount = findViewById(R.id.headercounts);
        recyclerView = findViewById(R.id.inforecyclerview);
        summrecyclerview = findViewById(R.id.summaryrecyclerview);
        paymentMethod = findViewById(R.id.pay_method);

        getOrderInfo();
    }



    public void getOrderInfo()
    {
        new syncAsyncTask(getApplicationContext(), "GET", API_URL+orderId, null, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                try {

                    JSONObject objects = new JSONObject(object);
                    getOrderData(objects);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }



    public void getOrderData(JSONObject object)
    {

       try {

           String ORDERID = object.getString("order_id");
           String DATEADDED = object.getString("date_added");
           String STATUS = "";
           String ITEMS = "";
           String ADDRESS = object.getString("shipping_address");
           String PAYMENT_METHOD = object.getString("payment_method");

           JSONArray getTrackInfo = object.getJSONArray("track");
           for (int i=0; i< getTrackInfo.length(); i++)
           {
               JSONObject TRACK = getTrackInfo.getJSONObject(i);
               STATUS = TRACK.getString("status");
           }

           JSONArray getProducts = object.getJSONArray("products");

           producstArray = getProducts;

           ITEMS  = String.valueOf(getProducts.length());

           orderstatus.setText("STATUS : "+ STATUS);
           ordersDate.setText("ORDER DATE :" + DATEADDED);
           ordersid.setText("ORDER ID : #"+ ORDERID);
           delAddress.setText(Html.fromHtml(ADDRESS));
           delAddress.setTextColor(getResources().getColor(R.color.black));
           headerCount.setText("PRODUCTS"+" ("+ITEMS+")"+" ITEMS");
           paymentMethod.setText(PAYMENT_METHOD);
           summaryArray = object.getJSONArray("totals");


       }catch (Exception e)
       {
           e.printStackTrace();
       }


       recycleradapter_infos adapter = new recycleradapter_infos(getApplicationContext(),producstArray);
       recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
       recyclerView.setNestedScrollingEnabled(false);

       recycleradapter_summarys summaryAdapter = new recycleradapter_summarys(getApplicationContext(),summaryArray);
       summrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
       summrecyclerview.setNestedScrollingEnabled(false);


        recyclerView.setAdapter(adapter);
        summrecyclerview.setAdapter(summaryAdapter);

        closeDialog();

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




    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }

}