package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrackPackage extends AppCompatActivity {

    config_hosts hosts = new config_hosts();

    String url = hosts.TrackOrder;

    String orderID;
    String orderDate;

    RecyclerView recyclerView;
    TextView ordeidTv,orderAddedTv;

    ArrayList<String> dates = new ArrayList<>();
    ArrayList<String> messages = new ArrayList<>();
    ArrayList<String> statuses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderID = getIntent().getStringExtra("order_id");
        orderDate = getIntent().getStringExtra("order_date");

        setContentView(R.layout.activity_track_package);

        recyclerView = findViewById(R.id.track_recyclerview);

        ordeidTv = findViewById(R.id.track_orderid);
        orderAddedTv = findViewById(R.id.track_date_added);

        ordeidTv.setText("Order ID : #"+orderID);
        orderAddedTv.setText("Order Date : "+orderDate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Track Delivery");

        getTrackingDetails(orderID);

    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return super.onSupportNavigateUp();
    }



    public void getTrackingDetails(String orderid)
    {

        new syncAsyncTask(getApplicationContext(), "GET", url + orderid, null, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("Track",object);

                try {

                    JSONObject object1 = new JSONObject(object);
                    JSONArray jsonArray = object1.getJSONArray("track");
                    setTrackingList(jsonArray);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();

    }


    public void setTrackingList(JSONArray jsonArray)
    {
        try {

            for (int i=0; i < jsonArray.length(); i++)
            {
                JSONObject loopObjects = jsonArray.getJSONObject(i);
                String date = loopObjects.getString("date_added");
                String status = loopObjects.getString("status");
                String message = loopObjects.getString("comment");

                dates.add(date);
                statuses.add(status);
                messages.add(message);
            }

            showList();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void showList()
    {

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        track_adapter adapter = new track_adapter(getApplicationContext(),dates,messages,statuses);
        recyclerView.setAdapter(adapter);

    }


}