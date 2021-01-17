package com.shopping.gway_4u;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class downloader extends AsyncTask<String,String,String>
{

    private Context context;
    String param;
    String API_URL;
    String method;
    private jsonObjects jsonObjects;

    private ProgressDialog pDialog;



    String filename;

    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

    String downloadedFile;

    boolean isAborted = false;

    String size = "loading";

    int downloadedSize = 0;

    int fileSize = 0;

    public downloader(Context cxt, String method, String apiurl,String filename, String param, jsonObjects info)
    {
        this.context = cxt;
        this.method = method;
        this.filename = filename;
        this.param = param;
        this.jsonObjects = info;
        this.API_URL = apiurl;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(context);
        pDialog.setTitle("Downloading...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(context,"Download Canceled",Toast.LENGTH_LONG).show();
                deleteFile();

            }
        });
        pDialog.show();

    }


    public void deleteFile()
    {
        File file = new File(downloadedFile);

        Log.e("Download PATH",downloadedFile);

        if (file.exists())
        {
            file.delete();
            Log.e("File exists",""+file.exists());
        }else
        {
            Log.e("File exists",""+file.exists());
        }

        Log.e("deletedfile",downloadedFile);

    }


    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        Log.e("isAborted",""+isAborted);
        pDialog.setMessage(values[0]);
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        jsonObjects.getObjects(s);

        pDialog.dismiss();

        if (downloadedSize < fileSize)
        {
            Toast.makeText(context,"Download Corrupted please try in web",Toast.LENGTH_LONG).show();

        }else {

            Toast.makeText(context,"File saved to "+root,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder builder = new StringBuilder();

        SharedPreferences sharedpref = context.getSharedPreferences("cookie",Context.MODE_PRIVATE);

        String token = sharedpref.getString("token","nil");

        int count;


        try {

            System.out.println("Downloading");

            URL url = new URL(API_URL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Cookie","OCSESSID="+token+";");

            if (method.equals("POST"))
            {
                connection.setDoOutput(true);
            }

            if (param != null)
            {
                connection.getOutputStream().write(param.getBytes("UTF-8"));
            }

            connection.connect();

            // getting file length
            int lenghtOfFile = connection.getContentLength();

            Log.e("downloader","File size is "+lenghtOfFile);

            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            String fileformat = filename+System.currentTimeMillis()+".zip";
            downloadedFile = root+"/"+fileformat;
            OutputStream output = new FileOutputStream(root+"/"+fileformat);
            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // writing data to file
                output.write(data, 0, count);

                Log.e("File size","Size is : "+bytesIntoHumanReadable(total));

                pDialog.setIndeterminate(false);
                pDialog.setMax(100);


                Log.e("Percent","Downloading > "+total*100/lenghtOfFile);

                int downloaded = (int) (total*100/lenghtOfFile);

                downloadedSize = downloaded;
                fileSize = lenghtOfFile;

                Log.e("DOWNLOAD",progressText(bytesIntoHumanReadable(lenghtOfFile),bytesIntoHumanReadable(total)));

                pDialog.setProgress(downloaded);

                publishProgress(progressText(bytesIntoHumanReadable(lenghtOfFile),bytesIntoHumanReadable(total)));

            }

            // flushing output
            output.flush();

            input.close();

            // closing streams
            output.close();

        }catch (Exception e)
        {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }


    private String bytesIntoHumanReadable(long bytes) {

        long kilobyte = 1024;
        long megabyte = kilobyte * 1024;
        long gigabyte = megabyte * 1024;
        long terabyte = gigabyte * 1024;

        if ((bytes >= 0) && (bytes < kilobyte)) {
            return bytes + " B";

        } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
            return (bytes / kilobyte) + " KB";

        } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
            return (bytes / megabyte) + " MB";

        } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
            return (bytes / gigabyte) + " GB";

        } else if (bytes >= terabyte) {
            return (bytes / terabyte) + " TB";

        } else {
            return bytes + " Bytes";
        }
    }


    public String progressText(String filesize,String downloadedSize)
    {
        size = filesize+"/"+downloadedSize;

        return filesize+"/"+downloadedSize;
    }




}













