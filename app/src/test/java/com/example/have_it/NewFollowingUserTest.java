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

public class NewFollowingUserTest {
    public NewFollowUser testFollowUser;
    NewFollowUserList tempNewFollowUserList;

    // reusable followingUser and followingUserList
    private NewFollowUser mockNewFollowingUser(){
        return new NewFollowUser("1585342","Allen",true,true);
    }

    private NewFollowUserList mockNewFollowUserList(){
        NewFollowUserList newFollowUserList = new NewFollowUserList(null, new ArrayList<NewFollowUser>(Arrays.asList(mockNewFollowingUser())));
        return newFollowUserList;
    }

    @Test
    public void TestSetFollowUser(){
        testFollowUser = mockNewFollowingUser();
        assertTrue(testFollowUser.isRequested());
        assertTrue(testFollowUser.isFollowing());
        testFollowUser.setFollowing(false);
        testFollowUser.setRequested(false);
        assertFalse(testFollowUser.isRequested());
        assertFalse(testFollowUser.isFollowing());
    }

    @Test
    public void TestNewFollowUserListInitialize(){
        tempNewFollowUserList = mockNewFollowUserList();
        assertNotNull(tempNewFollowUserList);
    }

    @Test
    public void TestGetCount(){
        tempNewFollowUserList = mockNewFollowUserList();
        assertEquals(1,tempNewFollowUserList.getCount());
    }
}
