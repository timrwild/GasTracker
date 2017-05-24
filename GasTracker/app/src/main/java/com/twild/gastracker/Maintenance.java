package com.twild.gastracker;


public class Maintenance extends Record
{
    String type, notes;

    public Maintenance(int day, int month, int year, double mileage, String type, String notes)
    {
        super(day, month, year, mileage);
        this.type = type;
        this.notes = notes;
    }

    public Maintenance()
    {

    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
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
