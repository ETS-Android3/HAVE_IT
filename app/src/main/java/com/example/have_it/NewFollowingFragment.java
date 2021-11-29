package com.example.have_it;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * Fragment for sending new following request
 * @author yulingshen
 */
public class NewFollowingFragment extends Fragment {
    /**
     * A reference to all users list view, of class {@link ListView}
     */
    ListView allUsersList;
    /**
     * Adapter for list view, of class {@link NewFollowUserList}
     */
    NewFollowUserList usersAdapter;
    /**
     * The list to store data for the user adapter, of class {@link ArrayList}
     */
    ArrayList<NewFollowUser> usersDataList;
    /**
     * A reference to search view for users, of class {@link SearchView}
     */
    SearchView searchUserText;

    /**
     * Method invoked when creating the fragment
     * @param inflater {@link LayoutInflater} used for setting layout
     * @param container {@link ViewGroup} used for setting layout
     * @param savedInstanceState {@link Bundle}
     * @return {@link View} the view content
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_following,container,false);

        allUsersList = view.findViewById(R.id.all_users_list);
        searchUserText = (SearchView) view.findViewById(R.id.name_filter_text);

        usersDataList = new ArrayList<>();
        usersAdapter = new NewFollowUserList(this.getActivity(),usersDataList);
        ArrayList<GeneralUser> followingDataList = new ArrayList<>();
        FollowingUserList followingAdapter = new FollowingUserList(this.getActivity(),followingDataList);
        ArrayList<RequestedUser> requestedDataList = new ArrayList<>();
        RequestedUserList requestedAdapter = new RequestedUserList(this.getActivity(),requestedDataList);
        allUsersList.setAdapter(usersAdapter);

        FollowingController.getRequestedList(requestedAdapter,requestedDataList);
        FollowingController.getNowFollowing(followingAdapter,followingDataList);

        FollowingController.getUserList(usersAdapter, usersDataList, requestedAdapter, followingAdapter);

        searchUserText.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                usersAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return view;
    }
}
