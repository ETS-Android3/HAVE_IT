package com.example.have_it;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 *This is the class for managing the information of habit
 *@author yulingshen
 */
public class Habit{
    /**
     * This is the title of habit, of class {@link String}
     */
    private String title;
    /**
     * This is the reason of habit, of class {@link String}
     */
    private String reason;
    /**
     * This is the start date of habit, of class {@link Date}
     */
    private Date dateStart;
    /**
     * This is the weekdays information of habit, of class {@link ArrayList}
     */
    private ArrayList<Boolean> weekdayReg;


    /**
     * This is the constructor of the {@link Habit}
     * @param title @see title, {@link String}, give the title
     * @param reason @see reason, {@link String}, give the reason
     * @param dateStart @see dateStart, {@link String}, give the start date
     * @param weekdayReg @see weekdayReg, {@link String}, give the weekdays selection
     */
    public Habit(String title, String reason, Date dateStart, ArrayList<Boolean> weekdayReg) {
        this.title = title;
        this.reason = reason;
        this.dateStart = dateStart;
        if (weekdayReg.size() !=7){
            Boolean[] defaultReg= {false, false, false, false, false, false, false};
            this.weekdayReg = new ArrayList<>(Arrays.asList(defaultReg));
        }
        this.weekdayReg = weekdayReg;
    }

    /**
     *This is getter for title
     * @return Returns {@link String} {@link Habit#title}
     */
    public String getTitle() {
        return title;
    }

    /**
     *This is setter for title
     * @param title {@link String}, set {@link Habit#title}
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *This is getter for reason
     * @return Returns {@link String} {@link Habit#reason}
     */
    public String getReason() {
        return reason;
    }

    /**
     *This is setter for reason
     * @param reason {@link String}, set {@link Habit#reason}
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     *This is getter for start date
     * @return Returns {@link Date} {@link Habit#dateStart}
     */
    public Date getDateStart() {
        return dateStart;
    }

    /**
     *This is setter for start date
     * @param dateStart {@link Date}, set {@link Habit#dateStart}
     */
    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    /**
     *This is getter for weekdays selection
     * @return Returns {@link ArrayList} {@link Habit#weekdayReg}
     */
    public ArrayList<Boolean> getWeekdayReg() {
        return weekdayReg;
    }

    /**
     *This is setter for weekdays selection
     * @param weekdayReg {@link ArrayList}, set {@link Habit#weekdayReg}
     */
    public void setWeekdayReg(ArrayList<Boolean> weekdayReg) {
        this.weekdayReg = weekdayReg;
    }


}
