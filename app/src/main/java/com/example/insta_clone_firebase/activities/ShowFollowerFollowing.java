package com.example.insta_clone_firebase.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.adapter.tabbedAdapter;
import com.example.insta_clone_firebase.model.create_user_model;
import com.example.insta_clone_firebase.thread_package.get_user_db;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ShowFollowerFollowing extends AppCompatActivity {

    ViewPager viewPager;
    public static Boolean isOwner;
    TabItem followerTab,followingTab;
    public static create_user_model userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_follower_following);

        Intent i = getIntent();

        String pageNum = i.getStringExtra("pageNum");
        isOwner = i.getBooleanExtra("isOwner",false);
        String chk = i.getStringExtra("ownerData");
        if(chk != null){
            if(chk.equals("aboutFrag")){
                userModel = HomeScreenActivity.USER_DATA;
            }
            else{
                userModel = Search_User_Activity.foundUserObj;
            }
        }


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        followerTab = findViewById(R.id.followerTab);
        followingTab = findViewById(R.id.followingTab);
        viewPager = findViewById(R.id.viewPager);

        tabbedAdapter adapter  = new tabbedAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        if(pageNum.equals("0")){
            viewPager.setCurrentItem(0);
        }
        else{
            viewPager.setCurrentItem(1);
        }

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}