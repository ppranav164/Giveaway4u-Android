package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Model.firstStep;

import Model.secondtStep;
import Model.thirdStep;
import ernestoyaquello.com.verticalstepperform.Step;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class TrackStepper extends AppCompatActivity implements StepperFormListener {


    private UserNameStep userNameStep;

    private firstStep firstStep;
    private secondtStep secondtStep;
    private thirdStep thirdStep;

    private VerticalStepperFormView verticalStepperForm;

    ArrayList<String> dates = new ArrayList<>();
    ArrayList<String> messages = new ArrayList<>();
    ArrayList<String> statuses = new ArrayList<>();

    config_hosts hosts = new config_hosts();

    String url = hosts.TrackOrder;

    String orderID;
    String orderDate;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderID = getIntent().getStringExtra("order_id");
        orderDate = getIntent().getStringExtra("order_date");


        setContentView(R.layout.activity_track_stepper);

        userNameStep = new UserNameStep("Payment Verified");

        dialog = new Dialog(TrackStepper.this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Track Delivery");

        openDialog();
        getTrackingDetails(orderID);

        // Find the form view, set it up and initialize it.
        verticalStepperForm = findViewById(R.id.stepper_form);


    }


    public void openDialog() {

        dialog.setContentView(R.layout.dialog_demo);
        dialog.setCancelable(false);
        dialog.show();

    }


    public void closeDialog()
    {
        dialog.dismiss();
    }


    @Override
    public void onCompletedForm() {

    }

    @Override
    public void onCancelledForm() {

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

                Log.e("stepper",object);

                try {

                    JSONObject object1 = new JSONObject(object);
                    JSONArray jsonArray = object1.getJSONArray("track");
                    setTrackingList(jsonArray);
                    closeDialog();
                }catch (Exception e)
                {
                    closeDialog();
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


            if (statuses.contains("Failed"))
            {
               showList(new firstStep("Failed","Invalid Payment"));
            }

            if (statuses.contains("Canceled"))
            {
                showList(new firstStep("Canceled","You canceled this order upon request"));
            }


            if (!statuses.contains("Failed") && !statuses.contains("Canceled"))
            {
               showTrackingLists();
            }



        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }




    public void showTrackingLists()
    {
        if (statuses.size()  == 1) //One data found
        {
            firstStep = new firstStep(statuses.get(0),messages.get(0));
            secondtStep = new secondtStep("","");
            thirdStep = new thirdStep("","");
            showList(firstStep);
        }

        if (statuses.size() == 2) //two data found
        {
            firstStep = new firstStep(statuses.get(0),messages.get(0));
            secondtStep = new secondtStep(statuses.get(1),messages.get(1));
            thirdStep = new thirdStep("","");
            secondtStep.getStepData();
            showList(firstStep,secondtStep);
        }


        if (statuses.size() == 3) //three data found
        {
            firstStep = new firstStep(statuses.get(0),messages.get(0));
            secondtStep = new secondtStep(statuses.get(1),messages.get(1));
            thirdStep = new thirdStep(statuses.get(2),messages.get(2));
            showList(firstStep,secondtStep,thirdStep);

        }

        if (statuses.size() == 4 && statuses.get(3).equals("Complete")) //three data found
        {
            firstStep = new firstStep(statuses.get(0),messages.get(0));
            secondtStep = new secondtStep(statuses.get(1),messages.get(1));
            thirdStep = new thirdStep(statuses.get(3),messages.get(3));
            showList(firstStep,secondtStep,thirdStep);

        }



    }


    public void showList(Step... steps)
    {
        verticalStepperForm
                .setup(this,steps)
                .allowStepOpeningOnHeaderClick(true)
                .displayBottomNavigation(true)
                .includeConfirmationStep(false)
                .displayStepButtons(false)
                .allowNonLinearNavigation(true)
                .closeLastStepOnCompletion(true)
                .init();

    }


}