package com.example.have_it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class GeneralAndRequestedUserTest {
    public GeneralUser testGeneralUser;
    public FollowingRequestUserList tempFollowingRequestUserList;

    // reusable GeneralUser and followingRequestedUserList
    private GeneralUser mockNewGeneralUser(){
        return new GeneralUser("1585342","Allen");
    }

    private FollowingRequestUserList mockNewFollowingRequestedUserList(){
        FollowingRequestUserList newFollowingRequestUserList = new FollowingRequestUserList(null, new ArrayList<GeneralUser>(Arrays.asList(mockNewGeneralUser())));
        return newFollowingRequestUserList;
    }
    @Test
    public void TestSetGeneralUser(){
        testGeneralUser = mockNewGeneralUser();
        assertEquals("Allen", testGeneralUser.getName());
        assertEquals("1585342", testGeneralUser.getUID());
        testGeneralUser.setUID("123321");
        assertEquals("123321", testGeneralUser.getUID());
        testGeneralUser.setName("Wenzhuo Hong");
        assertEquals("Wenzhuo Hong", testGeneralUser.getName());
    }

    @Test
    public void TestNewFollowUserListInitialize(){
        tempFollowingRequestUserList = mockNewFollowingRequestedUserList();
        assertNotNull(tempFollowingRequestUserList);
    }

}
