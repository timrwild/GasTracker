package com.twild.gastracker;


public class Maintenance extends Record
{
    String type, info;

    public Maintenance(int day, int month, int year, double mileage, String type, String info)
    {
        super(day, month, year, mileage);
        this.type = type;
        this.info = info;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
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
