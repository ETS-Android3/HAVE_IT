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

public class LoginAddDeleteEditTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<UserLoginActivity> rule =
            new ActivityTestRule<>(UserLoginActivity.class,true,
                    true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                rule.getActivity());
    }

    @Test
    public void checkLoginAlreadyRegistered(){
        // the first activity must be the login dialog
        solo.assertCurrentActivity("Wrong", UserLoginActivity.class);
        //write your email and password
        solo.enterText((EditText)solo.getView(R.id.email),
                "allenhonggod@gmail.com");
        solo.enterText((EditText)solo.getView(R.id.password),
                ".Allen1hong2god3");
        solo.clickOnView(solo.getView(R.id.signIn));
        solo.assertCurrentActivity("Wrong", HabitPageActivity.class);

    }

    @Test
    public void checkLoginNotRegistered(){
        solo.enterText((EditText)solo.getView(R.id.email),
                "Bie_e_xin_wo@gmail.com");
        solo.enterText((EditText)solo.getView(R.id.password),
                "6666666666666666");
        solo.clickOnView(solo.getView(R.id.signIn));

        // since the user is not registered, we should remain on the login activity
        solo.assertCurrentActivity("Wrong", UserLoginActivity.class);

    }

    @Test
    public void checkLoginInputs(){
        solo.assertCurrentActivity("Wrong", UserLoginActivity.class);

        //This is not how you write emails and therefore the format error should appear
        solo.enterText((EditText)solo.getView(R.id.email),
                "caonimashabi.com");
        solo.clickOnView(solo.getView(R.id.signIn));
        assertTrue(solo.waitForText("Please enter a valid email!"));

        //let's say I forgot to enter my email
        solo.clearEditText((EditText)solo.getView(R.id.email));
        solo.clickOnView(solo.getView(R.id.signIn));
        assertTrue(solo.waitForText("Email address is required!"));

        //let's say I forgot to enter my password
        solo.enterText((EditText)solo.getView(R.id.email),
                "allenhonggod@gmail.com");
        solo.clickOnView(solo.getView(R.id.signIn));
        assertTrue(solo.waitForText("Password is required!"));

        //let's say my keyword stopped working when I was typing my password which should
        //be more than 6 characters, but now since my keyboard stopped working the second half
        //of my password is not received and therefore my password length is less than 6
        solo.enterText((EditText)solo.getView(R.id.password),
                "cnm");
        solo.clickOnView(solo.getView(R.id.signIn));
        assertTrue(solo.waitForText("Minimum password length is 6 characters!"));

    }

    @Test
    public void checkAddAndDeleteHabit(){
        //login in first
        solo.assertCurrentActivity("Wrong", UserLoginActivity.class);
        solo.enterText((EditText)solo.getView(R.id.email),
                "allenhonggod@gmail.com");
        solo.enterText((EditText)solo.getView(R.id.password),
                ".Allen1hong2god3");
        solo.clickOnView(solo.getView(R.id.signIn));

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

        //Testing if the new habit we added is appearing in the EditActivity or not
        solo.assertCurrentActivity("Wrong", ViewEditHabitActivity.class);
        TextView view = (TextView)solo.getView(R.id.habit_title_editText_viewedit);
        solo.sleep(1000);
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
    public void checkEditHabitWithIndicatorViewTesting(){
        solo.enterText((EditText)solo.getView(R.id.email),
                "allenhonggod@gmail.com");
        solo.enterText((EditText)solo.getView(R.id.password),
                ".Allen1hong2god3");
        solo.clickOnView(solo.getView(R.id.signIn));

        solo.clickOnView(solo.getView(R.id.add_habit_button));
        solo.enterText((EditText)solo.getView(R.id.habit_title_editText),
                "brush teeth");
        solo.enterText((EditText)solo.getView(R.id.habit_reason_editText),
                "because I want to");

        //now click on the calendar and set the date
        solo.clickOnView(solo.getView(R.id.habit_start_date));
        solo.setDatePicker(0, 2021, 11, 11);
        solo.clickOnText("OK");
        solo.waitForDialogToClose();
        solo.clickOnView(solo.getView(R.id.add_habit_button));
        solo.clickOnText("brush teeth");

        //this is to be testing that the visual indicator can be accessed
        solo.clickOnView(solo.getView(R.id.indicator_button));
        solo.assertCurrentActivity("Wrong", IndicatorActivity.class);
        solo.goBack();

        // we already checked that everything is there so we are not going to check again
        // now we just change everything first then check if it is right
        solo.sleep(1000);
        solo.clearEditText((EditText)solo.getView(R.id.habit_title_editText_viewedit));
        solo.clearEditText((EditText)solo.getView(R.id.habit_reason_editText_viewedit));
        solo.enterText((EditText)solo.getView(R.id.habit_title_editText_viewedit),
                "shower");
        solo.enterText((EditText)solo.getView(R.id.habit_reason_editText_viewedit),
                "no teeth");
        solo.clickOnView(solo.getView(R.id.habit_start_date_viewedit));
        solo.setDatePicker(0, 2022, 8, 8);
        solo.clickOnText("OK");
        solo.waitForDialogToClose();
        solo.clickOnView(solo.getView(R.id.confirm_button_viewedit));

        //now we have our thing updated and just check again to see if everything is right
        solo.clickOnText("shower");

        //Testing if the new habit we added is appearing in the EditActivity or not
        solo.assertCurrentActivity("Wrong", ViewEditHabitActivity.class);
        TextView view = (TextView)solo.getView(R.id.habit_title_editText_viewedit);
        solo.sleep(1000);
        assertEquals("shower", view.getText().toString());
        view = (TextView)solo.getView(R.id.habit_reason_editText_viewedit);
        assertEquals("no teeth", view.getText().toString());
        view = (TextView)solo.getView(R.id.habit_start_date_viewedit);
        assertEquals("2022-09-08", view.getText().toString());
        solo.clickOnView(solo.getView(R.id.delete_button));

    }

    @Test
    public void duplicateTest(){
        // The way we check duplicates in habit is by their title, habits are not allowed
        // to have a same title. How we check event is different, two events are allowed
        // to have the same title but are not allowed to have the same date. If you add an
        // habit that it's title is already used by another habit your add won't work.
        solo.enterText((EditText)solo.getView(R.id.email),
                "allenhonggod@gmail.com");
        solo.enterText((EditText)solo.getView(R.id.password),
                ".Allen1hong2god3");
        solo.clickOnView(solo.getView(R.id.signIn));

        // add the first habit
        solo.clickOnView(solo.getView(R.id.add_habit_button));
        solo.enterText((EditText)solo.getView(R.id.habit_title_editText),
                "brush teeth");
        solo.enterText((EditText)solo.getView(R.id.habit_reason_editText),
                "because I want to");

        //now click on the calendar and set the date
        solo.clickOnView(solo.getView(R.id.habit_start_date));
        solo.setDatePicker(0, 2021, 11, 11);
        solo.clickOnText("OK");
        solo.waitForDialogToClose();
        solo.clickOnView(solo.getView(R.id.add_habit_button));


        // add the second habit, notice that everything is different with the first habit
        // except the title, meaning that as long as your title is used somewhere, you
        // can't add this habit. Even if your reason, date are unique.

        // the add habit button at the main page and the add habit button at the editing
        // page have the same id, my computer runs super fast so I have to make the
        // program sleep for 1 second inorder for my computer to distinguish the two button.
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.add_habit_button));
        solo.enterText((EditText)solo.getView(R.id.habit_title_editText),
                "brush teeth");
        solo.enterText((EditText)solo.getView(R.id.habit_reason_editText),
                "do it again");

        //now click on the calendar and set the date
        solo.clickOnView(solo.getView(R.id.habit_start_date));
        solo.setDatePicker(0, 2021, 10, 10);
        solo.clickOnText("OK");
        solo.waitForDialogToClose();
        solo.clickOnView(solo.getView(R.id.add_habit_button));

        // no error message will appear and you are simply stuck on the add habit page
        // unless you change your title
        assertTrue(solo.waitForActivity(AddHabitActivity.class));

        // delete all habit we added for future tests
        solo.goBack();
        solo.clickOnText("brush teeth");
        solo.clickOnView(solo.getView(R.id.delete_button));

    }
}