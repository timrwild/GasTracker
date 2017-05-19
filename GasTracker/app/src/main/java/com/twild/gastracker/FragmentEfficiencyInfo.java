package com.twild.gastracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.twild.gastracker.ActivityListOfCars.carList;

public class FragmentEfficiencyInfo extends Fragment
{

    /*
     * It seems that each fragment works roughly the same as an activity (except for when you're
     * trying to display anything on the screen, in which case it seems to drop an A-bomb on everything
     * you might know about setting layouts). As such, we need to define all the variables we're going
     * to use inside the activity. We want the current car so we can access the information, as well as
     * where that car is in the index, so we can add/edit information at that index. Next, we want the
     * list of fillups, as well as arraylists for every attribute we want to display to the user.
     * Last, but certainly not least, we want to name the listadapter and listview that we want to
     * display the information to and with.
     */

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

    Button buttonAddFillup;

    public static FragmentEfficiencyInfo newInstance(int currentCar)
    {

        /*
         * The first thing we need to do, is set a new instance of the fragment. This is what makes
         * it different from an activity. We have to take information from the actual activity
         * and then hand it to the fragment in a bundle. We take that information, wrap it up,
         * and return it in the form of an object, namely this class.
         * When the actual activity decides that it needs to make an instance of this fragment,
         * it will "create" this object and send it some information, namely the index of the
         * car we're trying to get the information for. When we create this instance, the one input
         * is the index at which the car is stored. We take that index, put it into a bundle,
         * and send that bundle of "stuff" to the onCreate method. That's the only way for us to
         * get information from the actual activity to this fragment.
         */

        FragmentEfficiencyInfo fragmentEfficiencyInfo = new FragmentEfficiencyInfo();
        Bundle bundleEfficiencyInfo = new Bundle();
        bundleEfficiencyInfo.putInt("current_car", currentCar);

        fragmentEfficiencyInfo.setArguments(bundleEfficiencyInfo);

        return fragmentEfficiencyInfo;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        /*
         * When we create the fragment, we're actually creating a view that the main activity is
         * throwing into the placeholder. As inputs, we take the R.layout that we want to show up,
         * then take the container, which is the space that we're going to fill with the fragment.
         * Last but not least, we take the bundle of "stuff" that we first started with. In
         * this case, all we need is the index of the vehicle with the information we're trying
         * to display.
         */

        View viewEfficiencyInfo = inflater.inflate(R.layout.layout_fragment_fillup_info, container, false);

        currentCarIndex = getArguments().getInt("current_car", 0);
        currentCar = carList.get(currentCarIndex);

        /*
         * Once we have that car (note that we're just copying it to this fragment, not editing
         * the actual car from the static list), we need to get the list of fillups. After that,
         * we take those fillups and populate all the arrays of stuff that we want to display.
         */

        currentCarFillup = currentCar.getFillUpList();

        populateLists();

        buttonAddFillup = (Button) viewEfficiencyInfo.findViewById(R.id.button_add_fillup);

        buttonAddFillup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moveToAddFillup();
            }
        });

        /*
         * Finally, we need to create the custom list adapter. This is where it gets complicated.
         * First of all, we need to give the adapter some context (i.e. where should it put this
         * list when it's done? At least that's what I think it's for). After that, we need to
         * tell it what layout to look for the listview inside of. Lastly, we need to send it
         * arraylists of all the information that we want to put inside the adapter.
         * After we create the adapter, we lookup the actual listview, set the adapter and
         * return the fully constructed view.
         */

        carEfficiencyAdapter = new AdapterFillupList(super.getContext(), R.layout.adapter_fillup_info, fillupDate, fillupMileage, fillupAmount,
                fillupFull, fillupPrice, fillupMPG);
        carEfficiencyList = (ListView) viewEfficiencyInfo.findViewById(R.id.fillup_list_view);
        carEfficiencyList.setAdapter(carEfficiencyAdapter);


        return viewEfficiencyInfo;
    }

    private void moveToAddFillup()
    {
        Intent moveToAddFillup = new Intent(super.getContext(), ActivityAddFillup.class);
        startActivity(moveToAddFillup);
    }

    private void populateLists()
    {

        /*
         * This method is fairly straightforward. We first clear all the lists of data.
         * After that, we run through all the instances of a fillup, and throw that information
         * into the individual arraylists while calculating the MPG on the fly. This assumes
         * the fillups are sorted by ascending mileage.
         */

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
                    }
                    totalGas = 0;
                    mileagePrevious = tempMileage;
                }

                fillupDate.add(tempDateMonth + "/" + tempDateDay + "/" + tempDateYear);
                fillupMileage.add(NumberFormat.getNumberInstance(Locale.US).format(fillup.getMileage()));
                fillupAmount.add(String.valueOf(fillup.getAmount()));
                fillupFull.add(String.valueOf(fillup.getFull()));
                fillupPrice.add(String.valueOf(fillup.getPrice()));
                fillupMPG.add(String.format("%.2f", tempMPG));
            }
        }
    }

}
