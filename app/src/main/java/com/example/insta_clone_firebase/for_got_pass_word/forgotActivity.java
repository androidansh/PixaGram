package com.example.insta_clone_firebase.for_got_pass_word;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.insta_clone_firebase.R;

public class forgotActivity extends AppCompatActivity {

    private FrameLayout frame;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        context = this;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.for_got_frame,new CurrPassword());
        ft.commit();

    }

}