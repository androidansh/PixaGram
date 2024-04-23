package com.example.insta_clone_firebase.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.insta_clone_firebase.Fragments.Show_BookmarkFrag;
import com.example.insta_clone_firebase.Fragments.user_bookmark;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.model.post_create;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class bookmark_post_activity extends AppCompatActivity {

    public static int BOOKMARK_POST_NUM = 0;
    public static ArrayList<post_create> savedPost = new ArrayList<>();

    static FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_post);


        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.bookMarkFrame,new user_bookmark());
        ft.commit();
    }

    public static void show_big_bookmark(int position){
        BOOKMARK_POST_NUM = position;
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.bookMarkFrame,new Show_BookmarkFrag());
        ft.addToBackStack("");
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.bookMarkFrame).getClass().getSimpleName().equals("user_bookmark"))  {
            super.onBackPressed();
            super.finish();
        } else {
            super.getSupportFragmentManager().popBackStack();
        }
    }
}