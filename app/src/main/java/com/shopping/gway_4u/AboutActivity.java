package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {


    String APP_VERSION;

    TextView termslink,privacylink,appversion,appname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        termslink = findViewById(R.id.termslink);
        privacylink = findViewById(R.id.policylink);
        appname = findViewById(R.id.app_name);
        appversion  = findViewById(R.id.app_version);
        APP_VERSION = BuildConfig.VERSION_NAME;
        appversion.setText("Version "+APP_VERSION);
        appname.setText(getString(R.string.app_name));


        termslink.setOnClickListener(this);
        privacylink.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return super.onSupportNavigateUp();
    }


    public void openPrivacyPolicy()
    {
        startActivity(new Intent(AboutActivity.this,PrivacyPolicy.class));

    }

    public void openTermsLink()
    {
        startActivity(new Intent(AboutActivity.this,TermsConditions.class));

    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.termslink : openTermsLink();
            break;
            case R.id.policylink : openPrivacyPolicy();
            break;
        }
    }


}