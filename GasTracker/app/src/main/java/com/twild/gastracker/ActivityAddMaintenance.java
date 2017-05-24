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

public class ActivityAddMaintenance extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{

    int year;
    int month;
    int day;

    int currentCarIndex;
    List<Maintenance> maintenanceList;

    EditText editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        currentCarIndex = getIntent().getIntExtra("car_index", 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_maintenance);

        maintenanceList = carList.get(currentCarIndex).getMaintenanceList();

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, ActivityAddMaintenance.this, year, month, day);

        Button buttonAddMaintenance = (Button) findViewById(R.id.button_submit_maintenance);
        buttonAddMaintenance.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (submitMaintenance())
                {
                    setResult(DATA_CHANGED);
                    finish();
                }
            }
        });

        editTextDate = (EditText) findViewById(R.id.edit_text_add_maintenance_date);
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

    private boolean submitMaintenance()
    {
        double mileage;

        EditText editTextMileage = (EditText) findViewById(R.id.edit_text_add_maintenance_mileage);
        EditText editTextType = (EditText) findViewById(R.id.edit_text_add_maintenance_type);
        EditText editTextNotes = (EditText) findViewById(R.id.edit_text_add_maintenance_notes);

        String mileageString = editTextMileage.getText().toString();
        String type = editTextType.getText().toString();
        String notes = editTextNotes.getText().toString();

        if (mileageString.equals("") || (type.equals("") && notes.equals("")))
        {
            Toast.makeText(this, "Please enter all information", Toast.LENGTH_SHORT).show();
            return false;
        }

        mileage = Double.parseDouble(mileageString);

        carList.get(currentCarIndex).addMaintenance(day, month, year, mileage, type, notes);
        databaseReference.child(userID).child("" + currentCarIndex).child("maintenanceList").setValue(maintenanceList);
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
