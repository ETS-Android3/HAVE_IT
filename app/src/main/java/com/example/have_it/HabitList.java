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

/**
 *This is the list of {@link Habit}, extends {@link ArrayAdapter} of {@link Habit}
 * @author yulingshen
 */
public class HabitList extends ArrayAdapter<Habit> {
    /**
     *This is the array list for habit data, of class {@link ArrayList}
     */
    private ArrayList<Habit> habits;
    /**
     *This is the current context, of class {@link Context}
     */
    private Context context;

    /**
     *This is the constructor of {@link HabitList}
     * @param context @see context, {@link Context}, give the context
     * @param habits @see habits, {@link ArrayList}, give the habit data
     */
    public HabitList( Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }

    /**
     *This method is invoked when a list of habits is to be shown in {@link HabitPageActivity}
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
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

    /**
     *The method for getting a habit list of the habits to be done today
     * This is expected to be called by {@link HabitPageActivity}
     * @return {@link ArrayList} The habit list for today
     */
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

    /**
     *The method for getting a habit list of the habits that are public
     *
     * @return {@link ArrayList} The habit list for public habits
     */
    public ArrayList<Habit> getPublicHabits(){
        ArrayList<Habit> result = new ArrayList<Habit>();
        for (Habit each : habits){
            if (each.getPublicity()){
                result.add(each);
            }
        }
        return result;
    }
}
