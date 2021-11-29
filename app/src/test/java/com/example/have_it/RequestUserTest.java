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

public class RequestUserTest {

    public RequestedUser testRequestedUser;
    public RequestedUserList tempRequestedUserList;

    // reusable RequestedUser and RequestedUserList
    private RequestedUser mockRequestedUser(){
        return new RequestedUser("1585342","Allen",true,true);
    }

    private RequestedUserList mockNewRequestedUserList(){
        RequestedUserList newRequestedUserList = new RequestedUserList(null, new ArrayList<RequestedUser>(Arrays.asList(mockRequestedUser())));
        return newRequestedUserList;
    }

    @Test
    public void TestSetGeneralUser(){
        testRequestedUser = mockRequestedUser();
        assertTrue(testRequestedUser.isAllowed());
        assertTrue(testRequestedUser.isReplied());
        testRequestedUser.setAllowed(false);
        testRequestedUser.setReplied(false);
        assertFalse(testRequestedUser.isAllowed());
        assertFalse(testRequestedUser.isReplied());
    }

    @Test
    public void TestNewFollowUserListInitialize(){
        tempRequestedUserList = mockNewRequestedUserList();
        assertNotNull(tempRequestedUserList);
    }

    @Test
    public void TestUid(){
        tempRequestedUserList = mockNewRequestedUserList();
        assertEquals(Arrays.asList("1585342"),tempRequestedUserList.getUID());
    }
}