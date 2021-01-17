package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

public class Activity_login extends AppCompatActivity {


    TextView accCr,forgottenpass;

    EditText Email,Pass;

    Button loginB;

    private static String email;

    private static  String password;

    private ImageView loadingview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.layout_login);


        accCr = findViewById(R.id.ClcreateAcc);

        forgottenpass = findViewById(R.id.forgotten);

        Email = findViewById(R.id.Enemail);

        Pass = findViewById(R.id.Enpass);

        loadingview = findViewById(R.id.loadingview);

        accCr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),Activity_Register.class);
                startActivity(intent);
            }
        });


        loginB = findViewById(R.id.Blogin);


        forgottenpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cEmail = Email.getText().toString();
                forgottenPassword(cEmail);
            }
        });



        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = Email.getText().toString();

                password = Pass.getText().toString();

                showLoading();

                new syncLogin(getApplicationContext(), new login() {

                    Boolean isLogged_in = false;


                    @Override
                    public void confirm(String message) {

                        Log.e("Login",message);

                        try {


                            JSONObject object = new JSONObject(message);

                          if (object.has("success")){


                              SharedPreferences.Editor sharedpref = getSharedPreferences("cookie",MODE_PRIVATE).edit();

                              sharedpref.putString("token",object.getString("Set-Cookie"));

                              sharedpref.putString("username",object.getString("username"));

                              sharedpref.putString("address_id",object.getString("address_id"));

                              sharedpref.putString("customer_id",object.getString("customer_id"));

                              sharedpref.putBoolean("logged_in",true);

                              sharedpref.apply();

                              Intent intent = new Intent(Activity_login.this,MainActivity.class);
                              startActivity(intent);
                              finish();

                          }else {
                              restart();
                              String warning = object.getString("warning").replace("Warning:","");
                              Toast.makeText(getApplicationContext(),warning,Toast.LENGTH_LONG).show();
                          }


                        }catch (Exception e)
                        {
                            e.printStackTrace();

                            Log.e("login",e.toString());

                            Toast.makeText(getApplicationContext(),"No internet connection please try again later",Toast.LENGTH_LONG).show();

                        }

                    }
                },email,password).execute();


            }
        });



    }






    public void forgottenPassword(String emailID)
    {
        Intent intent = new Intent(Activity_login.this,forgottpasswrodActivity.class);
        intent.putExtra("email",emailID);
        startActivity(intent);

    }



    public void showLoading()
    {
        loginB.setClickable(false);
        loadingview.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext()).asGif().load(R.drawable.loading).into(loadingview);
    }


    public void restart()
    {
        loginB.setClickable(true);
        loadingview.setVisibility(View.INVISIBLE);
    }





}
