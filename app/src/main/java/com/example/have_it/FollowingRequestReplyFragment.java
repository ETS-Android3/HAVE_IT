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

/**
 * Fragment for the following requests replies
 * @author yulingshen
 */
public class FollowingRequestReplyFragment extends Fragment {

    /**
     * A reference to following user request reply list view, of class {@link ListView}
     */
    ListView requestReplyList;
    /**
     * Adapter for list view, of class {@link RequestedUserList}
     */
    RequestedUserList requestedUserAdapter;
    /**
     * The list to store data for the user adapter, of class {@link ArrayList}
     */
    ArrayList<RequestedUser> userDataList;

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
        View view = inflater.inflate(R.layout.fragment_following_request_reply,container,false);

        userDataList = new ArrayList<>();
        requestedUserAdapter = new RequestedUserList(this.getActivity(),userDataList);
        requestReplyList = view.findViewById(R.id.following_request_reply_list);
        requestReplyList.setAdapter(requestedUserAdapter);

        FollowingController.getRequestedList(requestedUserAdapter,userDataList);

        return view;
    }
}
