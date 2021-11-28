package com.example.have_it;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * The custom fragment pager adapter
 * @author yulingshen
 */
public class FollowingSectionsPageAdapter extends FragmentPagerAdapter {

    /**
     * The list of all fragments used, of class {@link List}
     */
    private final List<Fragment> fragmentList = new ArrayList<>();

    /**
     * The constructor
     * @param fm {@link FragmentManager}, for its super constructor
     */
    public FollowingSectionsPageAdapter(@NonNull FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    /**
     * The method for getting a fragment
     * @param position {@link int}, the position
     * @return {@link Fragment}, the fragment to get
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    /**
     * The method for getting total number of fragments
     * @return {@link int}, the number of fragments
     */
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /**
     * The method for adding a new fragment to the tabs
     * @param fragment {@link Fragment}, the fragment to set
     */
    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }
}
