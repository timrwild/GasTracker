package com.twild.gastracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tim Wildauer on 23-May-17.
 */

class AdapterMaintenanceList extends ArrayAdapter<String> implements ListAdapter {

    private ArrayList<String> arrayListDate;
    private ArrayList<String> arrayListMileage;
    private ArrayList<String> arrayListType;
    private ArrayList<String> arrayListNotes;

    public AdapterMaintenanceList(Context context, int resource, ArrayList<String> date, ArrayList<String> mileage,
                                  ArrayList<String> type, ArrayList<String> notes)
    {

        super(context, resource, date);

        arrayListDate = date;
        arrayListMileage = mileage;
        arrayListType = type;
        arrayListNotes = notes;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater maintenanceListInflater = LayoutInflater.from(getContext());

        View maintenanceListView = maintenanceListInflater.inflate(R.layout.adapter_maintenance_info, parent, false);

        String date = arrayListDate.get(position);
        String mileage = arrayListMileage.get(position);
        String type = arrayListType.get(position);
        String notes = arrayListNotes.get(position);

        TextView textViewDate = (TextView) maintenanceListView.findViewById(R.id.data_table_maintenance_date);
        TextView textViewMileage = (TextView) maintenanceListView.findViewById(R.id.data_table_maintenance_mileage);
        TextView textViewType = (TextView) maintenanceListView.findViewById(R.id.data_table_maintenance_type);
        TextView textViewNotes = (TextView) maintenanceListView.findViewById(R.id.data_table_maintenance_notes);

        textViewDate.setText(date);
        textViewMileage.setText(mileage);
        textViewType.setText(type);
        textViewNotes.setText(notes);

        return maintenanceListView;
    }
}
