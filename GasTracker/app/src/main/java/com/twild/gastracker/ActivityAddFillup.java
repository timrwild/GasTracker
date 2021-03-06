package com.twild.gastracker;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import static com.twild.gastracker.ActivityListOfCars.carList;
import static com.twild.gastracker.ActivityListOfCars.databaseReference;
import static com.twild.gastracker.ActivityListOfCars.userID;
import static com.twild.gastracker.ActivityViewCarRecords.DATA_CHANGED;

public class ActivityAddFillup extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{

    int year;
    int month;
    int day;

    int currentCarIndex;
    List<Fillup> fillupList;

    EditText editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        currentCarIndex = getIntent().getIntExtra("car_index", 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fillup);

        fillupList = carList.get(currentCarIndex).getFillUpList();

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, ActivityAddFillup.this, year, month, day);

        Button buttonAddFillup = (Button) findViewById(R.id.button_submit_fillup);
        buttonAddFillup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (submitFillup())
                {
                    setResult(DATA_CHANGED);
                    finish();
                }
            }
        });

        editTextDate = (EditText) findViewById(R.id.edit_text_add_fillup_date);
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

    private boolean submitFillup()
    {
        double mileage;
        double amount;
        double price;
        int full = 0;

        EditText editTextMileage = (EditText) findViewById(R.id.edit_text_add_fillup_mileage);
        EditText editTextAmount = (EditText) findViewById(R.id.edit_text_add_fillup_amount);
        EditText editTextPrice = (EditText) findViewById(R.id.edit_text_add_fillup_price);
        CheckBox checkBoxFull = (CheckBox) findViewById(R.id.checkbox_add_fillup_full);

        String mileageString = editTextMileage.getText().toString();
        String amountString = editTextAmount.getText().toString();
        String priceString = editTextPrice.getText().toString();

        if (mileageString.equals("") || amountString.equals(""))
        {
            Toast.makeText(this, "Please enter all information", Toast.LENGTH_SHORT).show();
            return false;
        }

        mileage = Double.parseDouble(mileageString);
        amount = Double.parseDouble(amountString);

        if (priceString.equals(""))
        {
            price = 0;
        }
        else
        {
            price = Double.parseDouble(priceString);
        }

        if (checkBoxFull.isChecked())
        {
            full = 1;
        }

        carList.get(currentCarIndex).addFillup(day, month, year, mileage, amount, price, full);
        databaseReference.child(userID).child("" + currentCarIndex).child("fillUpList").setValue(fillupList);
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
