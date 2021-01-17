package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class MyAccount extends AppCompatActivity  {


    TabLayout tabLayout;
    SharedPreferences.Editor editor ;
    Dialog dialog;


    private  settingsTabAdapter settingsTabAdapter;
    private ViewPager mviewPager;
    boolean updated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new Dialog(MyAccount.this);


        editor = getSharedPreferences("addresses",MODE_PRIVATE).edit();

        tabLayout = findViewById(R.id.mytabs);

        mviewPager = findViewById(R.id.framelayouts);

        settingsTabAdapter = new settingsTabAdapter(getSupportFragmentManager());
        setUpViewpager(mviewPager);

        tabLayout.setupWithViewPager(mviewPager);

        boolean isUpdated = getIntent().getBooleanExtra("updated",false);

        updated = isUpdated;

        if (updated == true)
        {
            mviewPager.setCurrentItem(1);
        }

    }


     private void setUpViewpager(ViewPager viewpager)
     {

         settingsTabAdapter adapter = new settingsTabAdapter(getSupportFragmentManager());
         adapter.addFragment(new editAccountInfo(),"Account Info");
         adapter.addFragment(new AccountAddress(),"Address Book");
         adapter.addFragment(new changePasswords(),"Change Password");
         viewpager.setAdapter(adapter);
         adapter.notifyDataSetChanged();

     }


    public void alerts(String message)
    {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }else {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }





    @Override
    public boolean onSupportNavigateUp(){

        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
        return true;
    }



}