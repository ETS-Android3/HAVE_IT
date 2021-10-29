package com.example.have_it;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class AddHabitActivity extends AppCompatActivity {
    FirebaseFirestore db;
    EditText titleText;
    EditText reasonText;
    TextView startDateText;
    WeekdaysPicker weekdaysPicker;
    Button confirm;
    DatePickerDialog picker;
    Activity nowActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        db = FirebaseFirestore.getInstance();
        final CollectionReference habitListReference = db.collection("Users")
                .document("DefaultUser").collection("HabitList");

        titleText = findViewById(R.id.habit_title_editText);
        reasonText = findViewById(R.id.habit_reason_editText);
        weekdaysPicker = (WeekdaysPicker) findViewById(R.id.habit_weekday_selection);
        startDateText = findViewById(R.id.habit_start_date);
        confirm = findViewById(R.id.add_habit_button);

        weekdaysPicker.setSelected(true);

        startDateText.setOnClickListener(new View.OnClickListener() {
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
                                startDateText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieving the city name and the province name from the EditText fields
                final String title = titleText.getText().toString();
                final String reason = reasonText.getText().toString();
                Date startDate = new Date();
                try {
                    startDate = new SimpleDateFormat("yyyy-MM-dd")
                            .parse(startDateText.getText().toString());
                } catch (ParseException e){
                    Toast.makeText(getApplicationContext(),"Not valid date", Toast.LENGTH_LONG).show();
                    return;
                }
                final Timestamp startDateTimestamp = new Timestamp(startDate);

                List<Integer> selectedDays = weekdaysPicker.getSelectedDays();
                Boolean[] defaultReg= {false, false, false, false, false, false, false};
                List<Boolean> weekdayReg = new ArrayList<>(Arrays.asList(defaultReg));
                for (int each : selectedDays){
                    weekdayReg.set(each-1,true);
                }

                HashMap<String, Object> data = new HashMap<>();

                if (title.length()>0){
                    data.put("title", title);
                    data.put("reason", reason);
                    data.put("dateStart", startDateTimestamp);
                    data.put("weekdayReg", weekdayReg);

                    habitListReference
                            .document(title)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is succeeded
                                    Log.d("Adding Habit", "Habit data has been added successfully!");
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // These are a method which gets executed if there’s any problem
                                    Log.d("Adding Habit", "Habit data could not be added!" + e.toString());
                                    Toast.makeText(getApplicationContext(),"Not being able to add data, please check duplication title", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }
}
