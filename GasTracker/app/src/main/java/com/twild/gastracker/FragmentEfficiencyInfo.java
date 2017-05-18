package com.twild.gastracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.twild.gastracker.ActivityListOfCars.carList;

/**
 * Created by Tim Wildauer on 17-May-17.
 */

public class FragmentEfficiencyInfo extends Fragment {

    Car currentCar;
    int currentCarIndex;

    List<Fillup> currentCarFillup;
    ArrayList<String> fillupDate = new ArrayList<>();
    ArrayList<String> fillupMileage = new ArrayList<>();
    ArrayList<String> fillupAmount = new ArrayList<>();
    ArrayList<String> fillupFull = new ArrayList<>();
    ArrayList<String> fillupPrice = new ArrayList<>();
    ArrayList<String> fillupMPG = new ArrayList<>();

    ListAdapter carEfficiencyAdapter;
    ListView carEfficiencyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_fragment_efficiency_info, container, false);

        currentCarIndex = getArguments().getInt("current_car", 0);
        currentCar = carList.get(currentCarIndex);

        Toast.makeText(super.getContext(), "current_car" + currentCar.getCarInfo().getName(), Toast.LENGTH_SHORT).show();

        currentCarFillup = currentCar.getFillUpList();

        populateLists();

        carEfficiencyAdapter = new AdapterEfficiencyList(super.getContext(), R.layout.adapter_efficiency_info, fillupDate, fillupMileage, fillupAmount,
                fillupFull, fillupPrice, fillupMPG);
        carEfficiencyList = (ListView) v.findViewById(R.id.fillup_list_view);
        carEfficiencyList.setAdapter(carEfficiencyAdapter);


        return v;
    }

    private void populateLists()
    {
        fillupDate.clear();
        fillupMileage.clear();
        fillupAmount.clear();
        fillupFull.clear();
        fillupPrice.clear();
        fillupMPG.clear();

        int mileagePrevious = 0;
        float totalGas = 0;

        if (currentCarFillup != null) {
            for (Fillup fillup : currentCarFillup) {

                int tempDateDay = fillup.getDay();
                int tempDateMonth = fillup.getMonth();
                int tempDateYear = fillup.getYear();

                int tempMileage = fillup.getMileage();
                double tempAmount = fillup.getAmount();
                int tempFull = fillup.getFull();
                float tempMPG = 0;


                totalGas += tempAmount;
                if (tempFull == 1)
                {
                    if (mileagePrevious != 0)
                    {
                        tempMPG = (tempMileage - mileagePrevious) / totalGas;
                        totalGas = 0;
                    }
                    mileagePrevious = tempMileage;
                }

                fillupDate.add(tempDateDay + "/" + tempDateMonth + "/" + tempDateYear);
                fillupMileage.add(NumberFormat.getNumberInstance(Locale.US).format(fillup.getMileage()));
                fillupAmount.add(String.valueOf(fillup.getAmount()));
                fillupFull.add(String.valueOf(fillup.getFull()));
                fillupPrice.add(String.valueOf(fillup.getPrice()));
                fillupMPG.add(String.format("%.2f", tempMPG));
            }
        }
    }

    public static FragmentEfficiencyInfo newInstance(int currentCar) {

        FragmentEfficiencyInfo f = new FragmentEfficiencyInfo();
        Bundle b = new Bundle();
        b.putInt("current_car", currentCar);

        f.setArguments(b);

        return f;
    }
}
