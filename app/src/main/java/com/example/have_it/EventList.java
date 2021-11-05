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

/**
 *This is the list of {@link Event}, extends {@link ArrayAdapter} of {@link Event}
 * @author songkunguo
 */
public class EventList extends ArrayAdapter<Event> {
    /**
     *This is the array list for event data, of class {@link ArrayList}
     */
    private ArrayList<Event> events;
    /**
     *This is the current context, of class {@link Context}
     */
    private Context context;

    /**
     *This is the contstructor of {@link EventList}
     * @param context @see context, {@link Context}, give the context
     * @param events @see habits, {@link ArrayList}, give the event data
     */
    public EventList( Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
    }

    /**
     *This method is invoked when a list of events is to be shown in {@link EventPageActivity}
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
            view = LayoutInflater.from(context).inflate(R.layout.event_list_content, parent,false);
        }
        Event event = events.get(position);
        TextView eventName = view.findViewById(R.id.event_title);
        TextView eventDate = view.findViewById(R.id.event_date);
        eventName.setText(event.getEvent());
        eventDate.setText(event.getDate());
        return view;
    }
}