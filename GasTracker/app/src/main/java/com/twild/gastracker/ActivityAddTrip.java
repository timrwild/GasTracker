package com.twild.gastracker;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import static com.twild.gastracker.ActivityListOfCars.carList;
import static com.twild.gastracker.ActivityListOfCars.databaseReference;
import static com.twild.gastracker.ActivityListOfCars.userID;
import static com.twild.gastracker.ActivityViewCarRecords.DATA_CHANGED;

public class ActivityAddTrip extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{

    int year;
    int month;
    int day;

    int currentCarIndex;
    List<Trip> tripList;

    EditText editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        currentCarIndex = getIntent().getIntExtra("car_index", 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        tripList = carList.get(currentCarIndex).getTripList();

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, ActivityAddTrip.this, year, month, day);

        Button buttonAddTrip = (Button) findViewById(R.id.button_submit_trip);
        buttonAddTrip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (submitTrip())
                {
                    setResult(DATA_CHANGED);
                    finish();
                }
            }
        });

        editTextDate = (EditText) findViewById(R.id.edit_text_add_trip_date);
        editTextDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                datePickerDialog.show();
            }
        });
        setDateText();
    }

    private void setDateText()
    {
        int displayMonth = month + 1;
        editTextDate.setText(displayMonth + "/" + day + "/" + year);
    }

    private boolean submitTrip()
    {
        double mileage;
        double endMileage;

        EditText editTextMileage = (EditText) findViewById(R.id.edit_text_add_trip_mileage);
        EditText editTextEndMileage = (EditText) findViewById(R.id.edit_text_add_trip_end);
        EditText editTextNotes = (EditText) findViewById(R.id.edit_text_add_trip_notes);

        String mileageString = editTextMileage.getText().toString();
        String endMileageString = editTextEndMileage.getText().toString();
        String notesString = editTextNotes.getText().toString();

        if (mileageString.equals(""))
        {
            Toast.makeText(this, "Please enter all information", Toast.LENGTH_SHORT).show();
            return false;
        }

        mileage = Double.parseDouble(mileageString);
        if (!endMileageString.equals(""))
        {
            endMileage = Double.parseDouble(endMileageString);
            if (endMileage - mileage < 0)
            {
                Toast.makeText(this, "Ending mileage must be greater than starting mileage", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else
        {
            endMileage = 0;
        }

        carList.get(currentCarIndex).addTrip(day, month, year, mileage, endMileage, notesString);
        databaseReference.child(userID).child("" + currentCarIndex).child("tripList").setValue(tripList);
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        setDateText();
    }

}
