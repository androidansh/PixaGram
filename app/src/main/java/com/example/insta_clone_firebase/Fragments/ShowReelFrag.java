package com.example.insta_clone_firebase.Fragments;

import static com.example.insta_clone_firebase.activities.HomeScreenActivity.activeAccount;
import static com.example.insta_clone_firebase.activities.HomeScreenActivity.addPost;
import static com.example.insta_clone_firebase.activities.HomeScreenActivity.reel;
import static com.example.insta_clone_firebase.activities.HomeScreenActivity.searchUser;
import static com.example.insta_clone_firebase.activities.HomeScreenActivity.showPost;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.adapter.ReelAdapter;

public class ShowReelFrag extends Fragment {

    ViewPager2 reelPager;
    ReelAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_reel, container, false);

        HomeScreenActivity.windowFrame.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        HomeScreenActivity.windowFrame.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.bgColor));

        showPost.setBackgroundResource(R.drawable.home);
        searchUser.setBackgroundResource(R.drawable.find);
        activeAccount.setVisibility(View.INVISIBLE);
        addPost.setBackgroundResource(R.drawable.add);
        reel.setBackgroundResource(R.drawable.active_reels);

        reelPager = view.findViewById(R.id.reelPager);
        adapter = new ReelAdapter(ShowPostFrag.allReel,getContext());
        reelPager.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showPost.setBackgroundResource(R.drawable.home);
        searchUser.setBackgroundResource(R.drawable.find);
        activeAccount.setVisibility(View.INVISIBLE);
        addPost.setBackgroundResource(R.drawable.add);
        reel.setBackgroundResource(R.drawable.active_reels);
    }
}