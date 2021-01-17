package com.shopping.gway_4u;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class syncFeatured extends AsyncTask<String,String,String>
{


    config_hosts hosts = new config_hosts();

    String api = hosts.FEATURED;


    private  featured myfeatures;

    private Context context;

    public syncFeatured(Context cxt,featured ldata)
    {

        this.context = cxt;

        this.myfeatures = ldata;

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        myfeatures.loadFeatured(s);


    }

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder builder = new StringBuilder();

        SharedPreferences sharedpref = context.getSharedPreferences("cookie",Context.MODE_PRIVATE);
        String token = sharedpref.getString("token","nil");



        try{

            URL link = new URL(api);

            HttpURLConnection connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("GET");
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













