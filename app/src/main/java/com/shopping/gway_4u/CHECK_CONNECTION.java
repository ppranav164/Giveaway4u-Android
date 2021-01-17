package com.shopping.gway_4u;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CHECK_CONNECTION extends AsyncTask<String,String,String>
{

    private Context context;
    String param;
    String API_URL = "https://www.google.com";
    String method;
    private jsonObjects jsonObjects;


    public CHECK_CONNECTION(Context cxt, String method, String param, jsonObjects info)
    {
        this.context = cxt;
        this.method = method;
        this.param = param;
        this.jsonObjects = info;
    }




    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        jsonObjects.getObjects(s);

    }

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder builder = new StringBuilder();

        SharedPreferences sharedpref = context.getSharedPreferences("cookie",Context.MODE_PRIVATE);

        String token = sharedpref.getString("token","nil");



        try {

            URL url = new URL(API_URL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);

            if (method.equals("POST"))
            {
                connection.setDoOutput(true);
            }

            connection.setRequestProperty("Cookie","OCSESSID="+token+";");

            if (param != null)
            {
                connection.getOutputStream().write(param.getBytes("UTF-8"));
            }

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
            Log.e("INTERNETCONN",e.getMessage());
            String error = e.getMessage();
            builder.append("error");
        }



        return builder.toString();
    }
}













