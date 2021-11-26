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

public class NewFollowingFragment extends Fragment {
    ListView allUsersList;
    NewFollowUserList usersAdapter;
    ArrayList<NewFollowUser> usersDataList;
    SearchView searchUserText;

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
