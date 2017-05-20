package com.twild.gastracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.twild.gastracker.ActivityListOfCars.carList;

public class ActivityEditFillup extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{

    private Button buttonEditFillup;
    EditText editTextDate;
    EditText editTextMileage;
    EditText editTextAmount;
    EditText editTextPrice;
    CheckBox checkBoxFull;

    private FirebaseAuth firebaseAuth;
    FirebaseUser user;

    private DatabaseReference databaseReference;
    private String userID;

    int currentCarIndex;
    int currentFillupIndex;
    Fillup editedFillup;
    int year;
    int month;
    int day;
    int mileage;
    double amount;
    double price;
    int full;

    List<Fillup> fillupList;


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

        mileage = editedFillup.getMileage();
        amount = editedFillup.getAmount();
        price = editedFillup.getPrice();
        full = editedFillup.getFull();

        editTextDate = (EditText) findViewById(R.id.edit_text_edit_fillup_date);
        editTextMileage = (EditText) findViewById(R.id.edit_text_edit_fillup_mileage);
        editTextAmount = (EditText) findViewById(R.id.edit_text_edit_fillup_amount);
        editTextPrice = (EditText) findViewById(R.id.edit_text_edit_fillup_price);
        checkBoxFull = (CheckBox) findViewById(R.id.checkbox_edit_fillup_full);

        String stringMileage = Integer.toString(mileage);
        String stringAmount = Double.toString(amount);
        String stringPrice = Double.toString(price);
        editTextDate.setText(month + "/" + day + "/" + year);
        editTextMileage.setText(stringMileage);
        editTextAmount.setText(stringAmount);
        editTextPrice.setText(stringPrice);
        checkBoxFull.setChecked(full == 1);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, ActivityEditFillup.this, year, month, day);

        buttonEditFillup = (Button) findViewById(R.id.button_edit_fillup);
        buttonEditFillup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (editFillup())
                {
                    Intent returnToViewRecords = new Intent(ActivityEditFillup.this, ActivityViewCarRecords.class);
                    returnToViewRecords.putExtra("car_index", currentCarIndex);
                    returnToViewRecords.putExtra("page_index", 0);
                    finish();
                    startActivity(returnToViewRecords);
                }
            }
        });
        editTextDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                datePickerDialog.show();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        user = firebaseAuth.getCurrentUser();

        if (user != null)
        {
            userID = user.getUid();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = database.getReference();
        databaseReference.child(userID);


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

        mileage = Integer.parseInt(mileageString);
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

        editTextDate.setText(this.month + "/" + this.day + "/" + this.year);

    }
}
