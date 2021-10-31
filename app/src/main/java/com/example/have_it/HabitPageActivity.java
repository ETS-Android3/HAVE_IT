package com.example.have_it;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    HabitList habitAdapter;
    HabitList todayHabitAdapter;
    ArrayList<Habit> habitDataList;
    ArrayList<Habit> todayHabitDataList;
    FirebaseFirestore db;

    public static final String EXTRA_MESSAGE = "com.example.have_it.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_homepage);

        habitList = findViewById(R.id.all_habit_list);
        todayHabitList = findViewById(R.id.today_habit_list);

        final FloatingActionButton addCityButton = findViewById(R.id.add_habit_button);
        final Intent addHabitIntent = new Intent(this, AddHabitActivity.class);
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(addHabitIntent);
            }
        });

        habitDataList = new ArrayList<>();
        habitAdapter = new HabitList(this, habitDataList);
        todayHabitDataList = new ArrayList<>();
        todayHabitAdapter = new HabitList(this, todayHabitDataList);

        habitList.setAdapter(habitAdapter);
        todayHabitList.setAdapter(todayHabitAdapter);

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
                todayHabitDataList.clear();
                ArrayList<Habit> today_temp = habitAdapter.getTodayHabits();
                for (Habit each : today_temp){
                    todayHabitDataList.add(each);
                }
                todayHabitAdapter.notifyDataSetChanged();
            }
        });



        final Intent view_editHabitIntent = new Intent(this, ViewEditHabitActivity.class);
        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                view_editHabitIntent.putExtra(EXTRA_MESSAGE, habitDataList.get(position));
                view_editHabitIntent.putExtra("habit", habitDataList.get(position).getTitle());

                startActivity(view_editHabitIntent);

            }

        });


    }
}
