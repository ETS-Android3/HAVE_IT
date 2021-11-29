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

/**
 *This is the adapter corresponds to the list of {@link Habit}, extends {@link RecyclerView.Adapter} of {@link HabitAdapter.ViewHolder}
 * @author Ruiqing Tian
 */
public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {

    /**
     *This is the array list for habit data, of class {@link ArrayList}
     */
    private ArrayList<Habit> habits;
    /**
     *This is the current context, of class {@link Context}
     */
    private Context context;

    /**
     *This is the constructor of {@link HabitAdapter}
     * @param context @see context, {@link Context}, give the context
     * @param habits @see habits, {@link ArrayList}, give the habit data
     */
    // Constructor
    public HabitAdapter(Context context, ArrayList<Habit> habits) {
        this.context = context;
        this.habits = habits;
    }

    /**
     *This method is invoked when card view is created and ViewHolder is required
     * @param parent
     * @param viewType
     * @return {@link HabitAdapter.ViewHolder} a ViewHolder contains that list of habits and corresponds to view
     */
    @NonNull
    @Override
    public HabitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.habit_card_layout,parent,false);
        return new ViewHolder(context, view, habits);
    }

    /**
     *This method is invoked when card view is created and set the text on it via ViewHolder
     * @param holder
     * @param position
     * @return
     */
    @Override
    public void onBindViewHolder(@NonNull HabitAdapter.ViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.habitTitle.setText(habit.getTitle());
//        holder.courseNameTV.setText("HHH");
    }

    /**
     *This method is invoked when the total number of habits is needed
     * @return the total number of habits related to this adapter
     */
    @Override
    public int getItemCount() {
        return habits.size();
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
     *This is the class named ViewHolder corresponds to the list of {@link Habit}, extends {@link RecyclerView.ViewHolder}, implements {@link View.OnClickListener} {@link DatabaseUserReference }
     * @author ruiqingtian
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, DatabaseUserReference {
        private TextView habitTitle;
        private Context context;
        private ArrayList<Habit> habits;
        final Intent viewEditHabitIntent;


        /**
         *This is the constructor of {@link ViewHolder}
         * @param context @see context, {@link Context}, give the context
         *   @param itemView @see itemView, {@link View}, give view corresponding to the view in card view context
         * @param habits @see habits, {@link ArrayList}, give the habit data
         */
        public ViewHolder(Context context, @NonNull View itemView, ArrayList<Habit> habits) {
            super(itemView);
            habitTitle = itemView.findViewById(R.id.habit_title);
            this.context = context;
            this.habits = habits;

            viewEditHabitIntent = new Intent(context, ViewEditHabitActivity.class);
            itemView.setOnClickListener(this);
        }

        /**
         * To implement the clicking function on each card view, in our program, we use the click on card view to view details and edit/ delete habits
         *   @param view
         */
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
