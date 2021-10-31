package com.example.have_it;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dpro.widgets.WeekdaysPicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AddEventActivity extends AppCompatActivity {
    FirebaseFirestore db;
    EditText eventText;

    TextView DateText;

    Button addevent;
    DatePickerDialog picker;
    Activity nowActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        db = FirebaseFirestore.getInstance();
        Intent i = getIntent();
        String selected_title = i.getStringExtra("habit");
        final CollectionReference eventListReference = db.collection("Users")
                .document("DefaultUser").collection("HabitList").document(selected_title).collection("Eventlist");

        eventText = findViewById(R.id.event_editText);


        DateText = findViewById(R.id.date);
        addevent = findViewById(R.id.addevent_button);



        DateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(nowActivity,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                DateText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        addevent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),selected_title, Toast.LENGTH_LONG).show();
                // Retrieving the city name and the province name from the EditText fields
                final String title = eventText.getText().toString();

                Date startDate = new Date();
                try {
                    startDate = new SimpleDateFormat("yyyy-MM-dd")
                            .parse(DateText.getText().toString());
                } catch (ParseException e){
                    Toast.makeText(getApplicationContext(),"Not valid date", Toast.LENGTH_LONG).show();
                    return;
                }
                final Timestamp startDateTimestamp = new Timestamp(startDate);




                HashMap<String, Object> data = new HashMap<>();


            }
        });
        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieving the city name and the province name from the EditText fields
                final String event = eventText.getText().toString();

                Date startDate = new Date();
                try {
                    startDate = new SimpleDateFormat("yyyy-MM-dd")
                            .parse(DateText.getText().toString());
                } catch (ParseException e){
                    Toast.makeText(getApplicationContext(),"Not valid date", Toast.LENGTH_LONG).show();
                    return;
                }
                final Timestamp startDateTimestamp = new Timestamp(startDate);



                HashMap<String, Object> data = new HashMap<>();

                if (event.length()>0){
                    data.put("event", event);

                    data.put("date", startDateTimestamp);


                    eventListReference
                            .document(event)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is succeeded
                                    Log.d("Adding event", "event data has been added successfully!");
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // These are a method which gets executed if there’s any problem
                                    Log.d("Adding event", "Habit event could not be added!" + e.toString());
                                    Toast.makeText(getApplicationContext(),"Not being able to add data, please check duplication title", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
