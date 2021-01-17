package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebBrowser extends AppCompatActivity {

    String url ="";

    WebView webView;

    config_hosts config_hosts = new config_hosts();

    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

    String filename = "gway_4u";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null)
        {
            url = getIntent().getStringExtra("download_link");
            filename = getIntent().getStringExtra("file");
        }

        setContentView(R.layout.activity_web_browser);
        webView = findViewById(R.id.browserview);

        SharedPreferences sharedpref = getSharedPreferences("cookie", Context.MODE_PRIVATE);
        String token = sharedpref.getString("token","nil");
        Log.e("Token",token);
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(webView.getContext());
        CookieManager manager = CookieManager.getInstance();
        manager.setAcceptCookie(true);
        manager.removeSessionCookie();
        manager.setCookie(config_hosts.BASE_URL,"OCSESSID="+token);
        cookieSyncManager.sync();
        String cookie = manager.getCookie(config_hosts.BASE_URL);
        Log.d("Cookie Call", "cookie ------>"+cookie);

        start(url);

        startdownload();

    }




    public void start(String download)
    {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(download);
        String successURLExisits = webView.getUrl();
        Log.e("Downloading from ",successURLExisits);
    }


    public void startdownload()
    {

        final String fileformat = filename+System.currentTimeMillis()+".zip";

        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileformat);
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                        Toast.LENGTH_LONG).show();
            }
        });
    }



}