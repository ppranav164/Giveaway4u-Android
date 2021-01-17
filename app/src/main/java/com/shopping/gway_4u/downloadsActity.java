package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class downloadsActity extends AppCompatActivity implements adapterClicklistener {


    String downloadURL = "";

    String loc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

    config_hosts config_hosts = new config_hosts();


    String downloadsLINK = config_hosts.downloads;


    WebView webView;

    RecyclerView recyclerView;

    boolean hasPermission;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads_actity);

        recyclerView = findViewById(R.id.downloadsview);

        checkPermissionForReadExtertalStorage();

        hasPermission = checkPermissionForReadExtertalStorage();

        //downloadFileFromweb();

        //downloadFile();

        getDownloads(downloadsLINK);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Downloads");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public void getDownloads(String url)
    {
        new syncAsyncTask(downloadsActity.this, "GET", url, null, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                try {

                    Log.e("downloadsActivity",object);
                    JSONObject jsonObject = new JSONObject(object);
                    JSONArray jsonArray = jsonObject.getJSONArray("downloads");
                    loadView(jsonArray);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }


    public void loadView(JSONArray array)
    {

        recycleradapter_downloads adapter = new recycleradapter_downloads(downloadsActity.this,array,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView.setAdapter(adapter);

    }



    public void downloadFile()
    {
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    public void downloadFileFromweb() {


        SharedPreferences sharedpref = getSharedPreferences("cookie", Context.MODE_PRIVATE);

        String token = sharedpref.getString("token","nil");


        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(webView.getContext());

        CookieManager manager = CookieManager.getInstance();

        manager.setAcceptCookie(true);

        manager.removeSessionCookie();

        manager.setCookie(config_hosts.BASE_URL,"OCSESSID="+token);

        cookieSyncManager.sync();

        String cookie = manager.getCookie(config_hosts.BASE_URL);

        Log.d("Cookie Call", "cookie ------>"+cookie);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(downloadURL);

        Log.w("Download start","downloading");
    }


    public void testfiledownload() {

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(downloadURL);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());
        downloadManager.enqueue(request);
    }

    private void DownloadSuccess() {

        Toast.makeText(getApplicationContext(),"Downloaded successfully",Toast.LENGTH_LONG).show();
    }


    public boolean checkPermissionForReadExtertalStorage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            return result == PackageManager.PERMISSION_GRANTED;
        }

        return false;
    }


    public void requestPermissionForReadExtertalStorage() throws Exception {

        try {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }



    @Override
    public void getPosition(int pos, String data,String title) {


        Log.e("Download Link",data);

        openWebPage(data);
        //directDownload(data,title);
        //testServer(data);
    }


    public void testServer(String downloadURL)
    {
        new syncAsyncTask(getApplicationContext(), "GET",downloadURL, null, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("Test Response",object);
            }
        }).execute();

    }


    public void directDownload(String url,String title)
    {
        hasPermission = checkPermissionForReadExtertalStorage();

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());

        if (file.isDirectory())
        {
            Log.e("Downloads Folder","Exists");
        }else {

            Log.e("Downloads Folder","Doesn't Exist");
            file.mkdir();
        }

        if (!hasPermission)
        {

            grantWritePermission();

        }else{

            new downloader(downloadsActity.this, "GET",url,title,null, new jsonObjects() {
                @Override
                public void getObjects(String object) {
                    Log.e("Response Download","Status :"+object);
                }
            }).execute();
        }

        Log.e("Folder Creatable","Status "+hasPermission);
    }


    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    public void openbrowser(String url,String filename)
    {
        Intent intent = new Intent(downloadsActity.this,WebBrowser.class);
        intent.putExtra("download_link",url);
        intent.putExtra("file",filename);
        startActivity(intent);
    }


    public void grantWritePermission()
    {
        try {

            requestPermissionForReadExtertalStorage();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }





}