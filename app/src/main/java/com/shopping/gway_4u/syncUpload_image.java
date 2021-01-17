package com.shopping.gway_4u;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class syncUpload_image extends AsyncTask<String,String,String>

{


    ProgressDialog dialog;

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = MainActivity.class.getSimpleName();

    private String selectedFilePath;

    //private String selectedFilePath = "/storage/emulated/0/Download/cat.jpeg";

    ImageView ivAttachment;
    Button bUpload;
    TextView tvFileName;
    PowerManager.WakeLock wakeLock;


    private Context context;

    Uri sourceURI;


    upload upload;


    config_hosts hosts = new config_hosts();


    String SERVER_URL = hosts.FILE_UPLOAD;

    public syncUpload_image(Context cxt, Uri sourceUri, upload upload)
    {

        this.context = cxt;

        this.sourceURI = sourceUri;

        this.upload = upload;

        selectedFilePath = FilePath.getPath(context,sourceURI);

        Log.e("PATH",selectedFilePath);
    }




    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        dialog = new ProgressDialog(context);

        dialog.setMessage("Uploading file");

        dialog.show();


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        upload.status(s);

        if (s != null || s != "")
        {
            dialog.dismiss();
        }
    }

    @Override
    protected String doInBackground(String... strings) {


        int serverResponseCode = 0;

        StringBuilder builder = new StringBuilder();

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";



        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];


        try {

            FileInputStream fileInputStream = new FileInputStream(selectedFile);
            URL url = new URL(SERVER_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);//Allow Inputs
            connection.setDoOutput(true);//Allow Outputs
            connection.setUseCaches(false);//Don't use a cached Copy
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);
            connection.setRequestProperty("file",selectedFilePath);

            //creating new dataoutputstream
            dataOutputStream = new DataOutputStream(connection.getOutputStream());

            //writing bytes to data outputstream
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
                    + selectedFilePath + "\"" + lineEnd);

            dataOutputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);

            dataOutputStream.writeBytes(lineEnd);

            //returns no. of bytes present in fileInputStream
            bytesAvailable = fileInputStream.available();
            //selecting the buffer size as minimum of available bytes or 1 MB
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            //setting the buffer as byte array of size of bufferSize
            buffer = new byte[bufferSize];

            //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);


            //loop repeats till bytesRead = -1, i.e., no bytes are left to read
            while (bytesRead > 0) {

                try {

                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer, 0, bufferSize);
                } catch (OutOfMemoryError e) {
                    Toast.makeText(context, "Insufficient Memory!", Toast.LENGTH_SHORT).show();
                }
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            try{
                serverResponseCode = connection.getResponseCode();
            }catch (OutOfMemoryError e){
                Toast.makeText(context, "Memory Insufficient!", Toast.LENGTH_SHORT).show();
            }
            String serverResponseMessage = connection.getResponseMessage();

            Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

            Log.e("Type",selectedFilePath);



            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String read = reader.readLine();

            String message = builder.append(read).toString();


            //closing the input and output streams
            fileInputStream.close();
            dataOutputStream.flush();
            dataOutputStream.close();

           if (wakeLock != null)
           {
               if (wakeLock.isHeld()) {

                   wakeLock.release();
               }
           }


        }catch (Exception e)
        {
            e.printStackTrace();
        }



        return builder.toString();

        }



}













