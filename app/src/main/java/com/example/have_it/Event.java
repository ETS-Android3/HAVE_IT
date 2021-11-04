package com.example.have_it;

/**
 *
 */
public class Event{
    /**
     *
     */
    private String event;
    /**
     *
     */
    private String date;

    /**
     *
     * @param event
     * @param date
     */
    public Event(String event,  String date) {
        this.event = event;
        this.date = date;
    }

    /**
     *
     * @return
     */
    public String getEvent() {
        return event;
    }

    /**
     *
     * @param event
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     *
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }
}