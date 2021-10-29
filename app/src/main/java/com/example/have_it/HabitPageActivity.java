package com.example.have_it;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HabitPageActivity extends AppCompatActivity {
    ListView habitList;
    ListView todayHabitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayAdapter<Habit> todayHabitAdapter;
    ArrayList<Habit> habitDataList;

    public static final String EXTRA_MESSAGE = "com.example.have_it.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_homepage);

        habitList = findViewById(R.id.all_habit_list);
        todayHabitList = findViewById(R.id.today_habit_list);

    }
}
