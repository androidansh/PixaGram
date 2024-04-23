package com.example.insta_clone_firebase.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.insta_clone_firebase.Fragments.FollowerTab;
import com.example.insta_clone_firebase.Fragments.FollowingTab;

public class tabbedAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public tabbedAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new FollowingTab();
        }
        return new FollowerTab(); // return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0: return "Followers";
            case 1: return "Followings";
            default: return null; // return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
