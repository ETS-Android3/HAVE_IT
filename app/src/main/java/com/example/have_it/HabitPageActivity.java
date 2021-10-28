package com.example.have_it;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;

public class HabitPageActivity extends AppCompatActivity {
    ListView todayHabitList;
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayAdapter<Habit> todayHabitAdapter;
    ArrayList<Habit> habitDataList;
    FirebaseFirestore db;

    public static final String EXTRA_MESSAGE = "com.example.have_it.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_homepage);

        habitList = findViewById(R.id.all_habit_list);
        todayHabitList = findViewById(R.id.today_habit_list);

        final FloatingActionButton addCityButton = findViewById(R.id.add_habit_button);
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        habitDataList = new ArrayList<>();
        habitAdapter = new HabitList(this, habitDataList);

        habitList.setAdapter(habitAdapter);

        db = FirebaseFirestore.getInstance();

        final CollectionReference habitListReference = db.collection("Users")
                .document("DefaultUser").collection("HabitList");

        habitListReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                habitDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String title = (String) doc.getData().get("title");
                    String reason = (String) doc.getData().get("reason");
                    Timestamp startTimestamp = (Timestamp) doc.getData().get("dateStart");
                    Date dateStart = startTimestamp.toDate();
                    List<Boolean> weekdayRegArray = (List<Boolean>) doc.getData().get("weekdayReg");
                    habitDataList.add(new Habit(title,reason,dateStart, (ArrayList<Boolean>) weekdayRegArray));
                }
                habitAdapter.notifyDataSetChanged();
            }
        });
    }
}
