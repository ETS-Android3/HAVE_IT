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

import java.util.ArrayList;
import java.util.Date;

public class EventPageActivity extends AppCompatActivity {
    FloatingActionButton addEvent;
    ListView eventList;
   EventList EventAdapter;
    FirebaseFirestore db;
    ArrayList<Event> eventDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        addEvent = findViewById(R.id.add_event_button);
        Intent i = getIntent();
        String selected_title = i.getStringExtra("habit");
        eventList = findViewById(R.id.all_event_list);

        final Intent addeventIntent = new Intent(this, AddEventActivity.class);
        eventDataList = new ArrayList<>();
        EventAdapter = new EventList(this, eventDataList);
        eventList.setAdapter(EventAdapter);
        db = FirebaseFirestore.getInstance();

        final CollectionReference EventListReference = db.collection("Users")
                .document("DefaultUser").collection("HabitList").document(selected_title).collection("Eventlist");



        EventListReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                eventDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String event = (String) doc.getData().get("event");
                    Timestamp Date = (Timestamp) doc.getData().get("date");
                    Date dateStart = Date.toDate();

                    eventDataList.add(new Event(event,dateStart));
                }
                EventAdapter.notifyDataSetChanged();

            }
        });
        final Intent view_editEventIntent = new Intent(this, ViewEditEventActivity.class);
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                view_editHabitIntent.putExtra(EXTRA_MESSAGE, habitDataList.get(position));
                view_editEventIntent.putExtra("event", eventDataList.get(position).getEvent());
                view_editEventIntent.putExtra("habit", selected_title);

                startActivity(view_editEventIntent);

            }

        });



        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText( getApplicationContext(),  selected_title, Toast.LENGTH_SHORT).show();
                addeventIntent.putExtra("habit", selected_title);
                //view_editHabitIntent.putExtra("habit", habitDataList.get(position).getTitle());

                startActivity(addeventIntent);
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