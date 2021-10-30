package com.example.have_it;

import static org.junit.Assert.*;

import android.app.Activity;
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

public class HabitActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<HabitPageActivity> rule =
            new ActivityTestRule<>(HabitPageActivity.class,true,
                    true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

/*    @Test
    public void checkHabitPageActivity(){
        // this checks that the current activity is HabitActivityTest
        // otherwise an error is thrown
        solo.assertCurrentActivity("Wrong", HabitActivityTest.class);

        solo.clickOnView(solo.getView(R.id.add_habit_button));

        //write something on the editTest
        solo.enterText((EditText)solo.getView(R.id.editText_name),
                "Edmonton");
        solo.clickOnView(solo.getView(R.id.button_confirm));

        solo.clearEditText((EditText)solo.getView(R.id.editText_name));

        //if everything goes well then we should see Edmonton on our listView
        //assertTrue test
        // the time is in milli second, so 2000 is 2 seconds
        // this wait for a text on the screen
*//*        assertTrue(solo.waitForText("Edmonton",
                1, 2000));*//*

        //clears up the screen
        solo.clickOnView(solo.getView(R.id.button_clear));

        //assertFalse test
        //solo,searchText("Edmonton") should return false and when assertFalse
        //gets a false then it returns true
        assertFalse(solo.searchText("Edmonton"));
    }*/


    @Test
    public void checkAddAndDeleteHabit(){
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
        solo.clickOnButton(2);
        solo.waitForDialogToClose();

        //we assume you are going to perform your habit everyday except on the weekends
        solo.clickOnView(solo.getView(R.id.add_habit_button));

        solo.assertCurrentActivity("Wrong", HabitPageActivity.class);
        solo.clickInList(2,1);

        //Testing if the new habit we added is appearing in the EditActivity or not
        solo.assertCurrentActivity("Wrong", ViewEditHabitActivity.class);
        TextView view = (TextView)solo.getView(R.id.habit_title_editText_viewedit);
        assertEquals("brush teeth", view.getText().toString());
        view = (TextView)solo.getView(R.id.habit_reason_editText_viewedit);
        assertEquals("because I want to", view.getText().toString());
        view = (TextView)solo.getView(R.id.habit_start_date_viewedit);
        assertEquals("2021-12-11", view.getText().toString());

        //now we click on the delete button and check that the habit no longer exists
        solo.clickOnView(solo.getView(R.id.delete_button));
        solo.assertCurrentActivity("Wrong", HabitPageActivity.class);

        //This returns false if we see the title of our habit, if the deletion
        //was ok, then a false statement is inserted to assertfalse and a true
        //is returned
        assertFalse(solo.searchText("brush teeth"));
    }


    @Test
    public void checkEditHabit(){
        solo.clickInList(2,1);

        // we already checked that everything is there so we are not going to check again
        // now we just change everything first then check if it is right
        solo.clearEditText((EditText)solo.getView(R.id.habit_title_editText_viewedit));
        solo.clearEditText((EditText)solo.getView(R.id.habit_reason_editText_viewedit));
        solo.enterText((EditText)solo.getView(R.id.habit_title_editText_viewedit),
                "shower");
        solo.enterText((EditText)solo.getView(R.id.habit_reason_editText_viewedit),
                "no teeth");
        solo.clickOnView(solo.getView(R.id.habit_start_date_viewedit));
        solo.setDatePicker(0, 2022, 8, 8);
        solo.clickOnButton(2);
        solo.waitForDialogToClose();
        solo.clickOnView(solo.getView(R.id.confirm_button_viewedit));

        //now we have our thing updated and just check again to see if everything is right
        solo.clickInList(2,1);

        //Testing if the new habit we added is appearing in the EditActivity or not
        solo.assertCurrentActivity("Wrong", ViewEditHabitActivity.class);
        TextView view = (TextView)solo.getView(R.id.habit_title_editText_viewedit);
        assertEquals("shower", view.getText().toString());
        view = (TextView)solo.getView(R.id.habit_reason_editText_viewedit);
        assertEquals("no teeth", view.getText().toString());
        view = (TextView)solo.getView(R.id.habit_start_date_viewedit);
        assertEquals("2022-09-08", view.getText().toString());

    }

}
