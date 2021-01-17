package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONObject;

public class CurencySettings extends AppCompatActivity {


    RadioGroup radioGroup;

    config_hosts hosts = new config_hosts();

    String API_URL = hosts.getCurrency;

    String UPDATE_URL = hosts.updateCurrency;

    String  DefaultcurrencyCode = "";

    String selectedCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curency_settings);

        radioGroup = findViewById(R.id.radiogroupCurrency);

        getSupportActionBar().setTitle("Countries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getCurrencyData();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = group.findViewById(checkedId);

                selectedCode = radioButton.getTag().toString();
            }
        });

    }




    public void getCurrencyData()
    {
        new syncAsyncTask(getApplicationContext(), "GET", API_URL, null, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("currency",object);

                try {

                    JSONObject jsonObject = new JSONObject(object);

                    setJsonObjects(jsonObject);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }




    public void setJsonObjects(JSONObject jsonObjects)
    {
        try {

            DefaultcurrencyCode = jsonObjects.getString("code");

            JSONArray jsonArray = jsonObjects.getJSONArray("currencies");

            loadCurrency(jsonArray);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }





    public void loadCurrency(JSONArray jsonArray)
    {

        RadioButton[] radioButton  = new RadioButton[10];

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5,5,5,5);


        try {

            for (int i=0; i < jsonArray.length(); i++)
            {
                JSONObject loops = jsonArray.getJSONObject(i);
                String title = loops.getString("title");
                String code = loops.getString("code");

                radioButton[i] = new RadioButton(getApplicationContext());
                radioButton[i].setText(title);
                radioButton[i].setTag(code);
                radioButton[i].setPadding(20,20,20,20);
                radioButton[i].setLayoutParams(layoutParams);
                radioButton[i].setBackgroundColor(Color.parseColor("#F8F8F8"));
                radioGroup.addView(radioButton[i]);

                if (DefaultcurrencyCode.equals(code))
                {
                    radioButton[i].setChecked(true);
                }


            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.currency, menu);

        final MenuItem saveBtn = menu.findItem(R.id.menu_save);

        View view = saveBtn.getActionView();

       saveBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               updateCurrency();
               Intent intent = new Intent(getApplicationContext(),MainActivity.class);
               startActivity(intent);
               finish();
               return false;
           }
       });

        return true;
    }



    public void updateCurrency()
    {


        String param = "code="+selectedCode;

        new syncAsyncTask(getApplicationContext(), "POST", UPDATE_URL,param, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("update",object);

                reset();

            }
        }).execute();

    }


    public void reset()
    {
        String url ="http://192.168.1.108/index.php?route=common/home";

        new syncAsyncTask(getApplicationContext(), "GET", url, null, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("jome",object);
            }
        }).execute();
    }


}