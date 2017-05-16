package com.twild.gastracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tim Wildauer on 15-May-17.
 */

public class AdapterCarList extends ArrayAdapter<String>
{
    ArrayList<String> arrayListCarNames;
    ArrayList<String> arrayListCarInfo;

    public AdapterCarList(Context context, ArrayList<String> carNames, ArrayList<String> carInfo)
    {
        super(context, R.layout.adapter_list_of_cars, carNames);
        arrayListCarNames = carNames;
        arrayListCarInfo = carInfo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater carListInflater = LayoutInflater.from(getContext());

        View carListView = carListInflater.inflate(R.layout.adapter_list_of_cars, parent, false);

        String carName = arrayListCarNames.get(position);
        String carInfo = arrayListCarInfo.get(position);

        TextView carNameText = (TextView) carListView.findViewById(R.id.text_box_car_name);
        TextView carInfoText = (TextView) carListView.findViewById(R.id.text_box_car_info);

        carNameText.setText(carName);
        carInfoText.setText(carInfo);

        return carListView;
    }

}
