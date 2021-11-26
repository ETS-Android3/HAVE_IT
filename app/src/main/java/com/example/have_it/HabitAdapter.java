package com.example.have_it;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.Calendar;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {

    private ArrayList<Habit> habits;
    private Context context;

    // Constructor
    public HabitAdapter(Context context, ArrayList<Habit> habits) {
        this.context = context;
        this.habits = habits;
    }

    @NonNull
    @Override
    public HabitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.habit_card_layout,parent,false);
        return new ViewHolder(context, view, habits);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitAdapter.ViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.habitTitle.setText(habit.getTitle());
//        holder.courseNameTV.setText("HHH");
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, DatabaseUserReference {
        private TextView habitTitle;
        private Context context;
        private ArrayList<Habit> habits;
        final Intent viewEditHabitIntent;


        public ViewHolder(Context context, @NonNull View itemView, ArrayList<Habit> habits) {
            super(itemView);
            habitTitle = itemView.findViewById(R.id.habit_title);
            this.context = context;
            this.habits = habits;

            viewEditHabitIntent = new Intent(context, ViewEditHabitActivity.class);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            viewEditHabitIntent.putExtra("habit", habitTitle.getText());

            final CollectionReference habitListReference = db.collection("Users")
                    .document(logged.getUID()).collection("HabitList");

            for(Habit habit: habits){
                habitListReference.document(habit.getTitle())
                        .update("order", habits.indexOf(habit));
            }


            context.startActivity(viewEditHabitIntent);
        }
    }

}
