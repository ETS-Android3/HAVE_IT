package com.example.have_it;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;

public class HabitList extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habits;
    private Context context;

    public HabitList( Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.habit_list_content, parent,false);
        }
        Habit habit = habits.get(position);
        TextView habitName = view.findViewById(R.id.habit_title);
        habitName.setText(habit.getTitle());
        return view;
    }

    //The method for getting a habit list of the habits to be done today
    public ArrayList<Habit> getTodayHabits(){
        ArrayList<Habit> result = new ArrayList<Habit>();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        for (Habit each : habits){
            if (each.getWeekdayReg().get(day-1)){
                result.add(each);
            }
        }
        return result;
    }
}
