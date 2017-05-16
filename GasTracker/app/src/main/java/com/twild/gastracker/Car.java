package com.twild.gastracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tim Wildauer on 15-May-17.
 */

public class Car
{
    CarInfo carInfo;
    List<Fillup> fillUpList;
    List<Maintenance> maintenanceList;
    List<Trip> tripList;

    public Car (String name, String make, String model, String year){
        this();
        carInfo = new CarInfo(name, make, model, year);
    }
    public Car()
    {
        fillUpList = new ArrayList<>();
        maintenanceList = new ArrayList<>();
        tripList = new ArrayList<>();
    }

    public void addFillup(int day, int month, int year, int mileage, double amount, double price, int full)
    {
        fillUpList.add(new Fillup(day, month, year, mileage, amount, price, full));
    }

    public void addMaintenance(int day, int month, int year, int mileage, String type, String info)
    {
        maintenanceList.add(new Maintenance(day, month, year, mileage, type, info));
    }

    public void addTrip(int day, int month, int year, int mileage, int endMileage, String info)
    {
        tripList.add(new Trip(day, month, year, mileage, endMileage, info));
    }

    public List<Fillup> getFillUpList()
    {
        return fillUpList;
    }

    public List<Maintenance> getMaintenanceList()
    {
        return maintenanceList;
    }

    public List<Trip> getTripList()
    {
        return tripList;
    }

    public CarInfo getCarInfo()
    {
        return carInfo;
    }

}
