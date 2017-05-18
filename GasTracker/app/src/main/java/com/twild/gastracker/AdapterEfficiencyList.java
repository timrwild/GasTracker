package com.twild.gastracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Tim Wildauer on 18-May-17.
 */

public class AdapterEfficiencyList extends ArrayAdapter<String> {

    ArrayList<String> arrayListDate;
    ArrayList<String> arrayListMileage;
    ArrayList<String> arrayListAmount;
    ArrayList<String> arrayListFull;
    ArrayList<String> arrayListPrice;
    ArrayList<String> arrayListMPG;

    public AdapterEfficiencyList(Context context, ArrayList<String> date, ArrayList<String> mileage,
                                 ArrayList<String> amount, ArrayList<String> full, ArrayList<String> price, ArrayList<String> mpg) {
        super(context, R.layout.adapter_efficiency_info);

        arrayListDate = date;
        arrayListMileage = mileage;
        arrayListAmount = amount;
        arrayListFull = full;
        arrayListPrice = price;
        arrayListMPG = mpg;

    }

    public AdapterEfficiencyList(Context context, int resource, ArrayList<String> date, ArrayList<String> mileage,
                                 ArrayList<String> amount, ArrayList<String> full, ArrayList<String> price, ArrayList<String> mpg) {
        super(context, resource, date);

        arrayListDate = date;
        arrayListMileage = mileage;
        arrayListAmount = amount;
        arrayListFull = full;
        arrayListPrice = price;
        arrayListMPG = mpg;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater efficiencyListInflater = LayoutInflater.from(getContext());

        View efficiencyListView = efficiencyListInflater.inflate(R.layout.adapter_efficiency_info, parent, false);

        //efficiencyListView = efficiencyListInflater.inflate(R.layout.adapter_efficiency_info, null);

        String date = arrayListDate.get(position);
        String mileage = arrayListMileage.get(position);
        String amount = arrayListAmount.get(position);
        String full;
        if (arrayListFull.get(position).equals("0"))
        {
            full = "";
        }
        else
        {
            full = "\u2611";
        }
        String price = arrayListPrice.get(position);
        String mpg = arrayListMPG.get(position);

        TextView textViewDate = (TextView) efficiencyListView.findViewById(R.id.data_table_date);
        TextView textViewMileage = (TextView) efficiencyListView.findViewById(R.id.data_table_mileage);
        TextView textViewAmount = (TextView) efficiencyListView.findViewById(R.id.data_table_amount);
        TextView textViewFull = (TextView) efficiencyListView.findViewById(R.id.data_table_full);
        TextView textViewPrice = (TextView) efficiencyListView.findViewById(R.id.data_table_price);
        TextView textViewMPG = (TextView) efficiencyListView.findViewById(R.id.data_table_mpg);

        textViewDate.setText(date);
        textViewMileage.setText(mileage);
        textViewAmount.setText(amount);
        textViewFull.setText(full);
        textViewPrice.setText(price);
        textViewMPG.setText(mpg);

        return efficiencyListView;
    }
}