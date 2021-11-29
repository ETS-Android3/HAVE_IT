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

public class IndicatorTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<UserLoginActivity> rule =
            new ActivityTestRule<>(UserLoginActivity.class,true,
                    true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                rule.getActivity());

        //write your email and password
        solo.enterText((EditText)solo.getView(R.id.email),
                "allenhonggod@gmail.com");
        solo.enterText((EditText)solo.getView(R.id.password),
                ".Allen1hong2god3");
        solo.clickOnView(solo.getView(R.id.signIn));
    }

    @Test
    public void OpenIndicator(){
        // the first parameter just means the line of your current listView
        // the index parameter is when you have multiple listViews, if your index is 0
        // you are selecting the listView on top and if your index is 1 your are selecting
        // the listView at the bottom
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
        solo.clickOnText("brush teeth");

        // just open the indicator button
        solo.clickOnView(solo.getView(R.id.indicator_button));
        solo.assertCurrentActivity("Wrong", IndicatorActivity.class);
        solo.goBack();

        //delete the habit we just created
        solo.clickOnView(solo.getView(R.id.delete_button));
    }
}