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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class ViewEditEventActivity extends AppCompatActivity {
    /**
     *
     */
    FirebaseFirestore db;
    /**
     *
     */
    EditText eventText;
    /**
     *
     */
    TextView dateText;
    /**
     *
     */
    Button confirm;
    /**
     *
     */
    Button delete;
    /**
     *
     */
    DatePickerDialog picker;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_event);
        db = FirebaseFirestore.getInstance();
        Intent i = getIntent();
        String selected_event_date = i.getStringExtra("event_date");
        String selected_habit = i.getStringExtra("habit");

        User logged = User.getInstance();
        final CollectionReference EventListReference = db.collection("Users")
                .document(logged.getUID()).collection("HabitList").document(selected_habit).collection("Eventlist");
        eventText = findViewById(R.id.event_editText_viewedit);
        dateText = findViewById(R.id.event_date_viewedit);
        confirm = findViewById(R.id.confirm_button_viewedit);
        delete = findViewById(R.id.delete_button);

        EventListReference.document(selected_event_date).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                eventText.setText((String)documentSnapshot.getData().get("event"));
                //  SimpleDateFormat spf= new SimpleDateFormat("yyyy-MM-dd");
                //DateText.setText(spf.format(((Timestamp)documentSnapshot.getData().get("date")).toDate()));
                dateText.setText( documentSnapshot.getData().get("date").toString());
            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ViewEditEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String selectDayString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                Date selectDay = new Date();
                                try {
                                    selectDay = new SimpleDateFormat("yyyy-MM-dd")
                                            .parse(selectDayString);
                                } catch (ParseException e){
                                    Toast.makeText(getApplicationContext(),"Not valid date", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                dateText.setText(new SimpleDateFormat("yyyy-MM-dd").format(selectDay));
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText( getApplicationContext(),  selected_event_date, Toast.LENGTH_SHORT).show();
                EventListReference.document(selected_event_date)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Delete Event", "Habit data has been deleted successfully!");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Delete event", "Error deleting document", e);
                            }
                        });

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieving the city name and the province name from the EditText fields
                final String event = eventText.getText().toString();
                HashMap<String, Object> data = new HashMap<>();

                if (event.length()>0){
                    data.put("event", event);
                    data.put("date", dateText.getText().toString());
                    Date startDate = new Date();
                    try {
                        startDate = new SimpleDateFormat("yyyy-MM-dd")
                                .parse(dateText.getText().toString());
                    } catch (ParseException e) {
                        Toast.makeText(getApplicationContext(), "Not valid date", Toast.LENGTH_LONG).show();
                        return;
                    }

                    EventListReference.document(selected_event_date)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Delete event", "event data has been deleted successfully!");

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Delete event", "Error deleting document", e);
                                }
                            });

                    EventListReference
                            .document(dateText.getText().toString())
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is succeeded
                                    Log.d("Adding event", "event data has been edited successfully!");
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // These are a method which gets executed if there’s any problem
                                    Log.d("Adding event", "Habit data could not be edited!" + e.toString());
                                    Toast.makeText(getApplicationContext(),"Not being able to edit data, please check duplication event", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }

    /**
     *
     * @param item
     * @return
     */
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