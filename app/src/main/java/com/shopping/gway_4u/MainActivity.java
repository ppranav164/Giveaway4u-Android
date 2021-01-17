package com.shopping.gway_4u;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {


    TextView notify;
    TextView usernameView;
    Dialog dialog;

    LinearLayout loadinglayout;

    ImageView loadingview;

    config_hosts hosts = new config_hosts();

    String url = hosts.accountDetails;

    String FIRSTNAME="",LASTNAME="",EMAIL="",MOBILENO="";

    SharedPreferences.Editor editor;

    SharedPreferences.Editor fbToken;

    String TAG = "Mainactivity";

    private Handler handler;

    MenuItem seachItem;

    SearchView searchView;

    fragment_list searchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        onNewIntent(getIntent());


        dialog = new Dialog(this); // Context, this, etc.

        openDialog();

        setContentView(R.layout.activity_main);

        fbToken = getSharedPreferences("firebase",MODE_PRIVATE).edit();

         searchlist = new fragment_list();


        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);

                        fbToken.putString("fbstring",token);
                        fbToken.apply();
                    }
                });


        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        getDetails();

        SharedPreferences tokens = getSharedPreferences("firebase",MODE_PRIVATE);

        boolean isUpdated = tokens.getBoolean("isUpdated",false);

         if (tokens.getString("refreshed",null) != null && !isUpdated)
         {
             String token = tokens.getString("refreshed",null);
             sendRegistrationToServer(token);
         }


        if (getIntent().getStringExtra("orders") != null)
        {

            Fragment fragment = new orders();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.mainframeL,fragment,"orders");
            transaction.addToBackStack("home");
            transaction.commit();

            drawerLayout.closeDrawers();

            closeDialog();

        }else if (getIntent().getStringExtra("success") !=null)
        {

            Fragment succesfrag = new success("Thank you for purchasing with us");
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.mainframeL,succesfrag);
            transaction.commit();

            drawerLayout.closeDrawers();
            closeDialog();

        }else {

            Fragment mainfragment = new fragment_main();
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.mainframeL,mainfragment);
            transaction.commit();
            closeDialog();

        }


        dialog = new Dialog(this); // Context, this, etc.

        loadinglayout = findViewById(R.id.loadinglayout);

        loadingview = findViewById(R.id.loadingview);



        loadinglayout.getBackground().setAlpha(200);

        loadinglayout.setVisibility(View.VISIBLE);

        Glide.with(getApplicationContext()).asGif().load(R.drawable.loading).into(loadingview);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);



        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Engravedesigns");
        getSupportActionBar().setDisplayShowTitleEnabled(true);



        editor = getSharedPreferences("cookie",MODE_PRIVATE).edit();

        SharedPreferences preferences = getSharedPreferences("cookie",MODE_PRIVATE);

        String tok = preferences.getString("token","null");
        Boolean islogged = preferences.getBoolean("logged_in",false);

        if (tok == null && islogged == false)
        {
            ForceLoggout();
        }

        if (islogged == false)
        {
            ForceLoggout();
        }

        String user = preferences.getString("username",null);
        View headerView = navigationView.getHeaderView(0);
        usernameView = headerView.findViewById(R.id.usernameTv);


        Log.e("Greetings","Hello "+user + " This guy is "+ islogged);

        usernameView.setText("Hello, "+user);

        checkCoonection();

    }


    private void sendRegistrationToServer(String token) {

        String param = "gcm="+token;
        String URL = hosts.FIREBASE_SEND_TOKEN_URL;
        fbToken.putString("fbstring",token);
        fbToken.putBoolean("isUpdated",true);
        fbToken.apply();
        new syncAsyncTask(getApplicationContext(), "POST",URL,param, new jsonObjects() {
            @Override
            public void getObjects(String object) {
                Log.w("UPDATE_GCM to server",object);
            }
        }).execute();

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();

        if (extras != null)
        {
            String notify = extras.getString("product_id","null");

            Log.e("GCMalert",notify);

        }

    }


    public void openDialog() {


        dialog.setContentView(R.layout.dialog_demo);
        dialog.setCancelable(false);
        dialog.show();
    }



    public void closeDialog()
    {

        dialog.dismiss();
    }




    public boolean checkCoonection()
    {

        new syncInfo(getApplicationContext(), new info() {
            @Override
            public void getInfo(String data) {


                if (data.isEmpty())
                {
                   loadinglayout.setVisibility(View.INVISIBLE);
                   FragmentManager manager = getSupportFragmentManager();
                   FragmentTransaction transaction = manager.beginTransaction();
                   transaction.replace(R.id.mainframeL,new CONNECTION_FAILED());
                   transaction.addToBackStack(null);
                   transaction.commit();

                }else {

                    loadinglayout.setVisibility(View.INVISIBLE);
                }

            }
        }).execute();

        return true;
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.cart);

        seachItem = menu.findItem(R.id.seachIcon);

        View view = MenuItemCompat.getActionView(menuItem);

        final View sview = MenuItemCompat.getActionView(seachItem);

        searchView = (SearchView) seachItem.getActionView();

        setCartCounts(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });


        return true;

    }





    public void setCartCounts(final View menuview)
    {

        new syncInfo(getApplicationContext(), new info() {
            @Override
            public void getInfo(String data) {

                try {

                    JSONObject jsonObject = new JSONObject(data);

                    String items = jsonObject.getString("text_items");

                    notify  =  menuview.findViewById(R.id.notify_badge);

                    String value = notify.getText().toString();

                    if (items.matches("0"))
                    {
                        notify.setVisibility(View.GONE);
                    }else {

                        notify.setVisibility(View.VISIBLE);
                        notify.setText(items);
                    }


                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart) {

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.mainframeL,new cartFragment(),"cart");
            transaction.addToBackStack("cart");
            transaction.commit();

            return true;
        }else if (id == R.id.seachIcon)
        {

            menuSearch();

        }else if (id == R.id.home)
        {

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.mainframeL,new fragment_main());
            transaction.commit();
            return true;
        }else if (id == R.id.myaccount)
        {
            showMyaccountScreen();
        }

        return super.onOptionsItemSelected(item);
    }



    public boolean menuSearch()
    {
        startActivity(new Intent(MainActivity.this,searchProduct.class));
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Fragment fragment = new fragment_main();
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.mainframeL,fragment,"home");
            transaction.addToBackStack("home");
            transaction.commit();
        }
        else if (id == R.id.nav_wishlist) {
            Fragment fragment = new WishlistFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.mainframeL,fragment);
            transaction.addToBackStack("home");
            transaction.commit();
        } else if (id == R.id.logout) {
            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();


            clearFirebaseInstancecId();
            deleteInstanceIdFromServer();
            Intent intent = new Intent(getApplicationContext(),Activity_login.class);
            startActivity(intent);
            finish();

        }else if (id == R.id.nav_orders){

            Fragment fragment = new orders();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.mainframeL,fragment,"orders");
            transaction.addToBackStack("home");
            transaction.commit();
        }else if (id == R.id.nav_account){

            showMyaccountScreen();
        }
        else if (id == R.id.nav_currency){

            Intent intent = new Intent(getApplicationContext(),CurencySettings.class);
            startActivity(intent);

        }else if (id == R.id.nav_downloads){

            Intent intent = new Intent(getApplicationContext(),downloadsActity.class);
            startActivity(intent);

        }else if (id == R.id.nav_about)
        {
            showAboutUsScreen();
        }else if (id == R.id.nav_rate)
        {
            askRatings();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void showMyaccountScreen()
    {
        Intent intent = new Intent(getApplicationContext(),MyAccount.class);
        startActivity(intent);
        finish();
    }

    public void showAboutUsScreen()
    {
        Intent intent = new Intent(getApplicationContext(),AboutActivity.class);
        startActivity(intent);
    }



    public void clearFirebaseInstancecId()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void deleteInstanceIdFromServer()
    {
        String url = hosts.REMOVE_FIREBASE;

        SharedPreferences tokensSelector = getSharedPreferences("firebase",MODE_PRIVATE);

        String token = tokensSelector.getString("fbstring",null);

        String param = "token="+token;

        Log.e("ctoken",token);

        new syncAsyncTask(getApplicationContext(), "POST", url, param, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("removeToken",object);

                fbToken.clear();
                fbToken.putBoolean("isUpdated",false);
                fbToken.apply();

                SharedPreferences.Editor preferences = getSharedPreferences("cookie",MODE_PRIVATE).edit();
                SharedPreferences.Editor addrepref =   getSharedPreferences("addresses",MODE_PRIVATE).edit();

                preferences.putString("token","null");
                preferences.putBoolean("logged_in",false);
                preferences.apply();

                addrepref.putString("address_id",null);
                addrepref.apply();

            }
        }).execute();



    }



    public void ForceLoggout()
    {

        clearFirebaseInstancecId();
        deleteInstanceIdFromServer();
        Intent intent = new Intent(getApplicationContext(),Activity_login.class);
        startActivity(intent);
        finish();
    }


    public void getDetails()
    {

        new syncAsyncTask(getApplication(), "GET", url, null, new jsonObjects() {
            @Override
            public void getObjects(String object) {

                Log.e("ob",object);

                try {
                    JSONObject object1 = new JSONObject(object);
                    FIRSTNAME = object1.getString("firstname");
                    LASTNAME = object1.getString("lastname");
                    EMAIL = object1.getString("email");
                    MOBILENO = object1.getString("telephone");

                    editor.putString("firstname",FIRSTNAME);
                    editor.putString("lastname",LASTNAME);
                    editor.putString("username",FIRSTNAME);
                    editor.putString("email",EMAIL);
                    editor.putString("mobile",MOBILENO);
                    editor.apply();

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).execute();
    }




    @Override
    public boolean onSupportNavigateUp(){
        finish();

        return true;
    }



    public  void askRatings() {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+BuildConfig.APPLICATION_ID)));
        }catch (Exception e)
        {
            Log.e("askRatings",e.getMessage());

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID)));
        }
    }



}
