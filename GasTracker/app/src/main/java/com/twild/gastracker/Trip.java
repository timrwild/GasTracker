package com.twild.gastracker;

/**
 * Created by timwildauer on 1/31/17.
 */

public class Trip extends Record
{
    int endMileage;
    String info;

    public Trip(int day, int month, int year, int mileage, int endMileage, String info)
    {
        super(day, month, year, mileage);
        this.endMileage = endMileage;
        this.info = info;
    }

    public int getEndMileage()
    {
        return endMileage;
    }

    public void setEndMileage(int endMileage)
    {
        this.endMileage = endMileage;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

}
