package com.example.insta_clone_firebase.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.thread_package.get_all_post_storage;

public class splashScr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_scr);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref =getSharedPreferences("Pref_Logged_Session", Context.MODE_PRIVATE);
                boolean isLogged = pref.getBoolean("isLogged",false);
                if(isLogged){
//                    get_all_post_storage.getPosts(getApplicationContext());
                    startActivity(new Intent(splashScr.this, HomeScreenActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(splashScr.this, LoginActivity.class));
                    finish();
                }
            }
        },1000);




    }
}