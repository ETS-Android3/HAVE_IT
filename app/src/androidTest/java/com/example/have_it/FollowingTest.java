package com.example.have_it;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class FollowingTest {
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

    // To test the following functionality we will use two accounts
    // step_1: login in to allen's account and send a following request to jianbang Chen
    // check the notification that jianbang chen didn't reply to allen yet

    // step_2: login in to jianbang chen's account and find allen's request then click the
    // request button

    // step_3: login in to allen's account again and make sure that allen cannot follow
    // jianbang again because jianbang already accepted our follow so therefore allen
    // cannot make the same request. Then check the notification bar that jianbang had
    // confirmed our rest.

    // step_4: de_follow chenjian bang so that the UI test can be run again.

    @Test
    public void FollowingTest(){
        // choose the following menu
        solo.clickOnText("Following");
        solo.assertCurrentActivity("Wrong", FollowingPageActivity.class);

        // the cell phone resolution is 1080 * 1920
        // clicking on the second element of the tab bar
        solo.clickOnScreen(400,250);

        // let's follow the first 5 people in our following list
        solo.clickOnView(solo.getView(R.id.request_button));
        solo.clickOnView(solo.getView(R.id.request_button));
        solo.clickOnView(solo.getView(R.id.request_button));
        solo.clickOnView(solo.getView(R.id.request_button));
        solo.clickOnView(solo.getView(R.id.request_button));

        // going to the notification bar we will see that JianBang chen has not replied
        solo.clickOnScreen(900,250);
        solo.waitForText("Jianbang Chen has not yet replied",1,2000);

        // logout and login to JianBang's account
        solo.clickOnText("Account");
        solo.clickOnView(solo.getView(R.id.logout));
        solo.enterText((EditText)solo.getView(R.id.email),
                "jianbang@ualberta.ca");
        solo.enterText((EditText)solo.getView(R.id.password),
                "cjb521");
        solo.clickOnView(solo.getView(R.id.signIn));

        // choose the following menu and confirm allen's request then logout
        solo.clickOnText("Following");
        solo.clickOnScreen(600,250);
        solo.clickOnScreen(600,650);
        solo.clickOnText("Account");
        solo.clickOnView(solo.getView(R.id.logout));

        // login to allen's account again and delete jianbang from our following list
        // by doing this we will allow this UI test to run again and the fact that we can
        // delete jianbang means that the following functionality worked properly.
        solo.enterText((EditText)solo.getView(R.id.email),
                "allenhonggod@gmail.com");
        solo.enterText((EditText)solo.getView(R.id.password),
                ".Allen1hong2god3");
        solo.clickOnView(solo.getView(R.id.signIn));
        solo.clickOnText("Following");
        solo.clickOnScreen(900,550);

        // go to the notification bar and confirm jianbang's acceptance to our request
        solo.clickOnScreen(900,250);
        solo.waitForText("Jianbang Chen allowed your request");
        solo.clickOnScreen(900,1100);
        solo.clickOnText("Account");
        solo.clickOnView(solo.getView(R.id.logout));
    }
}
