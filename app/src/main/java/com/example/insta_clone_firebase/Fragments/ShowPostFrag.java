package com.example.insta_clone_firebase.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.adapter.allPostAdapter;
import com.example.insta_clone_firebase.model.create_user_model;
import com.example.insta_clone_firebase.model.post_create;
import com.example.insta_clone_firebase.thread_package.get_all_post_storage;
import com.example.insta_clone_firebase.thread_package.get_all_reels;
import com.example.insta_clone_firebase.thread_package.get_user_db;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ShowPostFrag extends Fragment {
    private RecyclerView postRecycler;
    public static allPostAdapter adapter;
    private SwipeRefreshLayout swipe;
    public static ArrayList<post_create> allPost = new ArrayList<>();
    public static ArrayList<post_create> allReel = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_show_post, container, false);


        SharedPreferences pref3 = getContext().getSharedPreferences("Pref_User_Post", Context.MODE_PRIVATE);
        String jsonUserPost = pref3.getString("User_Post","");

        if(HomeScreenActivity.USER_POSTS.size() > 0 && jsonUserPost.equals("[]")){
            Gson gson = new Gson();
            System.out.println("Storing");
            SharedPreferences preferences1 = getContext().getSharedPreferences("Pref_User_Post", Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor1 = preferences1.edit();
            String json_post = gson.toJson(HomeScreenActivity.USER_POSTS);
            prefsEditor1.putString("User_Post", json_post);
            prefsEditor1.apply();
        }

        postRecycler = view.findViewById(R.id.allPostRecycler);
        swipe = view.findViewById(R.id.swipe);


        chk_insta_data();
        adapter = new allPostAdapter(allPost,getContext(),getActivity());
        postRecycler.setAdapter(adapter);
        postRecycler.setVisibility(View.VISIBLE);
        postRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                updateData();
            }
        });

        return view;
    }

    private void updateData()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                get_user_db.loadUserData(getActivity().getApplicationContext());
            }
        }).start();
        get_all_post_storage.getPosts(getActivity().getApplicationContext());
        swipe.setRefreshing(false);
    }

    private void chk_insta_data()
    {
//  checking if user personal data is empty or not

        SharedPreferences pref1 = getContext().getSharedPreferences("Pref_User_Data", Context.MODE_PRIVATE);
        String aboutUserJson = pref1.getString("User_data","");
        if(aboutUserJson.equals("")){
            get_user_db.loadUserData(getContext());
        }
        else{
            Gson gson = new Gson();
            HomeScreenActivity.USER_DATA = gson.fromJson(aboutUserJson, create_user_model.class);
            SharedPreferences pref3 = getContext().getSharedPreferences("Pref_User_Post", Context.MODE_PRIVATE);
            String jsonUserPost = pref3.getString("User_Post","");
            System.out.println("User posts from shared pref = " + jsonUserPost);
            Type type = new TypeToken<ArrayList<post_create>>(){}.getType();
            if(!jsonUserPost.equals("")){
                HomeScreenActivity.USER_POSTS = gson.fromJson(jsonUserPost, type);
            }
        }
// getting all posts

        SharedPreferences pref2 = getContext().getSharedPreferences("Pref_All_Post",Context.MODE_PRIVATE);
        String json_post = pref2.getString("all_post","");

        if(json_post.equals("")){
            get_all_post_storage.getPosts(getContext());
        }else{
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<post_create>>(){}.getType();
            allPost = gson.fromJson(json_post, type);
        }
//   getting reels
        get_all_reels.getReels(getContext());
    }
}