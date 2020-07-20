package com.shopping.giveaway4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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

    TextView ordersid,ordersDate,delAddress,orderstatus,headerCount;
    RecyclerView recyclerView,summrecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        getSupportActionBar().setTitle("ORDER INFO");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderId = getIntent().getStringExtra("order_id");

        ordersid = findViewById(R.id.orderid);
        ordersDate = findViewById(R.id.orderdate);
        delAddress = findViewById(R.id.address);
        orderstatus = findViewById(R.id.orderstatus);
        headerCount = findViewById(R.id.headercounts);
        recyclerView = findViewById(R.id.inforecyclerview);
        summrecyclerview = findViewById(R.id.summaryrecyclerview);

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

           summaryArray = object.getJSONArray("totals");


       }catch (Exception e)
       {
           e.printStackTrace();
       }


       recycleradapter_infos adapter = new recycleradapter_infos(getApplicationContext(),producstArray);
       recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
       recyclerView.setAdapter(adapter);
       recyclerView.setNestedScrollingEnabled(false);

       recycleradapter_summarys summaryAdapter = new recycleradapter_summarys(getApplicationContext(),summaryArray);
       summrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
       summrecyclerview.setAdapter(summaryAdapter);
       summrecyclerview.setNestedScrollingEnabled(false);

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