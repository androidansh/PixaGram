package com.example.insta_clone_firebase.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.insta_clone_firebase.Fragments.AboutUserFrag;
import com.example.insta_clone_firebase.Fragments.SearchUserFrag;
import com.example.insta_clone_firebase.Fragments.ShowPostFrag;
import com.example.insta_clone_firebase.Fragments.ShowReelFrag;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.model.create_user_model;
import com.example.insta_clone_firebase.model.post_create;
import com.example.insta_clone_firebase.post_reel.AddPostReelActivity;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {

    public static create_user_model USER_DATA = new create_user_model();
    public static ArrayList<post_create> USER_POSTS = new ArrayList<>();
    private ImageView showPost,searchUser, addPost, reel;
    public static ImageView aboutUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_activity);

        showPost = findViewById(R.id.showAllPost);
        searchUser = findViewById(R.id.searchUser);
        addPost = findViewById(R.id.AddPost);
        reel = findViewById(R.id.reels);
        aboutUser = findViewById(R.id.aboutUser);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.homeFrame,new ShowPostFrag());
        ft.commit();

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, AddPostReelActivity.class));
            }
        });


        showPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.homeFrame,new ShowPostFrag());
                ft.commit();
            }
        });
        aboutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.homeFrame,new AboutUserFrag());
                ft.addToBackStack("");
                ft.commit();
            }
        });
        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm  = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack("");
                ft.replace(R.id.homeFrame,new SearchUserFrag());
                ft.commit();
            }
        });

        reel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.homeFrame,new ShowReelFrag());
                ft.commit();


            }
        });

        SharedPreferences preferences = getSharedPreferences("Pref_User_Data", Context.MODE_PRIVATE);
        String json_data = preferences.getString("User_data","");
        if(!(json_data.equals(""))){
            Gson gson = new Gson();
            USER_DATA = gson.fromJson(json_data,create_user_model.class);
           // System.out.println("Number of posts of user  = " + USER_DATA.getPosts().size());
            Glide.with(this).load(USER_DATA.getUser_profile()).into(aboutUser);
        }

//        SharedPreferences pref = getSharedPreferences("Pref_User_Post", Context.MODE_PRIVATE);
//        String jsonPost = pref.getString("User_Post","");
//        if(!jsonPost.equals("")){
//            Gson gson = new Gson();
//            Type type = new TypeToken<ArrayList<post_create>>(){}.getType();
//            USER_POSTS = gson.fromJson(jsonPost,type);
//        }

    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        System.out.println(fm.findFragmentById(R.id.homeFrame).getClass().getSimpleName());
        if (fm.findFragmentById(R.id.homeFrame).getClass().getSimpleName().equals("ShowPostFrag")) {
            super.onBackPressed();
            super.finish();
            //finish();
        } else {
            super.getSupportFragmentManager().popBackStack();

        }

    }


}