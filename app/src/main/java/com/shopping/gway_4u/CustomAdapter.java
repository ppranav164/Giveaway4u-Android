package com.shopping.gway_4u;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<list_totals> {


    public  ArrayList<list_totals> items;

    EditText editText;

    Button copuonApply;


    LinearLayout linearLayout;

    View copuonView;



    public CustomAdapter(Context context, int resource , ArrayList<list_totals> totals) {
        super(context, resource , totals );

        this.items = totals;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View v = convertView;

        list_totals m = items.get(position);


        if (m != null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listviews, null);

            TextView tv = v.findViewById(R.id.title_tv);

            TextView tv1 = v.findViewById(R.id.text_tv);



            tv.setText(m.title);
            tv1.setText(m.text);


        }

        return v;
    }
}
