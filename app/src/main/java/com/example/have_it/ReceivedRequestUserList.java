package com.example.have_it;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ReceivedRequestUserList extends ArrayAdapter<GeneralUser> {
    /**
     *This is the array list for general user data, of class {@link ArrayList}
     */
    private ArrayList<GeneralUser> generalUsers;
    /**
     *This is the current context, of class {@link Context}
     */
    private Context context;

    /**
     *This is the constructor of {@link FollowingUserList}
     * @param context @see context, {@link Context}, give the context
     * @param generalUsers @see habits, {@link ArrayList}, give the general user data
     */
    public ReceivedRequestUserList(Context context, ArrayList<GeneralUser> generalUsers) {
        super(context, 0, generalUsers);
        this.generalUsers = generalUsers;
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
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.user_list_content, parent,false);
        }
        GeneralUser generalUser = generalUsers.get(position);
        TextView userName = view.findViewById(R.id.user_name);
        userName.setText(generalUser.getName());
        return view;
    }

    public ArrayList<String> getUID() {
        ArrayList<String> result = new ArrayList<>();
        for (GeneralUser each : generalUsers) {
            result.add(each.getUID());
        }
        return result;
    }
}
