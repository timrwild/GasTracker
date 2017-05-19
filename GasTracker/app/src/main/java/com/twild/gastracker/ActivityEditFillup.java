package com.twild.gastracker;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;

public class ActivityEditFillup extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fillup);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {

    }
}
