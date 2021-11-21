package com.example.have_it;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RequestedUserList extends ArrayAdapter<RequestedUser> {
    /**
     *This is the array list for user data, of class {@link ArrayList}
     */
    private ArrayList<RequestedUser> usersData;
    /**
     *This is the current context, of class {@link Context}
     */
    private Context context;

    /**
     *This is the constructor of {@link FollowingUserList}
     * @param context @see context, {@link Context}, give the context
     * @param UsersData @see habits, {@link ArrayList}, give the user data
     */
    public RequestedUserList(Context context, ArrayList<RequestedUser> UsersData) {
        super(context, 0, UsersData);
        this.usersData = UsersData;
        this.context = context;
    }

    public ArrayList<String> getUID(){
        ArrayList<String> result = new ArrayList<>();
        for (RequestedUser each : usersData){
            result.add(each.getUID());
        }
        return result;
    }
}
