package com.twild.gastracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.database.FirebaseDatabase.*;

public class ActivityListOfCars extends AppCompatActivity implements View.OnClickListener
{

    static boolean databasePersistenceSet = false;

    int contextMenuVehiclePosition;

    private Button buttonSignOut;
    private Button buttonAddCar;

    private FirebaseAuth firebaseAuth;

    FirebaseUser user;

    static String userID;

    static List<Car> carList;

    static DatabaseReference databaseReference;
    static FirebaseDatabase firebaseDatabase;

    ArrayList<Car> arrayListCarsID = new ArrayList<>();
    ArrayList<String> arrayListCarNames = new ArrayList<>();
    ArrayList<String> arrayListCarInfo = new ArrayList<>();

    ListAdapter listAdapterCars;
    ListView listViewCars;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        /*
         * When this window is opened, set the layout, and if there isn't a current
         * list of cars, create a blank list. After that, initiate the buttons and
         * setup the car list.
         */

        if (!databasePersistenceSet)
        {
            firebaseDatabase.getInstance().setPersistenceEnabled(true);
            databasePersistenceSet = true;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_cars);

        if (carList == null)
        {
            carList = new ArrayList<>();
        }

        firebaseAuth = FirebaseAuth.getInstance();

        getData();
        setupCarList();

    }

    private void setView()
    {

        /*
         * Once the lists of all the car ids, names, and notes have been updated,
         * send the contents of the names and notes to the list adapter. Set an
         * onClick adapter so that when we click on one of the cars in the list,
         * we can receive the index on that list so that we can send that car forward
         * to the next window.
         */

        setContentView(R.layout.activity_list_of_cars);

        listAdapterCars = new AdapterCarList(this, arrayListCarNames, arrayListCarInfo);
        listViewCars = (ListView) findViewById(R.id.car_list);
        listViewCars.setAdapter(listAdapterCars);

        registerForContextMenu(listViewCars);

        listViewCars.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Log.d("car", "clicked on car " + i);
                moveToVehicleInfo(i);
            }
        });

        /*
         * Assign the buttons to the ones in the layout, and set onClick listeners.
         */

        buttonSignOut = (Button) findViewById(R.id.button_sign_out);
        buttonAddCar = (Button) findViewById(R.id.button_add_new_car);
        buttonSignOut.setOnClickListener(this);
        buttonAddCar.setOnClickListener(this);

    }

    private void moveToVehicleInfo(int i)
    {

        Intent moveToVehicleInfo = new Intent(this, ActivityViewCarRecords.class);

        moveToVehicleInfo.putExtra("car_index", i);
        startActivity(moveToVehicleInfo);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {

        Log.d("longPress", "the user longClicked");

        if (view.getId() == R.id.car_list)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(arrayListCarNames.get(info.position));
            menu.add(Menu.NONE, 0, 0, "Edit");
            menu.add(Menu.NONE, 1, 1, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        contextMenuVehiclePosition = info.position;

        if (menuItemIndex == 0)
        {
            Log.d("Context Menu", "Selected Edit");
            Intent editCar = new Intent(this, ActivityEditCar.class);
            editCar.putExtra("car_index", contextMenuVehiclePosition);

            startActivity(editCar);

        }
        else if (menuItemIndex == 1)
        {
            Log.d("Context Menu", "Selected Delete");
            AlertDialog.Builder alert = new AlertDialog.Builder(
                    ActivityListOfCars.this);
            alert.setTitle("Alert!!");
            alert.setMessage("Caution! All records from this vehicle will be removed. This cannot be undone.");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                    carList.remove(contextMenuVehiclePosition);
                    databaseReference.child(userID).setValue(carList);
                    dialog.dismiss();

                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });

            alert.show();
        }

        return true;
    }

    private void getData()
    {

        /*
         * Use the firebase authorization to get the ID of the current user.
         * Find the node in the database that corresponds with that user.
         * Whenever anything inside that node gets updated, clear the list of
         * cars, and repopulate the list with the new data.
         */

        user = firebaseAuth.getCurrentUser();
        if (user != null)
        {
            userID = user.getUid();
        }

        firebaseDatabase = getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                carList.clear();

                Log.d("retrieving cars", "" + dataSnapshot.getChildrenCount());

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children)
                {
                    Car car = child.getValue(Car.class);
                    carList.add(car);
                }
                setupCarList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void setupCarList()
    {

        /*
         * Clear the array lists of all car ids, names, and notes. After that,
         * iterate through the list of cars and repopulate the array lists.
         */

        arrayListCarsID.clear();
        arrayListCarNames.clear();
        arrayListCarInfo.clear();

        if(carList.size() > 0)
        {
            for (Car car : carList)
            {
                arrayListCarsID.add(car);
                arrayListCarNames.add(car.getCarInfo().getName());
                arrayListCarInfo.add(car.getCarInfo().getYear() + " " + car.getCarInfo().getMake() + " " + car.getCarInfo().getModel());
            }
        }

        setView();
    }

    @Override
    public void onClick(View view)
    {

        if (view == buttonSignOut)
        {
            signOut();
        }
        if (view == buttonAddCar)
        {
            startActivity(new Intent(this, ActivityAddCar.class));
        }
    }

    private void signOut()
    {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(ActivityListOfCars.this, ActivityLoginScreen.class));
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        databaseReference.child(userID).setValue(carList);
    }
}
