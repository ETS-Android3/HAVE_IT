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
 * The custom adapter for requested users
 * @author yulingshen
 */
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
     * @param UsersData {@link ArrayList}, give the user data
     */
    public RequestedUserList(Context context, ArrayList<RequestedUser> UsersData) {
        super(context, 0, UsersData);
        this.usersData = UsersData;
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
        View view = LayoutInflater.from(context).inflate(R.layout.request_reply_content, parent,false);
        RequestedUser user = usersData.get(position);
        TextView replyContext = view.findViewById(R.id.reply_status);
        Button confirmButton = view.findViewById(R.id.confirm_button);

        if(user.isReplied()){
            confirmButton.setVisibility(View.VISIBLE);
            if (user.isAllowed()){
                replyContext.setText(user.getName() + " allowed your request");
            }
            else{
                replyContext.setText(user.getName() + " rejected your request");
            }
        }
        else{
            replyContext.setText(user.getName() + " has not yet replied");
            confirmButton.setVisibility(View.INVISIBLE);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowingController.confirmReply(user);
            }
        });

        return view;
    }

    /**
     * Method invoked to get list of all UIDs of following users
     * @return {@link ArrayList}, the list of UIDs
     */
    public ArrayList<String> getUID(){
        ArrayList<String> result = new ArrayList<>();
        for (RequestedUser each : usersData){
            result.add(each.getUID());
        }
        return result;
    }
}
