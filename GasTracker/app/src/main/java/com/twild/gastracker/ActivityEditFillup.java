package com.twild.gastracker;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import static com.twild.gastracker.ActivityListOfCars.carList;
import static com.twild.gastracker.ActivityListOfCars.databaseReference;
import static com.twild.gastracker.ActivityListOfCars.userID;
import static com.twild.gastracker.ActivityViewCarRecords.DATA_CHANGED;

public class ActivityEditFillup extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{

    EditText editTextDate;
    EditText editTextMileage;
    EditText editTextAmount;
    EditText editTextPrice;
    CheckBox checkBoxFull;

    int currentCarIndex;
    int currentFillupIndex;
    List<Fillup> fillupList;
    Fillup editedFillup;
    int year;
    int month;
    int displayMonth;
    int day;
    double mileage;
    double amount;
    double price;
    int full;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        currentCarIndex = getIntent().getIntExtra("car_index", 0);
        currentFillupIndex = getIntent().getIntExtra("fillup_index", 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fillup);

        fillupList = carList.get(currentCarIndex).getFillUpList();
        editedFillup = fillupList.get(currentFillupIndex);

        year = editedFillup.getYear();
        month = editedFillup.getMonth();
        day = editedFillup.getDay();
        displayMonth = month + 1;

        DecimalFormat decimalFormatMileage = new DecimalFormat();
        decimalFormatMileage.setDecimalSeparatorAlwaysShown(false);

        mileage = editedFillup.getMileage();
        Log.d("mileage", "" + mileage);
        amount = editedFillup.getAmount();
        price = editedFillup.getPrice();
        full = editedFillup.getFull();

        editTextDate = (EditText) findViewById(R.id.edit_text_edit_fillup_date);
        editTextMileage = (EditText) findViewById(R.id.edit_text_edit_fillup_mileage);
        editTextAmount = (EditText) findViewById(R.id.edit_text_edit_fillup_amount);
        editTextPrice = (EditText) findViewById(R.id.edit_text_edit_fillup_price);
        checkBoxFull = (CheckBox) findViewById(R.id.checkbox_edit_fillup_full);

        String stringAmount = Double.toString(amount);
        String stringPrice = Double.toString(price);
        editTextDate.setText(displayMonth + "/" + day + "/" + year);
        editTextAmount.setText(stringAmount);
        editTextPrice.setText(stringPrice);
        checkBoxFull.setChecked(full == 1);
        editTextMileage.setText(decimalFormatMileage.format(mileage).replace(",", ""));

        Button buttonEditFillup = (Button) findViewById(R.id.button_edit_fillup);
        buttonEditFillup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (editFillup())
                {
                    setResult(DATA_CHANGED);
                    finish();
                }
            }
        });

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, ActivityEditFillup.this, year, month, day);
        editTextDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                datePickerDialog.show();
            }
        });
    }

    private boolean editFillup()
    {
        full = 0;

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

        carList.get(currentCarIndex).editFillup(currentFillupIndex, day, month, year, mileage, amount, price, full);
        databaseReference.child(userID).child("" + currentCarIndex).child("fillUpList").setValue(fillupList);

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
