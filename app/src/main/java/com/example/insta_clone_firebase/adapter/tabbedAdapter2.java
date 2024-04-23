package com.example.insta_clone_firebase.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.insta_clone_firebase.post_reel.AddPostFrag;
import com.example.insta_clone_firebase.post_reel.AddReelFrag;

public class tabbedAdapter2 extends FragmentPagerAdapter {

    private int numOfTabs;

    public tabbedAdapter2(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AddPostFrag();
        }
        return new AddReelFrag(); // return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0: return "Add Post";
            case 1: return "Add Reel";
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

