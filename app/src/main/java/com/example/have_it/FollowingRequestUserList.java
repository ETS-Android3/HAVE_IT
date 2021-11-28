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

/**
 * The custom adapter for following requests
 * @author yulingshen
 */
public class FollowingRequestUserList extends ArrayAdapter<GeneralUser> {
    /**
     *This is the array list for general user data, of class {@link ArrayList}
     */
    private ArrayList<GeneralUser> usersData;
    /**
     *This is the current context, of class {@link Context}
     */
    private Context context;

    /**
     *This is the constructor of {@link FollowingUserList}
     * @param context @see context, {@link Context}, give the context
     * @param usersData {@link ArrayList}, give the general user data
     */
    public FollowingRequestUserList(Context context, ArrayList<GeneralUser> usersData) {
        super(context, 0, usersData);
        this.usersData = usersData;
        this.context = context;
    }

    /**
     *This method is invoked when a list of users is to be shown in {@link FollowingPageActivity}
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.following_request_content, parent,false);
        GeneralUser user = usersData.get(position);
        TextView userName = view.findViewById(R.id.user_name);
        Button requestButton = view.findViewById(R.id.confirm_button);
        Button rejectButton = view.findViewById(R.id.reject_button);
        userName.setText(user.getName());

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowingController.confirmFollowingRequest(user);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowingController.rejectFollowingRequest(user);
            }
        });

        return view;
    }
}
