package com.shopping.gway_4u;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class TermsConditions extends AppCompatActivity {

    config_hosts hosts = new config_hosts();

    String URL = hosts.TERMSC;

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        webView = findViewById(R.id.policyweb);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL);

        Log.e("Link",URL);

    }
}