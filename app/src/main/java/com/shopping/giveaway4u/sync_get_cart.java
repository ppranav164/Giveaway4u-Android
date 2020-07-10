package com.shopping.giveaway4u;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class sync_get_cart extends AsyncTask<String,String,String>
{


    config_hosts hosts;

    private Context context;

    int proid;

    cart cart;



    String API_URL = "http://192.168.1.108/index.php?route=api/cart";

    public sync_get_cart(Context cxt , cart cart)
    {

        this.context = cxt;

        this.cart = cart;

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

       cart.loadCarts(s);

    }

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder builder = new StringBuilder();

        SharedPreferences sharedpref = context.getSharedPreferences("cookie",Context.MODE_PRIVATE);

        String token = sharedpref.getString("token","nil");


        try {

            URL url = new URL(API_URL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setDoOutput(true);

            connection.setRequestProperty("Cookie","OCSESSID="+token+";");

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













