package com.shopping.gway_4u;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseCloudService extends FirebaseMessagingService {


    config_hosts hosts = new config_hosts();

    String URL = hosts.FIREBASE_SEND_TOKEN_URL;

    private static String TAG = "FirebaseCloudService";

    SharedPreferences.Editor editor;

    SharedPreferences.Editor verifySuccess;


    Context context;


    public FirebaseCloudService()
    {

    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        Map<String,String> datas = remoteMessage.getData();

        verifySuccess  = getSharedPreferences("VERIFY_ORDER",MODE_PRIVATE).edit();

        Log.e("FBase","Received");

        String id = "";

        if (datas.containsKey("order_id"))
        {
            id = datas.get("order_id");

            verifySuccess.putString("ORDER_ID",id);
            verifySuccess.apply();

        }

        if (datas.containsKey("product_id"))
        {
            id = datas.get("product_id");
        }


        Log.e("GCMURL",id);


        generateNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle(),id);

    }



    public  void generateNotification(String body,String title,String id)
    {

        int uniqueid;

        if (id.equals(""))
        {
            uniqueid = 0;
        }else {

            uniqueid = Integer.parseInt(id);
        }

        Intent intent = new Intent(this,orderInfo.class);
        intent.putExtra("product_id",id);
        intent.putExtra("order_id",id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        notificationBuilder.setLights(Color.RED, 3000, 3000);
        notificationBuilder.setOnlyAlertOnce(true);
        notificationBuilder.setSmallIcon(R.drawable.icon);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(body);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(uniqueid,notificationBuilder.build());


    }

    @Override
    public void onNewToken(String token) {
        Log.e(TAG, "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        editor = getSharedPreferences("firebase",MODE_PRIVATE).edit();
        editor.putString("refreshed",token);
        editor.apply();

        sendRegistrationToServer(token);

    }

    private void sendRegistrationToServer(String token) {

        String param = "gcm="+token;

        new syncAsyncTask(getApplicationContext(), "POST",URL,param, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.w("GCM to server",object);

            }
        }).execute();


    }


}
