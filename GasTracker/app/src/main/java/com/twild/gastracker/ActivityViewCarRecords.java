package com.twild.gastracker;

import android.support.v4.app.FragmentActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import static com.twild.gastracker.ActivityListOfCars.carList;

public class ActivityViewCarRecords extends FragmentActivity
{

    Car currentCar;
    int currentCarPosition;
    int currentPageIndex;

    static final int DATA_CHANGED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_car_records);

        currentCarPosition = getIntent().getIntExtra("car_index", 0);
        currentPageIndex = getIntent().getIntExtra("page_index", 0);
        currentCar = carList.get(currentCarPosition);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(currentPageIndex);
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
                default: return ThirdFragment.newInstance("ThirdFragment, Default");
            }
        }

        @Override
        public int getCount()
        {
            return 3;
        }

        @Override
        public int getItemPosition(Object item)
        {
            return POSITION_NONE;
        }

    }

}

