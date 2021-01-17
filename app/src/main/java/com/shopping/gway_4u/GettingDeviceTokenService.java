package com.shopping.gway_4u;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class GettingDeviceTokenService extends FirebaseInstanceIdService {


    SharedPreferences.Editor editor ;


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        editor = getSharedPreferences("firebase",MODE_PRIVATE).edit();
        String DeviceToken = FirebaseInstanceId.getInstance().getToken();
        editor.putString("fbtoken",DeviceToken);
        editor.apply();

        Log.e("GettinToken",DeviceToken);

    }

}
