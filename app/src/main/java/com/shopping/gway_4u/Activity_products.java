package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Activity_products extends AppCompatActivity {

    TextView prod;


    String api = "http://10.0.2.2/ecom/upload/index.php?route=api/featured";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_products);

        int val = getIntent().getIntExtra("product_id",99);

        prod = findViewById(R.id.display);

        prod.setText(String.valueOf(val));





    }





    public class sycnproducts extends AsyncTask<String,String,String>
    {




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            PhotoView imageView = findViewById(R.id.previewer);


           try {

               JSONObject object = new JSONObject(s);

               JSONArray array = object.getJSONArray("products");

               JSONObject idobj = array.getJSONObject(0);

               String prod = idobj.getString("thumb");

               prod = prod.replace("localhost","10.0.2.2");

               Picasso.get().load(prod).into(imageView);

               //Toast.makeText(getApplicationContext(),"Data : "+prod,Toast.LENGTH_LONG).show();


           }catch (Exception e)
           {
               e.printStackTrace();
           }

        }

        @Override
        protected String doInBackground(String... strings) {

            StringBuilder builder = new StringBuilder();


            try{

                URL link = new URL(api);

                HttpURLConnection connection = (HttpURLConnection) link.openConnection();

                connection.setRequestMethod("GET");

                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while (true) {
                    String readLine = reader.readLine();
                    String line = readLine;
                    if (readLine == null) {
                        break;
                    }

                    builder.append(line);
                }



            }catch (Exception e)
            {
                e.printStackTrace();
            }


            return builder.toString();
        }
    }





}
