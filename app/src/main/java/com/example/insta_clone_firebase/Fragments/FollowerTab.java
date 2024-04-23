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
import com.example.insta_clone_firebase.adapter.FollowerTabAdapter;
import com.example.insta_clone_firebase.model.Follower_Following_model;

import java.util.ArrayList;

public class FollowerTab extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Follower_Following_model> followerList;
    FollowerTabAdapter adapter;

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.fragment_follower_tab, container, false);
       recyclerView = view.findViewById(R.id.followerTabRecyclerView);

       if(ShowFollowerFollowing.userModel.getFollowers() != null){
           System.out.println("Follower num = " + ShowFollowerFollowing.userModel.getFollowers().size());
           followerList = ShowFollowerFollowing.userModel.getFollowers();
       }else{
           System.out.println("zero followers");
           followerList = new ArrayList<>();
       }

       adapter = new FollowerTabAdapter(followerList,getContext());
       recyclerView.setAdapter(adapter);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       return view;
    }
}