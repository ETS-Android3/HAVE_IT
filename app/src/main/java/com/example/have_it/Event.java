package com.example.have_it;


import java.util.Date;

public class Event{
    private String event;

    private Date date;



    public Event(String event,  Date date) {
        this.event = event;

        this.date = date;

    }


    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
