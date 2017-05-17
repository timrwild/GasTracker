package com.twild.gastracker;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.twild.gastracker.ListOfCars.carList;

public class ActivityAddCar extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSubmitCar;

    private FirebaseAuth firebaseAuth;
    FirebaseUser user;

    private DatabaseReference databaseReference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        buttonSubmitCar = (Button) findViewById(R.id.button_submit_car);
        buttonSubmitCar.setOnClickListener(this);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);

            setResult(RESULT_CANCELED);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view)
    {
        if (view == buttonSubmitCar)
        {
            if (addCar())
            {
                finish();
                startActivity(new Intent(ActivityAddCar.this, ListOfCars.class));
            }
        }
    }

    private boolean addCar()
    {
        EditText editTextCarMake = (EditText) findViewById(R.id.edit_text_add_car_make);
        EditText editTextCarModel = (EditText) findViewById(R.id.edit_text_add_car_model);
        EditText editTextCarYear = (EditText) findViewById(R.id.edit_text_add_car_year);
        EditText editTextCarName = (EditText) findViewById(R.id.edit_text_add_car_name);

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
            Toast.makeText(ActivityAddCar.this, "Please enter all information", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Car newCar = new Car(carName, carMake, carModel, carYear);

            carList.add(newCar);
            databaseReference.child(userID).push().setValue(newCar);

            return true;
        }

        return false;

    }

}
