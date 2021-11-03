package com.example.have_it;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 */
public class EventPageActivity extends AppCompatActivity {
    /**
     *
     */
    FloatingActionButton addEvent;
    /**
     *
     */
    ListView eventList;
    /**
     *
     */
    EventList eventAdapter;
    /**
     *
     */
    FirebaseFirestore db;
    /**
     *
     */
    ArrayList<Event> eventDataList;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        addEvent = findViewById(R.id.add_event_button);
        Intent i = getIntent();
        String selected_title = i.getStringExtra("habit");
        eventList = findViewById(R.id.all_event_list);

        final Intent addeventIntent = new Intent(this, AddEventActivity.class);
        eventDataList = new ArrayList<>();
        eventAdapter = new EventList(this, eventDataList);
        eventList.setAdapter(eventAdapter);
        db = FirebaseFirestore.getInstance();

        User logged = User.getInstance();
        final CollectionReference eventListReference = db.collection("Users")
                .document(logged.getUID()).collection("HabitList")
                .document(selected_title).collection("Eventlist");

        eventListReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                eventDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String event = (String) doc.getData().get("event");
                    String sDate = (String) doc.getData().get("date");
                    eventDataList.add(new Event(event,sDate));
                }
                eventAdapter.notifyDataSetChanged();
            }
        });
        final Intent view_editEventIntent = new Intent(this, ViewEditEventActivity.class);
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                view_editEventIntent.putExtra("event_date", eventDataList.get(position).getDate());
                view_editEventIntent.putExtra("habit", selected_title);
                startActivity(view_editEventIntent);
            }

        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addeventIntent.putExtra("habit", selected_title);
                startActivity(addeventIntent);
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