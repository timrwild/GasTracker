package com.twild.gastracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.twild.gastracker.ActivityListOfCars.carList;
import static com.twild.gastracker.ActivityListOfCars.databaseReference;
import static com.twild.gastracker.ActivityListOfCars.userID;
import static com.twild.gastracker.ActivityViewCarRecords.DATA_CHANGED;


public class FragmentMaintenanceInfo extends Fragment {

    Car currentCar;
    int currentCarIndex;
    List<Maintenance> maintenanceList;

    ArrayList<String> maintenanceDate = new ArrayList<>();
    ArrayList<String> maintenanceMileage = new ArrayList<>();
    ArrayList<String> maintenanceType = new ArrayList<>();
    ArrayList<String> maintenanceNotes = new ArrayList<>();


    ListAdapter listAdapterMaintenance;
    ListView listViewMaintenance;

    Button buttonAddMaintenance;

    int contextMenuMaintenancePosition;

    final int RESULT_OK = 1;

    public static FragmentMaintenanceInfo newInstance(int currentCar)
    {

        FragmentMaintenanceInfo fragmentMaintenanceInfo = new FragmentMaintenanceInfo();
        Bundle bundleMaintenanceInfo = new Bundle();
        bundleMaintenanceInfo.putInt("current_car", currentCar);

        fragmentMaintenanceInfo.setArguments(bundleMaintenanceInfo);

        return fragmentMaintenanceInfo;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View viewMaintenanceInfo = inflater.inflate(R.layout.layout_fragment_maintenance_info, container, false);

        currentCarIndex = getArguments().getInt("current_car", 0);
        currentCar = carList.get(currentCarIndex);
        maintenanceList = currentCar.getMaintenanceList();

        populateMaintenanceLists();

        buttonAddMaintenance = (Button) viewMaintenanceInfo.findViewById(R.id.button_add_maintenance);
        buttonAddMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToAddMaintenance();
            }
        });

        listAdapterMaintenance = new AdapterMaintenanceList(super.getContext(), R.layout.adapter_maintenance_info,
                maintenanceDate, maintenanceMileage, maintenanceType, maintenanceNotes);
        listViewMaintenance = (ListView) viewMaintenanceInfo.findViewById(R.id.maintenance_list_view);
        listViewMaintenance.setAdapter(listAdapterMaintenance);
        registerForContextMenu(listViewMaintenance);

        return viewMaintenanceInfo;
    }

    private void moveToAddMaintenance()
    {
        Intent moveToAddMaintenance = new Intent(super.getContext(), ActivityAddMaintenance.class);
        moveToAddMaintenance.putExtra("car_index", currentCarIndex);
        startActivityForResult(moveToAddMaintenance, 0);
    }

    private void populateMaintenanceLists()
    {
        maintenanceDate.clear();
        maintenanceMileage.clear();
        maintenanceType.clear();
        maintenanceNotes.clear();

        if (maintenanceList != null)
        {
            for (Maintenance maintenance : maintenanceList)
            {

                int tempDateDay = maintenance.getDay();
                int tempDateMonth = maintenance.getMonth() + 1;
                int tempDateYear = maintenance.getYear();

                double tempMileage = maintenance.getMileage();
                String tempType = maintenance.getType();
                String tempNotes = maintenance.getNotes();

                String stringYearSubstring = Integer.toString(tempDateYear).substring(2);
                maintenanceDate.add(tempDateMonth + "/" + tempDateDay + "/" + stringYearSubstring);
                maintenanceMileage.add(NumberFormat.getNumberInstance(Locale.US).format((int) tempMileage));
                maintenanceType.add(tempType);
                if (!tempNotes.equals(""))
                {
                    if (tempNotes.length() > 12)
                    {
                        maintenanceNotes.add(tempNotes.substring(0, 12) + "...");
                    }
                    else
                    {
                        maintenanceNotes.add(tempNotes);
                    }
                }

            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (view.getId() == R.id.maintenance_list_view)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(maintenanceDate.get(info.position) + " " + maintenanceType.get(info.position));
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
            contextMenuMaintenancePosition = info.position;

            if (menuItemIndex == 0)
            {
                Intent editMaintenance = new Intent(getActivity(), ActivityEditMaintenance.class);
                editMaintenance.putExtra("car_index", currentCarIndex);
                editMaintenance.putExtra("maintenance_index", contextMenuMaintenancePosition);
                startActivityForResult(editMaintenance, RESULT_OK);
            } else if (menuItemIndex == 1)
            {
                carList.get(currentCarIndex).maintenanceList.remove(contextMenuMaintenancePosition);
                databaseReference.child(userID).child("" + currentCarIndex).child("maintenanceInfo").setValue(maintenanceList);

                populateMaintenanceLists();
                listViewMaintenance.setAdapter(listAdapterMaintenance);
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
            populateMaintenanceLists();
            listViewMaintenance.setAdapter(listAdapterMaintenance);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
