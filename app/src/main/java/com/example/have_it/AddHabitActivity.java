package com.example.have_it;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dpro.widgets.WeekdaysPicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *This is the activity for adding a new habit
 * @author yulingshen
 */
public class AddHabitActivity extends AppCompatActivity implements FirestoreAddData, DatabaseUserReference{
    /**
     *Reference to title input, of class {@link EditText}
     */
    EditText titleText;
    /**
     *Reference to reason input, of class {@link EditText}
     */
    EditText reasonText;
    /**
     *Reference to date input, of class {@link TextView}
     */
    TextView startDateText;
    /**
     *Reference to weekdays input, of class {@link WeekdaysPicker}
     */
    WeekdaysPicker weekdaysPicker;
    /**
     *Reference to the confirm button, of class {@link Button}
     */
    Button confirm;
    /**
     *Reference to the dialog for picking date, of class {@link DatePickerDialog}
     */
    DatePickerDialog picker;
    /**
     * Reference to the switch for publicity of habit, of class {@link Switch}
     */
    Switch publicitySwitch;

    /**
     *This is the method invoked when the activity starts
     * @param savedInstanceState {@link Bundle} used for its super class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        titleText = findViewById(R.id.habit_title_editText);
        reasonText = findViewById(R.id.habit_reason_editText);
        weekdaysPicker = (WeekdaysPicker) findViewById(R.id.habit_weekday_selection);
        startDateText = findViewById(R.id.habit_start_date);
        confirm = findViewById(R.id.add_habit_button);
        publicitySwitch = findViewById(R.id.publicity_switch);

        weekdaysPicker.setSelected(true);

        String pattern = "yyyy-MM-dd";
        Date today = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat(pattern);
        String todayAsString = df.format(today);
        startDateText.setText(todayAsString);


        startDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddHabitActivity.this,
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
                                startDateText.setText(new SimpleDateFormat("yyyy-MM-dd").format(selectDay));
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
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
        final CollectionReference habitListReference = db.collection("Users")
                .document(logged.getUID()).collection("HabitList");
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

        final Boolean publicity = publicitySwitch.isChecked();

        HashMap<String, Object> data = new HashMap<>();

        if (title.length()>0){
            data.put("title", title);
            data.put("reason", reason);
            data.put("dateStart", startDateTimestamp);
            data.put("weekdayReg", weekdayReg);
            data.put("publicity", publicity);
            HabitController.addHabit(data,habitListReference,title);
            finish();


        }
    }
}
