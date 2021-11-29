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

public class UserUnitTest {
    public User testUser;

    @Test
    public void TestUser(){
        testUser = User.getInstance();
        testUser.setUID("123321");
        assertEquals("123321", testUser.getUID());
    }
}
