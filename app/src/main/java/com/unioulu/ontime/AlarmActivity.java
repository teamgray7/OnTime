package com.unioulu.ontime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {

    // Needed widgets
    Spinner snoozeSpinner;
    Button snoozeButton;
    TextView medicineName, medicineTime;
    ImageView medicinePic;

    // Needed variables
    ArrayList<String> snoozeTimeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
    }

    
    @Override
    protected void onStart() {
        super.onStart();

        // Initialization of the variables
        snoozeSpinner = (Spinner) findViewById(R.id.snoozeSpinner);
        snoozeButton  = (Button ) findViewById(R.id.snoozeButton );


        // --------------------------------------- Spinner implementation ---------------------------------
        snoozeTimeList = new ArrayList<>();

        // These values should be retrieved from database (Set in settings screen)
        snoozeTimeList.add("5 min");
        snoozeTimeList.add("10 min");
        snoozeTimeList.add("15 min");
        snoozeTimeList.add("30 min");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(),
                                            R.layout.support_simple_spinner_dropdown_item,
                                            snoozeTimeList);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Setting drop down elements
        snoozeSpinner.setAdapter(spinnerAdapter);

        // Spinner on Item click listener
        snoozeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Getting selected item
                String item = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(getApplicationContext(), item+" selected !", Toast.LENGTH_SHORT).show();
            }
        });


        // ------------------------------------ End of spinner implementation -------------------------------------------------

        // ------------------------------------ Snooze button implementation --------------------------------------------------
        snoozeButton = (Button) findViewById(R.id.snoozeButton);

        snoozeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Snooze button clicked !", Toast.LENGTH_SHORT).show();
                // Closing alarm activity
                finish();
            }
        });

        // ------------------------------------ End of snooze button implementation --------------------------------------------

    }
}
