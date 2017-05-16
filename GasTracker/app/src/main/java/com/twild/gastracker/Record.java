package com.twild.gastracker;

/**
 * Created by timwildauer on 1/31/17.
 */

public class Record
{
    public int day, month, year, mileage;


    Record(int day, int month, int year, int mileage)
    {
        this.day = day;
        this.month = month;
        this.year = year;
        this.mileage = mileage;
    }

    Record()
    {

    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

}
