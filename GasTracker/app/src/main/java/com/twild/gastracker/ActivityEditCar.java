package com.twild.gastracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import static com.twild.gastracker.ListOfCars.carList;
import static com.twild.gastracker.ListOfCars.databaseReference;
import static com.twild.gastracker.ListOfCars.firebaseDatabase;
import static com.twild.gastracker.ListOfCars.userID;

public class ActivityEditCar extends AppCompatActivity implements View.OnClickListener {

    Car carToEdit;
    int carIndex;
    Button buttonEditCar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent i = getIntent();

        carIndex = i.getIntExtra("car_index", 0);

        carToEdit = carList.get(carIndex);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);

        buttonEditCar = (Button) findViewById(R.id.button_edit_car);

        EditText editTextCarMake = (EditText) findViewById(R.id.edit_text_edit_car_make);
        EditText editTextCarModel = (EditText) findViewById(R.id.edit_text_edit_car_model);
        EditText editTextCarYear = (EditText) findViewById(R.id.edit_text_edit_car_year);
        EditText editTextCarName = (EditText) findViewById(R.id.edit_text_edit_car_name);

        editTextCarMake.setText(carToEdit.getCarInfo().getMake());
        editTextCarModel.setText(carToEdit.getCarInfo().getModel());
        editTextCarYear.setText(carToEdit.getCarInfo().getYear());
        editTextCarName.setText(carToEdit.getCarInfo().getName());

        buttonEditCar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        if (view == buttonEditCar)
        {
            if (editCar())
            {
                finish();
            }
        }
    }

    private boolean editCar()
    {

        EditText editTextCarMake = (EditText) findViewById(R.id.edit_text_edit_car_make);
        EditText editTextCarModel = (EditText) findViewById(R.id.edit_text_edit_car_model);
        EditText editTextCarYear = (EditText) findViewById(R.id.edit_text_edit_car_year);
        EditText editTextCarName = (EditText) findViewById(R.id.edit_text_edit_car_name);

        String carMake = String.valueOf(editTextCarMake.getText()).trim();
        String carModel = String.valueOf(editTextCarModel.getText()).trim();
        String carYear = String.valueOf(editTextCarYear.getText()).trim();
        String carName = String.valueOf(editTextCarName.getText()).trim();

        if (carName.equals(""))
        {
            carName = carMake + " " + carModel;
        }

        if (carMake.equals("") || carModel.equals("") || carYear.equals(""))
        {
            Toast.makeText(ActivityEditCar.this, "Please enter all information", Toast.LENGTH_SHORT).show();
        }
        else
        {
            carToEdit.setCarInfo(carName, carMake, carModel, carYear);

            databaseReference.child(userID).setValue(carList);

            return true;
        }
        return false;
    }
}
