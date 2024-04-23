package com.example.insta_clone_firebase.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.adapter.allPostAdapter;
import com.example.insta_clone_firebase.adapter.userPostAdapter;
import com.example.insta_clone_firebase.model.post_create;

import java.util.ArrayList;

public class Show_User_PostActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    userPostAdapter adapter;
    ImageView back;
    ArrayList<post_create> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_post);

        Intent i = getIntent();
        String idCode = i.getStringExtra("code");

        recyclerView = findViewById(R.id.post_recycler);
        if(idCode.equals("1")){
            if(HomeScreenActivity.USER_POSTS == null){
                postList = new ArrayList<>();
            }
            else{
                postList = HomeScreenActivity.USER_POSTS;
            }
        }
        else{
            if(Search_User_Activity.foundUserObj.getPosts() == null){
                postList = new ArrayList<>();
            }
            else{
                postList = Search_User_Activity.foundUserObj.getPosts();
            }
        }

        adapter = new userPostAdapter(postList,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        back = findViewById(R.id.backUserPost);
        back.setOnClickListener(v -> finish());

    }
}