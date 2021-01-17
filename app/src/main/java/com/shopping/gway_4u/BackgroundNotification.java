package com.shopping.gway_4u;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONObject;

public class BackgroundNotification extends Service {

    private Handler handler;
    private Runnable runnable;

    config_hosts hosts = new config_hosts();

    String URL = hosts.notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Service is still running", Toast.LENGTH_LONG).show();

                new syncAsyncTask(getApplicationContext(), "GET", URL, null, new jsonObjects() {
                    @Override
                    public void getObjects(String object) {

                        Log.e("notification",object);

                        try {

                            JSONObject jsonObject = new JSONObject(object);
                            JSONArray array = jsonObject.getJSONArray("notifications");

                            if (array.length() > 0)
                            {
                                for (int i=0; i < array.length(); i++)
                                {
                                    JSONObject loop = array.getJSONObject(i);
                                    String title = loop.getString("title");
                                    String message = loop.getString("message");
                                    String product_id = loop.getString("product_id");
                                    String notification_id = loop.getString("notification_id");

                                    int id = Integer.parseInt(notification_id);

                                    Log.e("title",title);
                                    Log.e("message",message);
                                    Log.e("pro_id",product_id);
                                    Log.e("id",notification_id);
                                    generateNotification(title,message,product_id,id);
                                }
                            }else
                            {
                                Log.e("Notify","Empty");

                            }

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                    }
                }).execute();

                handler.postDelayed(runnable, 10000);
            }
        };

        handler.postDelayed(runnable, 15000);
    }




    public  void generateNotification(String body,String title,String product_id,int id)
    {

        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("product_id",product_id);
        intent.putExtra("id",id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        notificationBuilder.setSmallIcon(R.drawable.icon);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(body);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,notificationBuilder.build());

    }



}
