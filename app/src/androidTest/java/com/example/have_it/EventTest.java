package com.example.have_it;

import static org.junit.Assert.*;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.Rule;

public class EventTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<UserLoginActivity> rule =
            new ActivityTestRule<>(UserLoginActivity.class,true,
                    true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                rule.getActivity());

        //login in first
        solo.assertCurrentActivity("Wrong", UserLoginActivity.class);
        solo.enterText((EditText)solo.getView(R.id.email),
                "allenhonggod@gmail.com");
        solo.enterText((EditText)solo.getView(R.id.password),
                ".Allen1hong2god3");
        solo.clickOnView(solo.getView(R.id.signIn));
    }

    @Test
    public void Add_Edit_DuplicateTest(){
        solo.clickOnView(solo.getView(R.id.add_habit_button));
        assertTrue(solo.waitForActivity(AddHabitActivity.class));

        //write something on the AddActivity
        solo.enterText((EditText)solo.getView(R.id.habit_title_editText),
                "brush teeth");
        solo.enterText((EditText)solo.getView(R.id.habit_reason_editText),
                "because I want to");

        //now click on the calendar and set the date
        solo.clickOnView(solo.getView(R.id.habit_start_date));
        solo.setDatePicker(0, 2021, 11, 11);
        solo.clickOnText("OK");
        solo.waitForDialogToClose();

        //we assume you are going to perform your habit everyday except on the weekends
        solo.clickOnView(solo.getView(R.id.add_habit_button));

        solo.assertCurrentActivity("Wrong", HabitPageActivity.class);
        solo.clickOnText("brush teeth");

        //we are in the editing page right now and let's click the eventList button
        solo.assertCurrentActivity("Wrong", ViewEditHabitActivity.class);
        solo.clickOnView(solo.getView(R.id.event_list_button));

        //event page activity is the java code that's responsible for adding event
        solo.assertCurrentActivity("Wrong", EventPageActivity.class);

        //now we click on the add button, AddEventActivity should pop up
        solo.clickOnView(solo.getView(R.id.add_event_button));
        solo.assertCurrentActivity("Wrong", AddEventActivity.class);

        //write something
        solo.enterText((EditText)solo.getView(R.id.event_editText),
                "I did it yeah!");

        //now click on the calendar and set the date and add it
        solo.clickOnView(solo.getView(R.id.date));
        solo.setDatePicker(0, 2021, 11, 11);
        solo.clickOnText("OK");
        solo.waitForDialogToClose();
        solo.clickOnView(solo.getView(R.id.addevent_button));

        //we should see our new event appearing on the list
        solo.assertCurrentActivity("Wrong", EventPageActivity.class);
        solo.clickInList(1);
        // and we should see our edit page pop up
        solo.assertCurrentActivity("Wrong", ViewEditEventActivity.class);

        //Testing if the new event we added is appearing or not
        TextView view = (TextView)solo.getView(R.id.event_editText_viewedit);
        solo.sleep(1000);

        // checking if the content of the event matches with what we wrote previously
        assertEquals("I did it yeah!", view.getText().toString());
        view = (TextView)solo.getView(R.id.habit_start_date_viewedit);
        assertEquals("2021-12-11", view.getText().toString());
        assertTrue(solo.waitForText("I did it yeah!", 1,
                2000));
        solo.clickOnView(solo.getView(R.id.confirm_button_viewedit));

        // The way we check duplicates in habit is by their title, habits are not allowed
        // to have a same title. How we check event is different, two events are allowed
        // to have the same title but are not allowed to have the same date. If you add an
        // event that it's date is already used by another event your add won't work.

        // adding an event with the same date
        solo.clickOnView(solo.getView(R.id.add_event_button));
        solo.enterText((EditText)solo.getView(R.id.event_editText),
                "I failed no!");
        solo.clickOnView(solo.getView(R.id.date));
        solo.setDatePicker(0, 2021, 11, 11);
        solo.clickOnText("OK");
        solo.waitForDialogToClose();
        solo.clickOnView(solo.getView(R.id.addevent_button));

        // your new event is not added, no error message will appear and you are still
        // at the EditEventActivity page
        solo.assertCurrentActivity("Wrong", AddEventActivity.class);

        solo.goBack();;

        //open the first event and delete it to check the delete function
        solo.clickInList(1);
        solo.clickOnView(solo.getView(R.id.delete_button));
        solo.sleep(1000);

        //go back to delete the testing habit
        solo.goBack();
        solo.clickOnView(solo.getView(R.id.delete_button));
    }
}
