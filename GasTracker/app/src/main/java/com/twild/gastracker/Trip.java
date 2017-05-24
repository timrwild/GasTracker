package com.twild.gastracker;


public class Trip extends Record
{
    double endMileage;
    String notes;

    public Trip(int day, int month, int year, double mileage, double endMileage, String info)
    {
        super(day, month, year, mileage);
        this.endMileage = endMileage;
        this.notes = info;
    }

    public Trip()
    {

    }

    public double getEndMileage()
    {
        return endMileage;
    }

    public void setEndMileage(int endMileage)
    {
        this.endMileage = endMileage;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

}
