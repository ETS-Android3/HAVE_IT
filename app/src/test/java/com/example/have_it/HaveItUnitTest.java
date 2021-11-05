package com.example.have_it;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class HaveItUnitTest {
    Event testEvent;
    private HabitList tempHabList;
    Habit testHabit;

/*    // mockHablist and mockHabit creates a resuable HabitList
    private ArrayList<Boolean> weekday = new ArrayList<>(Arrays.asList(false, false, false, false, false, false, false));
    private Habit mockHabit(){
        return new Habit("brush teeth", "because I want to", new Date("2021-11-14"), weekday);
    }

    private HabitList mockHabList(){
        HabitList habitList = new HabitList(null, new ArrayList<Habit>(Arrays.asList(mockHabit())));
        return habitList;
    }*/

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

/*    @Test
    public void TestGetTodayHabit(){
        tempHabList = mockHabList();
        assertEquals(1, tempHabList.getHabits().size());
    }

    @Test
    public void TestAddHabit(){
        tempHabList = mockHabList();
        tempHabList.getHabits().add(mockHabit());
        assertEquals(2, tempHabList.getHabits().size());
    }*/


    // this is just a pointer for our Eventlist, we will use it shortly
    public EventList tmpEventList;

    @Test
    void TestInitializeEvent(){
        tmpEventList = mockEventList();
        assertEquals(1, tmpEventList.getEvents().size());
    }

    @Test
    void TestAddingEvent(){
        tmpEventList = mockEventList();
        tmpEventList.getEvents().add(new Event("wash my face","2021-11-12"));
        assertEquals(2, tmpEventList.getEvents().size());
    }
}
