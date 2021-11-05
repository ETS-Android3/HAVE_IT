package com.example.have_it;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class IndicatorActivity extends AppCompatActivity {


    CompactCalendarView simpleCalendarView;
    Button backButton;

    FirebaseFirestore db;

    private final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateFormatInDb = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.indicator_view);

        Intent i = getIntent();
        backButton = findViewById(R.id.backButton);

        simpleCalendarView = (CompactCalendarView) findViewById(R.id.simpleCalendarView);
        simpleCalendarView.setUseThreeLetterAbbreviation(true);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(dateFormatMonth.format(new Date()));
        }

        db = FirebaseFirestore.getInstance();
        User logged = User.getInstance();
        final CollectionReference habitListReference = db.collection("Users")
                .document(logged.getUID()).collection("HabitList");

        String selectedHabit = i.getStringExtra("habit");
        habitListReference.document(selectedHabit).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                Timestamp startTimestamp = (Timestamp) doc.getData().get("dateStart");
                Date dateStart = startTimestamp.toDate();
                Date today = Calendar.getInstance().getTime();


                Calendar cal = Calendar.getInstance();
                cal.setTime(dateStart);
                Event start_Habit= new Event(Color.parseColor("#010203"), cal.getTimeInMillis());
                simpleCalendarView.addEvent(start_Habit);

                List<Boolean> weekdayRegArray = (List<Boolean>) doc.getData().get("weekdayReg");
                List<Integer> weekdayIntArray = new ArrayList<>(7);
                Integer c = 1;
                for (boolean each : weekdayRegArray) {
                    if (each) {
                        weekdayIntArray.add(c);
                    }
                    c++;
                }

                final CollectionReference EventListReference = db.collection("Users")
                        .document(logged.getUID()).collection("HabitList")
                        .document(selectedHabit).collection("Eventlist");


                /**
                 *Now we are going to get data from firebase and insert some events in the calendar
                 *For missing event: planed, but we don't have event at that day ---> Red Color
                 *For complete event: planed, and we do have event for that habit at that day ---> Green Color
                 *For future event: planed, but in the future to complete ---> Yellow Color
                 **/
                for(int i = 1; i <= 120; i++){
                    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1,0,0,0);

                    int day_In_Week = cal.get(Calendar.DAY_OF_WEEK) ;
                    if(weekdayIntArray.contains(day_In_Week)){

                        Date date_Check = cal.getTime();

                        if(date_Check.before(today)) {
                            Event missing = new Event(Color.parseColor("#ff0005"), cal.getTimeInMillis());
                            simpleCalendarView.addEvent(missing);

                        }else{
                            Event wait_Todo = new Event(Color.parseColor("#fcfd2f"), cal.getTimeInMillis());
                            if(!simpleCalendarView.getEvents(date_Check).contains(wait_Todo)){
                                simpleCalendarView.addEvent(wait_Todo);
                            }
                        }
                    }
                }

                EventListReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots2, @Nullable
                            FirebaseFirestoreException error) {
                        Iterator iterator2 = queryDocumentSnapshots2.iterator();
                        while (iterator2.hasNext()) {
                            QueryDocumentSnapshot doc_Event = (QueryDocumentSnapshot)iterator2.next();

                            try {
                                Date eventDate = dateFormatInDb.parse((String)doc_Event.getData().get("date"));
                                Calendar c = Calendar.getInstance();
                                c.setTime(eventDate);

                                Event notMissed= new Event(Color.parseColor("#ff0005"), c.getTimeInMillis());
                                if((simpleCalendarView.getEvents(c.getTime()).contains(notMissed)) && (weekdayIntArray.contains(c.get(Calendar.DAY_OF_WEEK)))){
                                    simpleCalendarView.removeEvent(notMissed);
                                }
                                Event habit_Event= new Event(Color.parseColor("#10fe60"), c.getTimeInMillis());
                                simpleCalendarView.addEvent(habit_Event);


                            } catch (ParseException e) {
                                Toast.makeText(getApplicationContext(),"Not valid date", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    }
                });

            }
        });


        simpleCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();

                String clickedDate = dateFormatInDb.format(dateClicked);
//                Toast.makeText(getApplicationContext(),clickedDate, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
}