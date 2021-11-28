package com.example.have_it;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * The activity for the following user's habit
 * @author yulingshen
 */
public class FollowingHabitPageActivity extends AppCompatActivity {

    /**
     * A reference to all public habits' list view, of class {@link ListView}
     */
    ListView followingHabitList;

    /**
     * The adapter to the list view, of class {@link HabitList}
     */
    HabitList habitAdapter;

    /**
     * The list to store data for the habit adapter, of class {@link ArrayList}
     */
    ArrayList<Habit> habitDataList;

    /**
     *This is the method invoked when the activity starts
     * @param savedInstanceState {@link Bundle} used for its super class
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_habit_page);
        followingHabitList = findViewById(R.id.following_habit_list);
        Intent i = getIntent();
        String UID = i.getStringExtra("UID");

        habitDataList = new ArrayList<>();
        habitAdapter = new HabitList(this,habitDataList);
        followingHabitList.setAdapter(habitAdapter);

        FollowingController.setUserHabit(UID, habitAdapter, habitDataList);

        final Intent indicatorIntent = new Intent(this,FollowingIndicatorActivity.class);

        followingHabitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                indicatorIntent.putExtra("UID",UID);
                indicatorIntent.putExtra("habit",habitDataList.get(position).getTitle());

                startActivity(indicatorIntent);
            }
        });
    }
}
