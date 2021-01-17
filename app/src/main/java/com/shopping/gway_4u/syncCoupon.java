package com.shopping.gway_4u;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class syncCoupon extends AsyncTask<String,String,String>
{


    public static String api;

    Activity activity;


    ProgressDialog dialog;

    String method;


    private Context context;

    String codes;

    String param;

    config_hosts hosts = new config_hosts();;

    message message;


    public syncCoupon(Context cxt, String code , message message)
    {



        this.context = cxt;

        this.api = hosts.coupon;

        this.method =  "POST";

        this.codes = code;

        param = "coupon="+code;

        this.message = message;

    }



    @Override
    protected void onPreExecute() {

        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        message.setMessage(s);

        Log.e("Coupon message",s);


    }


    @Override
    protected String doInBackground(String... strings) {

        StringBuilder builder = new StringBuilder();

        SharedPreferences sharedpref = context.getSharedPreferences("cookie",Context.MODE_PRIVATE);

        String token = sharedpref.getString("token","nil");




        try{

            URL link = new URL(api);

            HttpURLConnection connection = (HttpURLConnection) link.openConnection();

            connection.setRequestMethod(method);

            connection.setRequestProperty("Cookie","OCSESSID="+token+";");

            connection.getOutputStream().write(param.getBytes("UTF-8"));

            connection.connect();

            Log.e("coupon",param);

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













