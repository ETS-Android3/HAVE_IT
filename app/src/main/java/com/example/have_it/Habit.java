package com.example.have_it;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.io.Serializable;

/**
 *
 */
@SuppressWarnings("serial")
public class Habit{
    private String title;
    private String reason;
    private Date dateStart;
    private ArrayList<Boolean> weekdayReg;


    /**
     * @param title
     * @param reason
     * @param dateStart
     * @param weekdayReg
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
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public String getReason() {
        return reason;
    }

    /**
     *
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     *
     * @return
     */
    public Date getDateStart() {
        return dateStart;
    }

    /**
     *
     * @param dateStart
     */
    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    /**
     *
     * @return
     */
    public ArrayList<Boolean> getWeekdayReg() {
        return weekdayReg;
    }

    /**
     *
     * @param weekdayReg
     */
    public void setWeekdayReg(ArrayList<Boolean> weekdayReg) {
        this.weekdayReg = weekdayReg;
    }


}
