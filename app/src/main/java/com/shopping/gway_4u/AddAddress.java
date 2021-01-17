package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class AddAddress extends AppCompatActivity {

    boolean isEdit = false;
    String address_ID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra("isEdit",false))
        {
            isEdit = getIntent().getBooleanExtra("isEdit",false);
            address_ID = getIntent().getStringExtra("address_id");
        }


        setContentView(R.layout.activity_add_address);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = new Bundle();

        bundle.putBoolean("isEdit",isEdit);
        bundle.putString("address_id",address_ID);

        Fragment fragment = new accounts_editDelivery();

        fragment.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainContainer,fragment);
        transaction.commit();


    }



    @Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }

}