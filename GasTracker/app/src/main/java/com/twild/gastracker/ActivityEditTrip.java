package com.twild.gastracker;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import static com.twild.gastracker.ActivityListOfCars.carList;
import static com.twild.gastracker.ActivityListOfCars.databaseReference;
import static com.twild.gastracker.ActivityListOfCars.userID;
import static com.twild.gastracker.ActivityViewCarRecords.DATA_CHANGED;

public class ActivityEditTrip extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{

    EditText editTextDate;
    EditText editTextMileage;
    EditText editTextEndMileage;
    EditText editTextNotes;

    int currentCarIndex;
    int currentTripIndex;
    List<Trip> tripList;
    Trip editedTrip;
    int year;
    int month;
    int displayMonth;
    int day;
    double mileage;
    double endMileage;
    String notes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        currentCarIndex = getIntent().getIntExtra("car_index", 0);
        currentTripIndex = getIntent().getIntExtra("trip_index", 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        tripList = carList.get(currentCarIndex).getTripList();
        editedTrip = tripList.get(currentTripIndex);

        year = editedTrip.getYear();
        month = editedTrip.getMonth();
        day = editedTrip.getDay();
        displayMonth = month + 1;

        DecimalFormat decimalFormatMileage = new DecimalFormat();
        decimalFormatMileage.setDecimalSeparatorAlwaysShown(false);

        mileage = editedTrip.getMileage();
        endMileage = editedTrip.getEndMileage();
        notes = editedTrip.getNotes();

        editTextDate = (EditText) findViewById(R.id.edit_text_edit_trip_date);
        editTextMileage = (EditText) findViewById(R.id.edit_text_edit_trip_mileage);
        editTextEndMileage = (EditText) findViewById(R.id.edit_text_edit_trip_end);
        editTextNotes = (EditText) findViewById(R.id.edit_text_edit_trip_notes);

        editTextDate.setText(displayMonth + "/" + day + "/" + year);
        editTextNotes.setText(notes);
        editTextMileage.setText(decimalFormatMileage.format(mileage).replace(",", ""));
        editTextEndMileage.setText(decimalFormatMileage.format(endMileage).replace(",", ""));

        Button buttonEditTrip = (Button) findViewById(R.id.button_edit_trip);
        buttonEditTrip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("mileage", "total mileage was " + 23);
                if (editTrip())
                {
                    setResult(DATA_CHANGED);
                    finish();
                }
            }
        });

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, ActivityEditTrip.this, year, month, day);
        editTextDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                datePickerDialog.show();
            }
        });
    }

    private boolean editTrip()
    {
        double mileage;
        double endMileage;

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
            Log.d("mileage", "total mileage was " + endMileage);

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

        carList.get(currentCarIndex).editTrip(currentTripIndex, day, month, year, mileage, endMileage, notesString);
        databaseReference.child(userID).child("" + currentCarIndex).child("tripList").setValue(tripList);
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        displayMonth = this.month + 1;
        editTextDate.setText(displayMonth + "/" + this.day + "/" + this.year);

    }

}
