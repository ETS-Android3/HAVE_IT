package com.example.have_it;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 *This is the activity for adding a new event
 * @author songkunguo & ruiqingtian
 */
public class AddEventActivity extends AppCompatActivity implements FirestoreAddData, DatabaseUserReference{

    /**
     *Reference to event input, of class {@link EditText}
     */
    EditText eventText;
    /**
     *Reference to date input, of class {@link TextView}
     */
    TextView dateText;
    /**
     *Reference to address, of class {@link TextView}
     */
    TextView addressText;
    /**
     *Reference to the pick location button, of class {@link Button}
     */
    Button pickLocation;
    /**
     *Reference to the addEvent button, of class {@link Button}
     */
    Button addEvent;
    /**
     *Reference to the dialog for picking date, of class {@link DatePickerDialog}
     */
    DatePickerDialog picker;

    /**
     * Lagitude and Longitude to store the location as String Variable
     */
    String latitude = null;
    String longitude = null;

    Context context;

    /**
     *This is the method invoked when the activity starts
     * @param savedInstanceState {@link Bundle} used for its super class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        context = this.getApplicationContext();

        eventText = findViewById(R.id.event_editText);
        dateText = findViewById(R.id.date);
        addressText = findViewById(R.id.address);
        pickLocation = findViewById(R.id.pick_location_button);
        addEvent = findViewById(R.id.addevent_button);

        Date today = Calendar.getInstance().getTime();
        String todayAsString = new SimpleDateFormat("yyyy-MM-dd").format(today);
        dateText.setText(todayAsString);


        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddEventActivity.this,
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

        Intent intent = new Intent(AddEventActivity.this.getApplicationContext(), PickLocationMapsActivity.class);
        pickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(intent, 2404);
            }
        });


        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDataToFirestore();
            }
        });
    }

    /**
     *This is the method invoked when the back in menu is pressed
     * @param item used for its super class
     * @return the result of its super selecting the same option
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

    /**
     * This is the method for adding data to the firestore
     */
    @Override
    public void addDataToFirestore() {
        Intent i = getIntent();
        String selectedTitle = i.getStringExtra("habit");
        final CollectionReference eventListReference = db.collection("Users")
                .document(logged.getUID()).collection("HabitList")
                .document(selectedTitle).collection("EventList");
        final String event = eventText.getText().toString();
        HashMap<String, Object> data = new HashMap<>();

        if (event.length()>0){
            data.put("event", event);
            data.put("date", dateText.getText().toString());
            data.put("latitude", latitude);
            data.put("longitude", longitude);
            String newDate=dateText.getText().toString();
            Date startDate = new Date();
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd")
                        .parse(dateText.getText().toString());
            } catch (ParseException e){
                Toast.makeText(getApplicationContext(),"Not valid date", Toast.LENGTH_LONG).show();
                return;
            }
            EventController.addEventToFirestore(eventListReference,event,data,newDate);
            finish();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 2404) {
            if(data != null) {
                latitude = data.getStringExtra("LAT");
                longitude = data.getStringExtra("LONG");

                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(Double.valueOf(latitude), Double.valueOf(longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0);
                    addressText.setText(address);

                } catch (IOException e) {
                    Toast.makeText(context,String.valueOf(e),Toast.LENGTH_SHORT).show();

                }

            }
        }
    }
}
