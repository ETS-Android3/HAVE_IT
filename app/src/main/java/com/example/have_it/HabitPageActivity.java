package com.example.have_it;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *This is the activity for main page of viewing habits
 * @author yulingshen
 */
public class HabitPageActivity extends AppCompatActivity implements  FirestoreGetCollection, DatabaseUserReference{
    /**
     *A reference to today habit list view, of class {@link ListView}
     */
    RecyclerView todayHabitList;
    /**
     *A reference to all habit list view, of class {@link ListView}
     */
    RecyclerView habitList;
    /**
     *habit adapter, of class {@link HabitList}
     */
    HabitAdapter habitAdapter;
    /**
     *today habit adapter, of class {@link HabitList}
     */
    HabitAdapter todayHabitAdapter;
    /**
     *habit data list, of class {@link ArrayList}
     */
    ArrayList<Habit> habitDataList;
    /**
     *today data list, of class {@link ArrayList}
     */
    ArrayList<Habit> todayHabitDataList;
    /**
     * Reference to the bottom navigation menu, of class {@link BottomNavigationView}
     */
    BottomNavigationView bottomNavigationView;
    /**
     *The extra message used for intent, of class {@link String}
     */
    public static final String EXTRA_MESSAGE = "com.example.have_it.MESSAGE";


    /**
     *This is the method invoked when the activity starts
     * @param savedInstanceState {@link Bundle} used for its super class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_homepage);

        habitList = findViewById(R.id.all_habits);
        todayHabitList = findViewById(R.id.today_habits);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.habit_menu);


        habitDataList = new ArrayList<>();
        habitAdapter = new HabitAdapter(this, habitDataList);
        todayHabitDataList = new ArrayList<>();
        todayHabitAdapter = new HabitAdapter(this, todayHabitDataList);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        habitList.setLayoutManager(linearLayoutManager1);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        todayHabitList.setLayoutManager(linearLayoutManager2);

        habitList.setAdapter(habitAdapter);
        todayHabitList.setAdapter(todayHabitAdapter);

        getCollection();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(habitList);

        final FloatingActionButton addHabitButton = findViewById(R.id.add_habit_button);
        final Intent addHabitIntent = new Intent(this, AddHabitActivity.class);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addHabitIntent.putExtra("order", habitAdapter.getItemCount());
                upLoad();
                habitDataList.clear();
                startActivity(addHabitIntent);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.habit_menu:
                        break;

                    case R.id.following_menu:
                        final Intent followingIntent = new Intent(HabitPageActivity.this, FollowingPageActivity.class);
                        upLoad();
                        habitDataList.clear();
                        startActivity(followingIntent);
                        break;

                    case R.id.account_menu:
                        final Intent accountIntent = new Intent(HabitPageActivity.this, AccountPageActivity.class);
                        upLoad();
                        habitDataList.clear();
                        startActivity(accountIntent);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * This is the method for getting a reference to collection
     */
    @Override
    public void getCollection() {
        final CollectionReference habitListReference = db.collection("Users")
                .document(logged.getUID()).collection("HabitList");

        habitListReference.orderBy("order").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    Boolean publicity = (Boolean) doc.getData().get("publicity");
                    habitDataList.add(new Habit(title,reason,dateStart, (ArrayList<Boolean>) weekdayRegArray, publicity));

                }
                habitAdapter.notifyDataSetChanged();

                todayHabitDataList.clear();
                ArrayList<Habit> todayTemp = habitAdapter.getTodayHabits();
                for (Habit each : todayTemp){
                    todayHabitDataList.add(each);
                }

                todayHabitAdapter.notifyDataSetChanged();

            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(habitDataList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


    public void upLoad() {

        final CollectionReference habitListReference = db.collection("Users")
                .document(logged.getUID()).collection("HabitList");

        for(Habit habit: habitDataList){
            habitListReference.document(habit.getTitle())
                    .update("order", habitDataList.indexOf(habit));
        }

    }
}
