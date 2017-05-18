package com.twild.gastracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import static com.twild.gastracker.ActivityListOfCars.carList;

/**
 * Created by Tim Wildauer on 17-May-17.
 */

public class SecondFragment extends Fragment {

    Car currentCar;
    int currentCarIndex;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.second_frag, container, false);

        currentCarIndex = getArguments().getInt("current_car", 0);
        currentCar = carList.get(currentCarIndex);

        Toast.makeText(super.getContext(), "current_car " + currentCar.getCarInfo().getName() + " maintenance", Toast.LENGTH_SHORT).show();

        return v;
    }

    public static SecondFragment newInstance(int currentCar) {

        SecondFragment f = new SecondFragment();
        Bundle b = new Bundle();
        b.putInt("current_car", currentCar);

        f.setArguments(b);

        return f;
    }
}
