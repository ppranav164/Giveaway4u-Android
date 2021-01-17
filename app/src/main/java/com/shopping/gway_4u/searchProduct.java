package com.shopping.gway_4u;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;

public class searchProduct extends AppCompatActivity {


    private SearchView searchView;
    private MenuItem menuItem;
    private FrameLayout frameLayout;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        frameLayout = findViewById(R.id.searchlayout);

        title = getIntent().getStringExtra("tag");

        View searchv = getLayoutInflater().inflate(R.layout.search_edittext,null);


        searchView = searchv.findViewById(R.id.searchview_inps);

        searchView.setIconifiedByDefault(false);

        searchView.onActionViewExpanded();

        actionBar.setCustomView(searchv);

        if (getIntent().getStringExtra("tag") != null)
        {
            searchView.setQuery(title,true);
            searchView.clearFocus();
            searchSubmit(title);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchSubmit(query);
                searchView.clearFocus();
                Log.e("onQueryTextSubmit",query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("onQueryTextChange",newText);
                return false;
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }


    public void searchSubmit(String query)
    {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.searchlayout,new searchContents(query,searchProduct.this));
        transaction.commit();
    }


    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }
}