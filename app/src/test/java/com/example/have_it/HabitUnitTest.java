package com.example.have_it;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class HabitUnitTest {
    private HabitList tempHabList;
    Habit testHabit;

    // mockHablist and mockHabit creates a resuable HabitList
    private ArrayList<Boolean> weekday = new ArrayList<>(Arrays.asList(false, false, false, false, false, false, false));
    private Habit mockHabit(){
        return new Habit("brush teeth", "because I want to", Calendar.getInstance().getTime(), weekday, true);
    }

    private HabitList mockHabList(){
        HabitList habitList = new HabitList(null, new ArrayList<Habit>(Arrays.asList(mockHabit())));
        return habitList;
    }

    @Test
    public void TestHabitInitialization(){
        testHabit = new Habit("play League", "I don't want to sleep",
                Calendar.getInstance().getTime(), new ArrayList<>(Arrays.asList(false, false, false, false, false, false, false)), true);
        assertEquals("play League", testHabit.getTitle());
        assertEquals("I don't want to sleep", testHabit.getReason());
        assertEquals(Calendar.getInstance().getTime().toString(), testHabit.getDateStart().toString());
        assertEquals(7, testHabit.getWeekdayReg().size());
    }

    @Test
    public void TestsetHabit(){
        testHabit = new Habit("play League", "I don't want to sleep",
                Calendar.getInstance().getTime(), new ArrayList<>(Arrays.asList(false, false, false, false, false, false, false)), true);
        testHabit.setTitle("stay awake");
        testHabit.setReason("I don't want to play league");
        testHabit.setWeekdayReg(new ArrayList<>(Arrays.asList(false)));
        Date testDate = new Date();
        try {
            testDate = new SimpleDateFormat("yyyy-MM-dd")
                    .parse("2021-11-11");
        } catch (ParseException e){
        }
        testHabit.setDateStart(testDate);
        assertEquals("stay awake", testHabit.getTitle());
        assertEquals("I don't want to play league", testHabit.getReason());
        assertEquals("2021-11-11", new SimpleDateFormat("yyyy-MM-dd").format(testHabit.getDateStart()));
        assertEquals(1, testHabit.getWeekdayReg().size());
    }

    @Test
    public void TestHabitListInitialize(){
        tempHabList = mockHabList();
        assertNotNull(tempHabList);
    }

    @Test
    public void TestGetTodayHabit(){
        tempHabList = mockHabList();
        // the habit is not today, so today there should be no habit
        assertEquals(0,tempHabList.getTodayHabits().size());
    }
}