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

public class FollowingRequestReplyFragment extends Fragment {
    ListView requestReplyList;
    RequestedUserList requestedUserAdapter;
    ArrayList<RequestedUser> userDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following_request_reply,container,false);

        userDataList = new ArrayList<>();
        requestedUserAdapter = new RequestedUserList(this.getActivity(),userDataList);
        requestReplyList = view.findViewById(R.id.following_request_reply_list);
        requestReplyList.setAdapter(requestedUserAdapter);

        FollowingController.getRequestedList(requestedUserAdapter,userDataList);

        return view;
    }
}
