package com.shopping.gway_4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;

import android.util.Log;
import java.util.ArrayList;

public class gallery extends AppCompatActivity {

    config_hosts hosts = new config_hosts();

    String[] code;

    String titles;

    Dialog dialog;

    RecyclerView recyclerView;
    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> filetitle = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        code = extras.getStringArray("code");

        titles = extras.getString("titles");

        Log.e("title",titles);

        titles = titles.replaceAll("\\[","").replaceAll("\\]","").replace(" ","");

        String[] titlarray = titles.split(",");

        for (int j=0; j < titlarray.length; j++)
        {
            filetitle.add(titlarray[j]);
        }


        for (int i=0; i < code.length; i++)
        {

            String img = code[i].replaceAll("\\[","").replaceAll("\\]","").replace(" ","");

            String split[] = img.split(",");

            for (int k=0; k<split.length; k++){

                images.add(split[k]);
            }

            Log.e("images",images.get(i));
        }

        setContentView(R.layout.activity_gallery);

        recyclerView = findViewById(R.id.previewrecyclerview);

        dialog = new Dialog(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Preview");

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));

        previews_adapter adapter = new previews_adapter(getApplicationContext(),images,filetitle);

        recyclerView.setAdapter(adapter);



    }


    public void openDialog() {

        dialog.setContentView(R.layout.dialog_demo);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.show();
    }



    public void closeDialog()
    {

        dialog.dismiss();
    }



    @Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }




}