package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class forgottpasswrodActivity extends AppCompatActivity implements TextWatcher {


    String emailID;

    EditText femail;

    Button submitbtn;

    config_hosts hosts = new config_hosts();

    String URL_RES = hosts.FORGOTTEN;

    TextView warningview;

    RelativeLayout relativeLayout;

    private ProgressDialog dialog;

    View resetPassLayout;

    View OTP_SCREEN;

    String OTP;


    Button clearOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new ProgressDialog(forgottpasswrodActivity.this);
        dialog.setTitle("Verifying");
        dialog.setCancelable(false);

        emailID = getIntent().getStringExtra("email");
        setContentView(R.layout.activity_forgottpasswrod);


        OTP_SCREEN = getLayoutInflater().inflate(R.layout.otp_screen,null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        femail = findViewById(R.id.femail);

        submitbtn = findViewById(R.id.submitemail);

        warningview = findViewById(R.id.warning);

        relativeLayout = findViewById(R.id.forgottenlayout);

        resetPassLayout = getLayoutInflater().inflate(R.layout.reset_password,null);

        if (emailID != null || emailID.isEmpty())
        {
            femail.setText(emailID);
        }

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                warningview.setText(null);
                String emailid = femail.getText().toString();
                sendForgottenpass(emailid);

            }
        });


        Button restpassBtn = resetPassLayout.findViewById(R.id.submitreset);

        restpassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("RESET ","resetting password");
                dialog.show();
                clearError();
                setPassword();
            }
        });


        clearOtp = OTP_SCREEN.findViewById(R.id.clearOTP);

        if (OTP_SCREEN != null)
        {
            triggerInputs();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    public void sendForgottenpass(String email_address)
    {

        String PARAM  = "email="+email_address;

        Log.e("sendForgottenpass",PARAM);

        new syncAsyncTask(forgottpasswrodActivity.this, "POST",URL_RES,PARAM, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("sendForgottenpass",object);

                try {

                    JSONObject jsonObject = new JSONObject(object);
                    if (jsonObject.has("isSuccess"))
                    {
                        setSuccess();
                    }else {

                        String warning = jsonObject.getString("error_warning");
                        setError(warning);
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }


    public void setError(String message)
    {
        dialog.dismiss();
        message = message.replace("Warning:","");
        warningview.setText(message);
    }


    public void setSuccess()
    {
        dialog.dismiss();

        warningview.setText(null);
        Log.e("forgott_pass","email sent");

        relativeLayout.removeAllViews();

        relativeLayout.addView(OTP_SCREEN);

        Button button = OTP_SCREEN.findViewById(R.id.submit_OTP);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                resetPassword(OTP_SCREEN);
            }
        });
    }



    public void triggerInputs()
    {
        View layout = OTP_SCREEN;

        EditText fnumber = layout.findViewById(R.id.fnum);
        EditText secnumber = layout.findViewById(R.id.snum);
        EditText thdnumber = layout.findViewById(R.id.tnum);
        EditText foutnumber = layout.findViewById(R.id.fonum);


        fnumber.addTextChangedListener(this);
        secnumber.addTextChangedListener(this);
        thdnumber.addTextChangedListener(this);
        foutnumber.addTextChangedListener(this);


        clearOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearOTP();
            }
        });
    }


    public void resetPassword(View layout)
    {

        EditText fnumber = layout.findViewById(R.id.fnum);
        EditText secnumber = layout.findViewById(R.id.snum);
        EditText thdnumber = layout.findViewById(R.id.tnum);
        EditText foutnumber = layout.findViewById(R.id.fonum);

         OTP = fnumber.getText().toString()+secnumber.getText().toString()+thdnumber.getText().toString()+foutnumber.getText().toString();

        Log.e("OTP NUMBER","OTP :"+OTP);
        String emailid = femail.getText().toString();

        verifyReset(emailid,OTP);
    }


    public void verifyReset(String email,String code)
    {
        String PARAM = "&code="+code;
        String RESET_LINK = hosts.RESET+PARAM;
        Log.e("verifyReset",PARAM);


        new syncAsyncTask(forgottpasswrodActivity.this, "GET", RESET_LINK,null, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                try {

                    JSONObject object1 = new JSONObject(object);

                    if (object1.length() > 0)
                    {
                        dialog.dismiss();
                    }

                    if (object1.has("isVerified"))
                    {
                        if (object1.getBoolean("isVerified"))
                        {
                            relativeLayout.removeAllViews();
                            relativeLayout.addView(resetPassLayout);
                        }
                    }else {
                        String error = object1.getString("error");
                        otp_error(error);
                    }

                }catch (Exception e)
                {
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }
        }).execute();

    }



    public void otp_error(String errorMessage)
    {
        TextView error_warning =  OTP_SCREEN.findViewById(R.id.warning);

        error_warning.setText(errorMessage);
    }


    public void setPassword()
    {
        String CODE = "&code="+OTP;

        String RESET_LINK = hosts.RESET+CODE;

        EditText NEWPASS = resetPassLayout.findViewById(R.id.newpass);
        EditText CONFPASS = resetPassLayout.findViewById(R.id.confpass);
        String password = NEWPASS.getText().toString();
        String confpass = CONFPASS.getText().toString();

        String PARAM = "password="+password;
        PARAM += "&confirm="+confpass;

        Log.e("RESET LINK",RESET_LINK);
        Log.e("RESET PASSWORD",PARAM);

        new syncAsyncTask(forgottpasswrodActivity.this, "POST", RESET_LINK,PARAM, new jsonObjects() {
            @Override
            public void getObjects(String object) {
                Log.e("RESET PASSWORD",object);

                try {

                    JSONObject jsonObject = new JSONObject(object);

                    if (jsonObject.length() > 0)
                    {
                        dialog.dismiss();
                        clearOTP();
                    }

                    if (jsonObject.has("error_password"))
                    {
                        String errorpass = jsonObject.getString("error_password");
                        String errconf = jsonObject.getString("error_confirm");
                        errorPassword(errorpass,errconf);
                    }

                    if (jsonObject.has("reset"))
                    {
                        if (jsonObject.getBoolean("reset") == true)
                        {
                            String message = jsonObject.getString("message");
                            passwordResetSuccess(message);
                        }

                    }

                    if (jsonObject.has("error"))
                    {
                        String error = jsonObject.getString("error");
                        showWarning(error);
                    }


                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }


    public void errorPassword(String errorpass,String errorconfirm)
    {
        EditText NEWPASS = resetPassLayout.findViewById(R.id.newpass);
        EditText CONFPASS = resetPassLayout.findViewById(R.id.confpass);

        if (!errorpass.isEmpty())
        {
            NEWPASS.setError(errorpass);
        }

        if (!errorconfirm.isEmpty())
        {
            CONFPASS.setError(errorconfirm);
        }

    }

    public void passwordResetSuccess(String message)
    {
        relativeLayout.removeAllViews();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.forgottenlayout,new success(message));
        transaction.commit();
    }


    public void clearError()
    {
        EditText NEWPASS = resetPassLayout.findViewById(R.id.newpass);
        EditText CONFPASS = resetPassLayout.findViewById(R.id.confpass);

        NEWPASS.setError(null);
        CONFPASS.setError(null);
    }

    public void showWarning(String warning)
    {
        TextView warningtext = resetPassLayout.findViewById(R.id.warning);
        warningtext.setText(warning);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        Log.e("onTextChanged",""+s.length());

        EditText fnumber = OTP_SCREEN.findViewById(R.id.fnum);
        EditText secnumber = OTP_SCREEN.findViewById(R.id.snum);
        EditText thdnumber = OTP_SCREEN.findViewById(R.id.tnum);
        EditText foutnumber = OTP_SCREEN.findViewById(R.id.fonum);

       if (fnumber.getText().length() > 0)
       {

           secnumber.requestFocus();
           fnumber.setEnabled(false);

       }

       if (secnumber.getText().length() > 0)
       {

           thdnumber.requestFocus();
           secnumber.setEnabled(false);

       }

       if (thdnumber.getText().length() > 0)
       {
           foutnumber.requestFocus();
           thdnumber.setEnabled(false);
       }

        if (foutnumber.getText().length() > 0)
        {
            foutnumber.setEnabled(false);
        }



    }

    @Override
    public void afterTextChanged(Editable s) {

    }



    public void clearOTP()
    {
        EditText fnumber = OTP_SCREEN.findViewById(R.id.fnum);
        EditText secnumber = OTP_SCREEN.findViewById(R.id.snum);
        EditText thdnumber = OTP_SCREEN.findViewById(R.id.tnum);
        EditText foutnumber = OTP_SCREEN.findViewById(R.id.fonum);

        fnumber.setText(null);
        secnumber.setText(null);
        thdnumber.setText(null);
        foutnumber.setText(null);

        fnumber.setEnabled(true);
        secnumber.setEnabled(true);
        thdnumber.setEnabled(true);
        foutnumber.setEnabled(true);

        fnumber.requestFocus();

    }




}