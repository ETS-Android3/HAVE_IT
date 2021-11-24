package com.example.have_it;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 *This is the activity for main page of viewing events
 * @author songkunguo
 */
public class EventPageActivity extends AppCompatActivity implements FirestoreGetCollection, DatabaseUserReference{
    /**
     *Reference to the confirm addEvent, of class {@link Button}
     */
    FloatingActionButton addEvent;
    /**
     *A reference to all event list view, of class {@link ListView}
     */
    ListView eventList;
    /**
     *event adapter, of class {@link EventList}
     */
    EventList eventAdapter;
    /**
     *The habit name, of class {@link String}
     */
    String selectedTitle;
    /**
     *event list, of class {@link ArrayList}
     */
    ArrayList<Event> eventDataList;

    /**
     *This is the method invoked when the activity starts
     * @param savedInstanceState {@link Bundle} used for its super class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        addEvent = findViewById(R.id.add_event_button);
        Intent i = getIntent();
        selectedTitle = i.getStringExtra("habit");
        eventList = findViewById(R.id.all_event_list);

        final Intent addEventIntent = new Intent(this, AddEventActivity.class);
        eventDataList = new ArrayList<>();
        eventAdapter = new EventList(this, eventDataList);
        eventList.setAdapter(eventAdapter);

        getCollection();

        final Intent viewEditEventIntent = new Intent(this, ViewEditEventActivity.class);
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                viewEditEventIntent.putExtra("event_date", eventDataList.get(position).getDate());
                viewEditEventIntent.putExtra("habit", selectedTitle);
                startActivity(viewEditEventIntent);
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEventIntent.putExtra("habit", selectedTitle);
                startActivity(addEventIntent);
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
     * This is the method for getting a reference to collection
     */
    @Override
    public void getCollection() {
        final CollectionReference eventListReference = db.collection("Users")
                .document(logged.getUID()).collection("HabitList")
                .document(selectedTitle).collection("EventList");
        EventController.getCollectionEvent(eventListReference,eventAdapter,eventDataList);


    }
}