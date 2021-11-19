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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class NowFollowingFragment extends Fragment implements DatabaseUserReference{
    ListView nowFollowingList;
    GeneralUserList nowFollowingAdapter;
    ArrayList<GeneralUser> userDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_following,container,false);

        nowFollowingList = view.findViewById(R.id.following_user_list);

        userDataList = new ArrayList<>();
        nowFollowingAdapter = new GeneralUserList(this.getActivity(),userDataList);
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
