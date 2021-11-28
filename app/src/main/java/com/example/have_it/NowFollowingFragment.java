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

/**
 * Fragment for users now following
 * @author yulingshen
 */
public class NowFollowingFragment extends Fragment implements DatabaseUserReference{
    /**
     * A reference to now following users list view, of class {@link ListView}
     */
    ListView nowFollowingList;
    /**
     * Adapter for list view, of class {@link FollowingUserList}
     */
    FollowingUserList nowFollowingAdapter;
    /**
     * The list to store data for the user adapter, of class {@link ArrayList}
     */
    ArrayList<GeneralUser> userDataList;

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
        View view = inflater.inflate(R.layout.fragment_now_following,container,false);

        nowFollowingList = view.findViewById(R.id.following_user_list);

        userDataList = new ArrayList<>();
        nowFollowingAdapter = new FollowingUserList(this.getActivity(),userDataList);
        nowFollowingList.setAdapter(nowFollowingAdapter);

        FollowingController.getNowFollowing(nowFollowingAdapter, userDataList);

        final Intent followingHabitIntent = new Intent (this.getActivity(), FollowingHabitPageActivity.class);

        nowFollowingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                followingHabitIntent.putExtra("UID", userDataList.get(position).getUID());
                followingHabitIntent.putExtra("name", userDataList.get(position).getName());
                startActivity(followingHabitIntent);
            }
        });

        return view;
    }
}
