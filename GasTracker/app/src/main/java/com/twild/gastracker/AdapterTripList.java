package com.twild.gastracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;


class AdapterTripList extends ArrayAdapter<String> implements ListAdapter
{

    private ArrayList<String> arrayListDate;
    private ArrayList<String> arrayListMileage;
    private ArrayList<String> arrayListEndMileage;
    private ArrayList<String> arrayListTotalMileage;
    private ArrayList<String> arrayListNotes;

    public AdapterTripList(Context context, int resource, ArrayList<String> date, ArrayList<String> mileage,
                                  ArrayList<String> endMileage, ArrayList<String> totalMileage, ArrayList<String> notes)
    {

        super(context, resource, date);

        arrayListDate = date;
        arrayListMileage = mileage;
        arrayListEndMileage = endMileage;
        arrayListTotalMileage = totalMileage;
        arrayListNotes = notes;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater tripListInflater = LayoutInflater.from(getContext());

        View tripListView = tripListInflater.inflate(R.layout.adapter_trip_info, parent, false);

        String date = arrayListDate.get(position);
        String mileage = arrayListMileage.get(position);
        String endMileage = arrayListEndMileage.get(position);
        String totalMileage = arrayListTotalMileage.get(position);
        String notes = arrayListNotes.get(position);

        TextView textViewDate = (TextView) tripListView.findViewById(R.id.data_table_trip_date);
        TextView textViewMileage = (TextView) tripListView.findViewById(R.id.data_table_trip_begin_mileage);
        TextView textViewEndMileage = (TextView) tripListView.findViewById(R.id.data_table_trip_end_mileage);
        TextView textViewTotalMileage = (TextView) tripListView.findViewById(R.id.data_table_trip_total_mileage);
        TextView textViewNotes = (TextView) tripListView.findViewById(R.id.data_table_trip_notes);

        textViewDate.setText(date);
        textViewMileage.setText(mileage);
        textViewEndMileage.setText(endMileage);
        textViewTotalMileage.setText(totalMileage);
        textViewNotes.setText(notes);

        return tripListView;
    }

}
