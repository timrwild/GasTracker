package com.twild.gastracker;


public class Fillup extends Record
{
    double amount;
    double price;
    int full;

    public Fillup(int day, int month, int year, double mileage, double amount, double price, int full)
    {
        super(day, month, year, mileage);
        this.amount = amount;
        this.price = price;
        this.full = full;
    }
    public Fillup()
    {

    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public int getFull()
    {
        return full;
    }

    public void setFull(int full)
    {
        this.full = full;
    }

}
