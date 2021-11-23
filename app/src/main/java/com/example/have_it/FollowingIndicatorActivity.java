package com.example.have_it;

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


/**
 *This is the activity for implementing an indicator using compact calendar view (Get resource from: com.github.sundeepk.compactcalendarview.CompactCalendarView)
 * @author ruiqingtian,yuling shen
 * base on ruiqing's indicator, modification to satisfy the use for view following
 */
public class FollowingIndicatorActivity extends AppCompatActivity implements DatabaseUserReference{

    /**
     * A reference to compact calendar view, of class {@link CompactCalendarView}
     */
    CompactCalendarView simpleCalendarView;

    /**
     * A reference to date format that displayed on the title, of class {@link SimpleDateFormat}
     */
    private final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());

    /**
     * A reference to date format that stored in firestore, of class {@link SimpleDateFormat}
     */
    private final SimpleDateFormat dateFormatInDb = new SimpleDateFormat("yyyy-MM-dd");

    /**
     *This is the method invoked when the activity starts
     * @param savedInstanceState {@link Bundle} used for its super class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);

        Intent intent = getIntent();

        //connect the compact calendar view in layout
        simpleCalendarView = (CompactCalendarView) findViewById(R.id.simpleCalendarView);
        simpleCalendarView.setUseThreeLetterAbbreviation(true);

        //Adding an title on the action bar
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(dateFormatMonth.format(new Date()));
        }

        //Set the title of habit from parent activity
        String selectedHabit = intent.getStringExtra("habit");
        String selectedUserUID = intent.getStringExtra("UID");

        //Access the indicator as a specific user
        final CollectionReference habitListReference = db.collection("Users")
                .document(selectedUserUID).collection("HabitList");




        //Access the data in firebase through habit selected title
        habitListReference
                .document(selectedHabit)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        Timestamp startTimestamp = (Timestamp) doc.getData().get("dateStart");
                        Date dateStart = startTimestamp.toDate();
                        Date today = Calendar.getInstance().getTime();


                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dateStart);
                        Event startHabit= new Event(Color.parseColor("#010203"), cal.getTimeInMillis());
                        simpleCalendarView.addEvent(startHabit);

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
                                .document(selectedUserUID).collection("HabitList")
                                .document(selectedHabit).collection("EventList");


                        /*
                         *Now we are going to get data from firebase and insert some events in the calendar
                         *For missing event: planed, but we don't have event at that day ---> Red Color
                         *For complete event: planed, and we do have event for that habit at that day ---> Green Color
                         *For future event: planed, but in the future to complete ---> Yellow Color
                         */

                        //We primarily designed to set the indicator for 120days from the start date of habit
                        for(int i = 1; i <= 120; i++){
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1,0,0,0);

                            int dayInWeek = cal.get(Calendar.DAY_OF_WEEK) ;
                            if(weekdayIntArray.contains(dayInWeek)){

                                Date dateCheck = cal.getTime();

                                if(dateCheck.before(today)) {
                                    Event missing = new Event(Color.parseColor("#ff0005"), cal.getTimeInMillis());
                                    simpleCalendarView.addEvent(missing);

                                }else{
                                    Event waitTodo = new Event(Color.parseColor("#fcfd2f"), cal.getTimeInMillis());
                                    if(!simpleCalendarView.getEvents(dateCheck).contains(waitTodo)){
                                        simpleCalendarView.addEvent(waitTodo);
                                    }
                                }
                            }
                        }

                        //For all the events in that habit, we need to access the firestore and get every event
                        //and set the green light in their dates
                        EventListReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots2, @Nullable
                                    FirebaseFirestoreException error) {
                                Iterator iterator2 = queryDocumentSnapshots2.iterator();
                                while (iterator2.hasNext()) {
                                    QueryDocumentSnapshot docEvent = (QueryDocumentSnapshot)iterator2.next();

                                    try {
                                        Date eventDate = dateFormatInDb.parse((String)docEvent.getData().get("date"));
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(eventDate);

                                        Event notMissed= new Event(Color.parseColor("#ff0005"), c.getTimeInMillis());
                                        if((simpleCalendarView.getEvents(c.getTime()).contains(notMissed)) && (weekdayIntArray.contains(c.get(Calendar.DAY_OF_WEEK)))){
                                            simpleCalendarView.removeEvent(notMissed);
                                        }
                                        Event habitEvent= new Event(Color.parseColor("#10fe60"), c.getTimeInMillis());
                                        simpleCalendarView.addEvent(habitEvent);

                                    } catch (ParseException e) {
                                        Toast.makeText(getApplicationContext(),"Not valid date", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                            }
                        });
                    }
                });


        simpleCalendarView
                .setListener(new CompactCalendarView.CompactCalendarViewListener() {
                    //Used for one specific date is clicked
                    @Override
                    public void onDayClick(Date dateClicked) {
                        Context context = getApplicationContext();
                        String clickedDate = dateFormatInDb.format(dateClicked);
//                Toast.makeText(getApplicationContext(),clickedDate, Toast.LENGTH_LONG).show();

                    }

                    //Used for scrolling the calendar left and right to other months
                    @Override
                    public void onMonthScroll(Date firstDayOfNewMonth) {
                        actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));

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