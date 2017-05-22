package com.twild.gastracker;


public class Trip extends Record
{
    double endMileage;
    String info;

    public Trip(int day, int month, int year, double mileage, double endMileage, String info)
    {
        super(day, month, year, mileage);
        this.endMileage = endMileage;
        this.info = info;
    }

    public double getEndMileage()
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
