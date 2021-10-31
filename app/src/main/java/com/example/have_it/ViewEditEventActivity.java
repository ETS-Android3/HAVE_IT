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

public class ViewEditEventActivity extends AppCompatActivity {
    FirebaseFirestore db;
    EditText eventText;

    TextView DateText;

    Button confirm;
    Button delete;

    DatePickerDialog picker;
    Activity nowActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_event);
         db = FirebaseFirestore.getInstance();
        Intent i = getIntent();
        String selected_event = i.getStringExtra("event");
        String selected_habit = i.getStringExtra("habit");


        final CollectionReference EventListReference = db.collection("Users")
                .document("DefaultUser").collection("HabitList").document(selected_habit).collection("Eventlist");
        eventText = findViewById(R.id.event_editText_viewedit);

        DateText = findViewById(R.id.event_date_viewedit);
        confirm = findViewById(R.id.confirm_button_viewedit);
        delete = findViewById(R.id.delete_button);





        EventListReference.document(selected_event).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                eventText.setText((String)documentSnapshot.getData().get("event"));

                SimpleDateFormat spf= new SimpleDateFormat("yyyy-MM-dd");
                DateText.setText(spf.format(((Timestamp)documentSnapshot.getData().get("date")).toDate()));


            }
        });


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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( getApplicationContext(),  selected_event, Toast.LENGTH_SHORT).show();
                EventListReference.document(selected_event)
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
////
        confirm.setOnClickListener(new View.OnClickListener() {
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


                    EventListReference.document(selected_event)
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
                            .document(event)
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