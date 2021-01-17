package com.shopping.gway_4u;


import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class syncLogin extends AsyncTask<String,String,String>
{


    config_hosts hosts = new config_hosts();

    private Context context;

    private login mlogin;

    private  String email;

    private String password;

    String API_URL = hosts.login;

    public syncLogin(Context cxt, login mlogin , String memail, String mpassword)
    {

        this.context = cxt;
        this.mlogin = mlogin;

        this.email = memail;
        this.password = mpassword;

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        mlogin.confirm(s);


    }

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder builder = new StringBuilder();



        try {

            URL url = new URL(API_URL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            String param = "email="+email+"&"+"password="+password;

            connection.setRequestMethod("POST");

            connection.setDoOutput(true);

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













