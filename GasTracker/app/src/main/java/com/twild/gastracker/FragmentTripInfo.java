package com.twild.gastracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.twild.gastracker.ActivityListOfCars.carList;
import static com.twild.gastracker.ActivityListOfCars.databaseReference;
import static com.twild.gastracker.ActivityListOfCars.userID;
import static com.twild.gastracker.ActivityViewCarRecords.DATA_CHANGED;


public class FragmentTripInfo extends Fragment {

    Car currentCar;
    int currentCarIndex;
    List<Trip> tripList;

    ArrayList<String> tripDate = new ArrayList<>();
    ArrayList<String> tripMileage = new ArrayList<>();
    ArrayList<String> tripEndMileage = new ArrayList<>();
    ArrayList<String> tripTotalMileage = new ArrayList<>();
    ArrayList<String> tripNotes = new ArrayList<>();


    ListAdapter listAdapterTrip;
    ListView listViewTrip;

    Button buttonAddTrip;

    int contextMenuTripPosition;

    final int RESULT_OK = 1;

    public static FragmentTripInfo newInstance(int currentCar)
    {

        FragmentTripInfo fragmentTripInfo = new FragmentTripInfo();
        Bundle bundleTripInfo = new Bundle();
        bundleTripInfo.putInt("current_car", currentCar);

        fragmentTripInfo.setArguments(bundleTripInfo);

        return fragmentTripInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View viewTripInfo = inflater.inflate(R.layout.layout_fragment_trip_info, container, false);

        currentCarIndex = getArguments().getInt("current_car", 0);
        currentCar = carList.get(currentCarIndex);
        tripList = currentCar.getTripList();

        populateTripLists();

        buttonAddTrip = (Button) viewTripInfo.findViewById(R.id.button_add_trip);
        buttonAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToAddTrip();
            }
        });

        listAdapterTrip = new AdapterTripList(super.getContext(), R.layout.adapter_trip_info,
                tripDate, tripMileage, tripEndMileage, tripTotalMileage, tripNotes);
        listViewTrip = (ListView) viewTripInfo.findViewById(R.id.trip_list_view);
        listViewTrip.setAdapter(listAdapterTrip);
        registerForContextMenu(listViewTrip);

        return viewTripInfo;
    }

    private void moveToAddTrip()
    {
        Intent moveToAddTrip = new Intent(super.getContext(), ActivityAddTrip.class);
        moveToAddTrip.putExtra("car_index", currentCarIndex);
        startActivityForResult(moveToAddTrip, 0);
    }

    private void populateTripLists()
    {
        tripDate.clear();
        tripMileage.clear();
        tripEndMileage.clear();
        tripTotalMileage.clear();
        tripNotes.clear();

        if (tripList != null)
        {
            for (Trip trip : tripList)
            {

                int tempDateDay = trip.getDay();
                int tempDateMonth = trip.getMonth() + 1;
                int tempDateYear = trip.getYear();

                double tempMileage = trip.getMileage();
                double tempEndMileage = trip.getEndMileage();
                double tempTotalMileage = tempEndMileage - tempMileage;
                Log.d("mileage", "total mileage was " + tempEndMileage);
                String tempNotes = trip.getNotes();

                String stringYearSubstring = Integer.toString(tempDateYear).substring(2);
                tripDate.add(tempDateMonth + "/" + tempDateDay + "/" + stringYearSubstring);
                tripMileage.add(NumberFormat.getNumberInstance(Locale.US).format(tempMileage));

                if (tempEndMileage != 0)
                {
                    tripEndMileage.add(NumberFormat.getNumberInstance(Locale.US).format(tempEndMileage));
                    tripTotalMileage.add(NumberFormat.getNumberInstance(Locale.US).format(tempTotalMileage));
                }
                else
                {
                    tripEndMileage.add("");
                    tripTotalMileage.add("");
                }

                if (tempNotes.length() > 10)
                {
                    tripNotes.add(tempNotes.substring(0, 10) + "...");
                }
                else
                {
                    tripNotes.add(tempNotes);
                }

            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (view.getId() == R.id.trip_list_view)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(tripDate.get(info.position) + " " + tripMileage.get(info.position));
            menu.add(Menu.NONE, 0, 0, "Edit");
            menu.add(Menu.NONE, 1, 1, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if (getUserVisibleHint())
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int menuItemIndex = item.getItemId();
            contextMenuTripPosition = info.position;

            if (menuItemIndex == 0)
            {
                Intent editTrip = new Intent(getActivity(), ActivityEditTrip.class);
                editTrip.putExtra("car_index", currentCarIndex);
                editTrip.putExtra("trip_index", contextMenuTripPosition);
                startActivityForResult(editTrip, RESULT_OK);
            }
            else if (menuItemIndex == 1)
            {
                carList.get(currentCarIndex).tripList.remove(contextMenuTripPosition);
                databaseReference.child(userID).child("" + currentCarIndex).child("tripList").setValue(tripList);

                populateTripLists();
                listViewTrip.setAdapter(listAdapterTrip);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == DATA_CHANGED)
        {
            populateTripLists();
            listViewTrip.setAdapter(listAdapterTrip);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
