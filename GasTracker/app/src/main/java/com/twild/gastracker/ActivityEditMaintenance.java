package com.twild.gastracker;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import static com.twild.gastracker.ActivityAddMaintenance.types;
import static com.twild.gastracker.ActivityListOfCars.carList;
import static com.twild.gastracker.ActivityListOfCars.databaseReference;
import static com.twild.gastracker.ActivityListOfCars.userID;
import static com.twild.gastracker.ActivityViewCarRecords.DATA_CHANGED;

public class ActivityEditMaintenance extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{

    EditText editTextDate;
    EditText editTextMileage;
    AutoCompleteTextView autoCompleteTextViewType;
    EditText editTextNotes;

    int currentCarIndex;
    int currentMaintenanceIndex;
    List<Maintenance> maintenanceList;
    Maintenance editedMaintenance;
    int year;
    int month;
    int displayMonth;
    int day;
    double mileage;
    String type;
    String notes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        currentCarIndex = getIntent().getIntExtra("car_index", 0);
        currentMaintenanceIndex = getIntent().getIntExtra("maintenance_index", 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_maintenance);

        maintenanceList = carList.get(currentCarIndex).getMaintenanceList();
        editedMaintenance = maintenanceList.get(currentMaintenanceIndex);

        year = editedMaintenance.getYear();
        month = editedMaintenance.getMonth();
        day = editedMaintenance.getDay();
        displayMonth = month + 1;

        DecimalFormat decimalFormatMileage = new DecimalFormat();
        decimalFormatMileage.setDecimalSeparatorAlwaysShown(false);

        mileage = editedMaintenance.getMileage();
        type = editedMaintenance.getType();
        notes = editedMaintenance.getNotes();

        editTextDate = (EditText) findViewById(R.id.edit_text_edit_maintenance_date);
        editTextMileage = (EditText) findViewById(R.id.edit_text_edit_maintenance_mileage);
        autoCompleteTextViewType = (AutoCompleteTextView) findViewById(R.id.edit_text_edit_maintenance_type);
        editTextNotes = (EditText) findViewById(R.id.edit_text_edit_maintenance_notes);

        editTextDate.setText(displayMonth + "/" + day + "/" + year);
        autoCompleteTextViewType.setText(type);
        editTextNotes.setText(notes);
        editTextMileage.setText(decimalFormatMileage.format(mileage).replace(",", ""));

        Button buttonEditFillup = (Button) findViewById(R.id.button_edit_maintenance);
        buttonEditFillup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (editMaintenance())
                {
                    setResult(DATA_CHANGED);
                    finish();
                }
            }
        });

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, ActivityEditMaintenance.this, year, month, day);
        editTextDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                datePickerDialog.show();
            }
        });
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, types);
        autoCompleteTextViewType.setAdapter(typeAdapter);
        autoCompleteTextViewType.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    autoCompleteTextViewType.showDropDown();
                }
                else
                {
                    autoCompleteTextViewType.dismissDropDown();
                }
            }
        });
        autoCompleteTextViewType.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                autoCompleteTextViewType.showDropDown();
                return false;
            }
        });

    }

    private boolean editMaintenance()
    {
        double mileage;

        EditText editTextMileage = (EditText) findViewById(R.id.edit_text_edit_maintenance_mileage);
        EditText editTextType = (EditText) findViewById(R.id.edit_text_edit_maintenance_type);
        EditText editTextNotes = (EditText) findViewById(R.id.edit_text_edit_maintenance_notes);

        String mileageString = editTextMileage.getText().toString();
        String type = editTextType.getText().toString();
        String notes = editTextNotes.getText().toString();

        if (mileageString.equals("") || (type.equals("") && notes.equals("")))
        {
            Toast.makeText(this, "Please enter all information", Toast.LENGTH_SHORT).show();
            return false;
        }

        mileage = Double.parseDouble(mileageString);

        carList.get(currentCarIndex).editMaintenance(currentMaintenanceIndex, day, month, year, mileage, type, notes);
        databaseReference.child(userID).child("" + currentCarIndex).child("maintenanceList").setValue(maintenanceList);
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
