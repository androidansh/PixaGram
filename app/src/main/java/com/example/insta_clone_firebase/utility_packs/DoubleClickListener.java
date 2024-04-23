package com.example.insta_clone_firebase.utility_packs;

import android.view.View;

public abstract class DoubleClickListener implements View.OnClickListener {
    private Long last_clicked = 0l;
    private int click_interval = 300;


    @Override
    public void onClick(View v) {
        long click_time = System.currentTimeMillis();
        if(click_time - last_clicked < click_interval){
            double_click_listener(v);
        }
        last_clicked = click_time;
    }
    public abstract void double_click_listener(View v);
}
