package com.example.have_it;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class NewFollowingFragment extends Fragment {
    ListView allUsersList;
    GeneralUserList usersAdapter;
    ArrayList<GeneralUser> usersDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_following,container,false);

        allUsersList = view.findViewById(R.id.all_users_list);

        usersDataList = new ArrayList<>();
        usersAdapter = new GeneralUserList(this.getActivity(),usersDataList);
        allUsersList.setAdapter(usersAdapter);

        FollowingController.getUserList(usersAdapter, usersDataList);

        return view;
    }
}
