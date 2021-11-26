package com.example.have_it;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class FollowingRequestFragment extends Fragment {

    ListView followingRequestList;
    FollowingRequestUserList requestedUserAdapter;
    ArrayList<GeneralUser> userDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following_request,container,false);

        followingRequestList = view.findViewById(R.id.following_request_list);

        userDataList = new ArrayList<>();
        requestedUserAdapter = new FollowingRequestUserList(this.getActivity(),userDataList);
        followingRequestList.setAdapter(requestedUserAdapter);

        FollowingController.getFollowingRequest(requestedUserAdapter,userDataList);

        return view;
    }
}
