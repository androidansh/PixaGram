package com.example.insta_clone_firebase.utility_packs;

import static com.example.insta_clone_firebase.thread_package.get_all_reels.context;

import android.media.AudioManager;
import android.view.View;

import com.example.insta_clone_firebase.adapter.ReelAdapter;

public abstract class ReelDoubleClickListener implements View.OnClickListener {
    private Long last_clicked = 0L;
    private int click_interval = 300;



    @Override
    public void onClick(View v) {
        long click_time = System.currentTimeMillis();
        if(click_time - last_clicked < click_interval){
            double_click_listener(v);
        }
        else{
            single_click_listener(v);
        }
        last_clicked = click_time;
    }
    public abstract void double_click_listener(View v);

    public abstract void single_click_listener(View v);
}
