package com.example.have_it;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FollowingHabitPageActivity extends AppCompatActivity {

    ListView followingHabitList;
    HabitList habitAdapter;
    ArrayList<Habit> habitDataList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_habit_page);
        followingHabitList = findViewById(R.id.following_habit_list);
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String UID = i.getStringExtra("UID");

        habitDataList = new ArrayList<>();
        habitAdapter = new HabitList(this,habitDataList);
        followingHabitList.setAdapter(habitAdapter);

        FollowingController.setUserHabit(UID, habitAdapter, habitDataList);
    }
}
