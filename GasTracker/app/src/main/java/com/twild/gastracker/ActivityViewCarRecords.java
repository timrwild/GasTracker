package com.twild.gastracker;

import android.support.v4.app.FragmentActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;

import static com.twild.gastracker.ActivityListOfCars.carList;

public class ActivityViewCarRecords extends FragmentActivity
{

    Car currentCar;
    int currentCarPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_car_records);

        currentCarPosition = getIntent().getIntExtra("car_index", 0);
        currentCar = carList.get(currentCarPosition);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    private class MyPagerAdapter extends FragmentPagerAdapter
    {

        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos)
        {
            switch(pos)
            {

                case 0: return FragmentFillupInfo.newInstance(currentCarPosition);
                case 1: return SecondFragment.newInstance(currentCarPosition);
                case 2: return ThirdFragment.newInstance("ThirdFragment, Instance 1");
                case 3: return ThirdFragment.newInstance("ThirdFragment, Instance 2");
                case 4: return ThirdFragment.newInstance("ThirdFragment, Instance 3");
                default: return ThirdFragment.newInstance("ThirdFragment, Default");
            }
        }

        @Override
        public int getCount()
        {
            return 5;
        }
    }

    /*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {

        Log.d("longPress", "the user longClicked");

        if (view.getId() == R.id.fillup_list_view)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("Edit Info");
            menu.add(Menu.NONE, 0, 0, "Edit");
            menu.add(Menu.NONE, 1, 1, "Delete");

            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_activity_view_car_records, menu);
        }
    }

    */
}

