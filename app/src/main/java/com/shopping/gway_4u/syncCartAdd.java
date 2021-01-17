package com.shopping.gway_4u;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class syncCartAdd extends AsyncTask<String,String,String>
{


    config_hosts hosts = new config_hosts();

    private Context context;

    int proid;

    int quantity;

    int option_id;

    String fileCode;

    addedToCart toCart;

    String checkbox;

    String param;


    String API_URL = hosts.cart_add;

    public syncCartAdd(Context cxt, String params, addedToCart cart)
    {

        this.context = cxt;

        this.param = params;

        this.toCart = cart;


    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        toCart.loadCarts(s);

    }

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder builder = new StringBuilder();

        SharedPreferences sharedpref = context.getSharedPreferences("cookie",Context.MODE_PRIVATE);

        String token = sharedpref.getString("token","nil");


        try {

            URL url = new URL(API_URL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");


            Log.e("checkbox",param);

            connection.setRequestMethod("POST");

            connection.setDoOutput(true);

            connection.setRequestProperty("Cookie","OCSESSID="+token+";");

            connection.getOutputStream().write(param.getBytes("UTF-8"));

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













