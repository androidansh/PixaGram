package com.example.insta_clone_firebase.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.adapter.ReelAdapter;

public class ShowReelFrag extends Fragment {

    ViewPager2 reelPager;
    ReelAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_reel, container, false);

        reelPager = view.findViewById(R.id.reelPager);
        adapter = new ReelAdapter(ShowPostFrag.allReel,getContext());
        reelPager.setAdapter(adapter);
        return view;
    }
}