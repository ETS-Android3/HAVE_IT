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


public class HaveItUnitTest {
    Event testEvent;
    private HabitList tempHabList;
    Habit testHabit;
    // this is just a pointer for our Eventlist, we will use it shortly
    public EventList tmpEventList;
    public User testUser;

    // mockHablist and mockHabit creates a resuable HabitList
    private ArrayList<Boolean> weekday = new ArrayList<>(Arrays.asList(false, false, false, false, false, false, false));
    private Habit mockHabit(){
        return new Habit("brush teeth", "because I want to", Calendar.getInstance().getTime(), weekday);
    }

    private HabitList mockHabList(){
        HabitList habitList = new HabitList(null, new ArrayList<Habit>(Arrays.asList(mockHabit())));
        return habitList;
    }

    // mockEvent and mock EventList creates a reusable EventList
    private Event mockEvent(){
        return new Event("brushing teeth", "2021-11-11");
    }

    private EventList mockEventList(){
        EventList eventList = new EventList(null, new ArrayList<Event>(Arrays.asList(mockEvent())));
        return eventList;
    }

    @Test
    public void TestEventInitialization(){
         testEvent = new Event("sleep fast", "2021-5-5");
        assertEquals("sleep fast", testEvent.getEvent());
        assertEquals("2021-5-5", testEvent.getDate());
    }

    @Test
    public void TestsetEvent(){
        testEvent = new Event("sleep fast", "2021-5-5");
        testEvent.setEvent("stay awake");
        testEvent.setDate("2021-5-6");
        assertEquals("stay awake", testEvent.getEvent());
        assertEquals("2021-5-6", testEvent.getDate());
    }

    @Test
    void TestEventListInitialize(){
        tmpEventList = mockEventList();
        assertEquals(1, tmpEventList.getEvents().size());
    }

    @Test
    void TestEventListAdding(){
        tmpEventList = mockEventList();
        tmpEventList.getEvents().add(new Event("wash my face","2021-11-12"));
        assertEquals(2, tmpEventList.getEvents().size());
    }


    @Test
    public void TestHabitInitialization(){
        testHabit = new Habit("play League", "I don't want to sleep",
                Calendar.getInstance().getTime(), new ArrayList<>(Arrays.asList(false, false, false, false, false, false, false)));
        assertEquals("play League", testHabit.getTitle());
        assertEquals("I don't want to sleep", testHabit.getReason());
        assertEquals(Calendar.getInstance().getTime().toString(), testHabit.getDateStart().toString());
        assertEquals(7, testHabit.getWeekdayReg().size());
    }

    @Test
    public void TestsetHabit(){
        testHabit = new Habit("play League", "I don't want to sleep",
                Calendar.getInstance().getTime(), new ArrayList<>(Arrays.asList(false, false, false, false, false, false, false)));
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
        assertEquals(1, tempHabList.getHabits().size());
    }

    @Test
    public void TestAddHabit(){
        tempHabList = mockHabList();
        tempHabList.getHabits().add(mockHabit());
        assertEquals(2, tempHabList.getHabits().size());
    }

    @Test
    public void TestUser(){
        testUser = User.getInstance();
        testUser.setUID("123321");
        assertEquals("123321", testUser.getUID());
    }



}
