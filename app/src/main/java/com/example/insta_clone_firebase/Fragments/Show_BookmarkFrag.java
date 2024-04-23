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
import com.example.insta_clone_firebase.activities.bookmark_post_activity;
import com.example.insta_clone_firebase.adapter.allPostAdapter;
import com.example.insta_clone_firebase.adapter.bookmark_big_post_adapter;


public class Show_BookmarkFrag extends Fragment {

    private static RecyclerView recyclerView;
    private allPostAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show__bookmark, container, false);

        recyclerView = view.findViewById(R.id.bookmark_recycler2);
        adapter = new allPostAdapter(bookmark_post_activity.savedPost,getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.smoothScrollToPosition(bookmark_post_activity.BOOKMARK_POST_NUM);
        return view;
    }

    public static void goToBookmark(int position){
        recyclerView.smoothScrollToPosition(position);
    }
}