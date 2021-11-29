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

public class EventUnitTest {
    Event testEvent;

    // this is just a pointer for our Eventlist, we will use it shortly
    public EventList tmpEventList;

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
        assertNotNull(tmpEventList);
    }
}