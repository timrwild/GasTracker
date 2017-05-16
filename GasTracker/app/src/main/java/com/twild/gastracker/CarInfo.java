package com.twild.gastracker;

/**
 * Created by timwildauer on 1/31/17.
 */

public class CarInfo
{
    public String name;
    public String make;
    public String model;
    public String year;

    public CarInfo(String name, String make, String model, String year)
    {
        this.name = name;
        this.make = make;
        this.model = model;
        this.year = year;
    }
    CarInfo()
    {

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make)
    {
        this.make = make;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

}
