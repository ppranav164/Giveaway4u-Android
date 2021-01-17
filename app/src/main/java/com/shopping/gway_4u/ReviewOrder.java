package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReviewOrder extends AppCompatActivity {


    RatingBar ratingBar;

    EditText reviewcomment;

    Button reviewSend;

    Dialog dialog;

    String USER;
    String COMMENTS;
    float RATIING;

    String PRODUCT_ID;

    String ORDER_ID;

    String USERNAME;

    config_hosts hosts = new config_hosts();

    String API_URL = hosts.orderInfo;

    SharedPreferences sharedPreferences;

    CheckBox identitybox;

    LinearLayout linearLayout;

    TextView reviewDESCS;

    ImageView reviewImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ORDER_ID = getIntent().getStringExtra("order_id");

        getProductId(ORDER_ID);

        setContentView(R.layout.activity_review_order);

        dialog = new Dialog(ReviewOrder.this);

        getOrderInfo();
        openDialog();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Write Feedback");


        sharedPreferences = getSharedPreferences("cookie",MODE_PRIVATE);


        ratingBar = findViewById(R.id.ratingbutton);
        reviewcomment = findViewById(R.id.reviewcomment);
        reviewSend = findViewById(R.id.sendreview);
        linearLayout = findViewById(R.id.container_feedback);

        identitybox = findViewById(R.id.checkbox_identitiy);

        reviewDESCS = findViewById(R.id.reviewDesc);

        reviewImageview = findViewById(R.id.reviewimage);

        reviewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog();

                if (identitybox.isChecked())
                {
                    USERNAME = "Anonymous";

                }else {

                    USERNAME = sharedPreferences.getString("username",null);
                }

                COMMENTS = reviewcomment.getText().toString();
                RATIING = ratingBar.getRating();

                reviewcomment.setError(null);
                submitFeedback();
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return super.onSupportNavigateUp();
    }


    public void getProductId(String orderid)
    {
        String PARAM = "&order_id="+orderid;
        String paramURL = hosts.GET_PRODUCT_ID+PARAM;

        new syncAsyncTask(ReviewOrder.this, "GET", paramURL, null, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("getProductId",object);

                try {

                    JSONArray array = new JSONArray(object);
                    JSONObject jsonObject = array.getJSONObject(0);

                    if (jsonObject.has("product_id"))
                    {
                        closeDialog();
                        PRODUCT_ID = jsonObject.getString("product_id");
                    }

                    Log.e("product_id",PRODUCT_ID);

                }catch (Exception e)
                {
                    e.printStackTrace();

                    closeDialog();
                }

            }
        }).execute();

    }

    public void submitFeedback()
    {

        String URL = hosts.SUBMIT_FEEDBACK;

        int rate = Math.round(RATIING);

        String PARAM = "name="+USERNAME;

        PARAM += "&text="+COMMENTS;

        PARAM +="&rating="+rate;

        PARAM +="&product_id="+PRODUCT_ID;

        Log.e("submitFeedback",PARAM);

        String textlength = String.valueOf(reviewcomment.length());

        Log.e("textlength",textlength);

        new syncAsyncTask(ReviewOrder.this, "POST", URL, PARAM, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("submitFeedback",object);

                try {

                    if (new JSONObject(object).has("success"))
                    {
                        closeDialog();
                        Log.e("submitFeedback","SENT");
                        String message  = new JSONObject(object).getString("success");
                        successMessage(message);
                    }

                    if (new JSONObject(object).has("error"))
                    {
                        reviewcomment.setError("Feedback  Text must be between 25 and 1000 characters!");
                        closeDialog();
                        Log.e("submitFeedback","SENT");
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).execute();


    }


    public void successMessage(String message)
    {
        linearLayout.removeAllViews();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container_feedback,new success(message));
        transaction.addToBackStack(null);
        transaction.commit();

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


    public void getOrderInfo()
    {
        openDialog();
        new syncAsyncTask(getApplicationContext(), "GET", API_URL+ORDER_ID, null, new jsonObjects() {
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

            JSONArray getProducts = object.getJSONArray("products");

            JSONObject jsonObject = getProducts.getJSONObject(0);
            String PRODUCT_NAME = jsonObject.getString("name");
            String MODEL_NO = jsonObject.getString("model");
            JSONArray images = jsonObject.getJSONArray("images");
            JSONObject imgObjects = images.getJSONObject(0);
            String IMAGE = imgObjects.getString("thumb");

            String DESCS = PRODUCT_NAME+System.lineSeparator()+MODEL_NO;

            reviewDESCS.setText(DESCS);

            Picasso.get().load(IMAGE).into(reviewImageview);

            closeDialog();

        }catch (Exception e)
        {
            e.printStackTrace();
            closeDialog();
        }


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}