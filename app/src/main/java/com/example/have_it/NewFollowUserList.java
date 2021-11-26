package com.example.have_it;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NewFollowUserList extends ArrayAdapter<NewFollowUser> implements Filterable {
    /**
     *This is the array list for user data, of class {@link ArrayList}
     */
    private ArrayList<NewFollowUser> usersData;
    /**
     *This is the array list for filtered user data, of class {@link ArrayList}
     */
    private ArrayList<NewFollowUser> usersDataFiltered;
    /**
     *This is the current context, of class {@link Context}
     */
    private Context context;

    /**
     *This is the constructor of {@link FollowingUserList}
     * @param context @see context, {@link Context}, give the context
     * @param UsersData @see habits, {@link ArrayList}, give the user data
     */
    public NewFollowUserList(Context context, ArrayList<NewFollowUser> UsersData) {
        super(context, 0, UsersData);
        this.usersData = UsersData;
        this.usersDataFiltered = UsersData;
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
        View view = LayoutInflater.from(context).inflate(R.layout.new_follow_user_content, parent,false);
        NewFollowUser user = usersDataFiltered.get(position);
        TextView userName = view.findViewById(R.id.user_name);
        TextView requestedText = view.findViewById(R.id.requested_text);
        TextView followingText = view.findViewById(R.id.following_text);
        Button requestButton = view.findViewById(R.id.request_button);
        userName.setText(user.getName());
        if(user.isFollowing()){
            requestButton.setVisibility(View.INVISIBLE);
            requestedText.setVisibility(View.INVISIBLE);
            followingText.setVisibility(View.VISIBLE);
        }
        else if (user.isRequested()){
            requestButton.setVisibility(View.INVISIBLE);
            requestedText.setVisibility(View.VISIBLE);
            followingText.setVisibility(View.INVISIBLE);
        }
        else{
            requestButton.setVisibility(View.VISIBLE);
            requestedText.setVisibility(View.INVISIBLE);
            followingText.setVisibility(View.INVISIBLE);
        }

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowingController.sendRequest(user);
                requestButton.setVisibility(View.INVISIBLE);
                requestedText.setVisibility(View.VISIBLE);
                followingText.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    @Override
    public int getCount(){
        return usersDataFiltered.size();
    }

    @Override
    public NewFollowUser getItem(int position){
        return usersDataFiltered.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length()==0){
                    filterResults.count = usersData.size();
                    filterResults.values = usersData;
                }
                else{
                    ArrayList<NewFollowUser> resultsUser = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();
                    for (NewFollowUser each : usersData){
                        if (each.getName().toLowerCase().contains(searchStr)){
                            resultsUser.add(each);
                        }
                    }
                    filterResults.count = resultsUser.size();
                    filterResults.values = resultsUser;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                usersDataFiltered = (ArrayList<NewFollowUser>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
