package com.example.have_it;

/**
 *This is the class for managing the information of event
 *@author songkunguo
 */
public class Event{
    /**
     *This is the title of event, of class {@link String}
     */
    private String event;
    /**
     *This is the date of event, of class {@link String}
     */
    private String date;

    /**
     *This is the constructor of the {@link Event}
     * @param event @see event, {@link String}, give the event title
     * @param date @see date, {@link String}, give the event date
     */
    public Event(String event,  String date) {
        this.event = event;
        this.date = date;
    }

    /**
     *This is getter for event
     * @return Returns {@link String} {@link Event#event}
     */
    public String getEvent() {
        return event;
    }

    /**
     *This is Setter for event
     * @return Returns {@link String} {@link Event#event}
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     *This is getter for event DATE
     * @return Returns {@link String} {@link Event#date}
     */
    public String getDate() {
        return date;
    }

    /**
     *This is setter for event DATE
     * @param date {@link String}, set {@link Event#date}
     */
    public void setDate(String date) {
        this.date = date;
    }
}