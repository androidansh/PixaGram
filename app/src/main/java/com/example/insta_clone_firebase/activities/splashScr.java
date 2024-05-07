package com.example.insta_clone_firebase.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.thread_package.get_all_post_storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splashScr extends AppCompatActivity {

    private LottieAnimationView lottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        EdgeToEdge ac = new EdgeToEdge();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_scr);

//        getWindow().setLayout(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Window windowFrame = getWindow();
        windowFrame.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        windowFrame.addFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        windowFrame.setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));

        lottie = findViewById(R.id.splashScr);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref =getSharedPreferences("Pref_Logged_Session", Context.MODE_PRIVATE);
                boolean isLogged = pref.getBoolean("isLogged",false);
                if(isLogged){
                    get_all_post_storage.getPosts(getApplicationContext());
                    startActivity(new Intent(splashScr.this, HomeScreenActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(splashScr.this, LoginActivity.class));
                    finish();
                }
            }
        },3000);



    }
}