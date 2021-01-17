package com.shopping.gway_4u;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class syncSlideshow extends AsyncTask<String,String,String>
{


    config_hosts hosts = new config_hosts();



    String api = hosts.SLIDER_IMAGE;


    private  slideshow sliders;

    private Context context;

    public syncSlideshow(Context cxt, slideshow ldata)
    {

        this.context = cxt;

        this.sliders = ldata;

    }



    @Override
    protected void onPreExecute() {

        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        sliders.loadSliders(s);

        Log.w("slideshow",s);

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













