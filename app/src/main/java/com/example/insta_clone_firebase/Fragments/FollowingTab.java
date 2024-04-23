package com.example.insta_clone_firebase.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.activities.ShowFollowerFollowing;
import com.example.insta_clone_firebase.adapter.FollowingTabAdapter;
import com.example.insta_clone_firebase.model.Follower_Following_model;

import java.util.ArrayList;


public class FollowingTab extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Follower_Following_model> followingList;
    FollowingTabAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_following_tab, container, false);
        recyclerView= view.findViewById(R.id.followingTabRecyclerView);

        if(ShowFollowerFollowing.userModel.getFollowings() != null){
            followingList = ShowFollowerFollowing.userModel.getFollowings();
        }else{
            followingList = new ArrayList<>();
        }

        adapter = new FollowingTabAdapter(followingList,getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}