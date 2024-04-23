package com.example.insta_clone_firebase.adapter;

import static com.example.insta_clone_firebase.thread_package.get_all_reels.context;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.model.post_create;
import com.example.insta_clone_firebase.utility_packs.DoubleClickListener;
import com.example.insta_clone_firebase.utility_packs.ReelDoubleClickListener;

import java.util.ArrayList;

public class ReelAdapter extends RecyclerView.Adapter<ReelAdapter.ViewHolder> {

    ArrayList<post_create> reelList;
    Context context;
    AudioManager manager;

    public ReelAdapter(ArrayList<post_create> reelList, Context context) {
        this.reelList = reelList;
        this.context = context;
        manager = (AudioManager)context.getSystemService(context.AUDIO_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reel_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        post_create reel = reelList.get(position);
        holder.videoView.setVideoURI(Uri.parse(reel.getPost_url()));
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                float screenRatio = holder.videoView.getWidth() / (float) holder.videoView.getHeight();
                float scale = videoRatio/screenRatio;
                if(scale >= 1f){
                    holder.videoView.setScaleX(scale);
                }
                else{
                    holder.videoView.setScaleY(1f/scale);
                }
                mp.start();
            }
        });
        holder.reelOwner.setText(reel.getPost_owner_name());
        holder.reelDesc.setText(reel.getPost_description());
        Glide.with(context).load(reel.getPost_owner_pic()).into(holder.reelProfile);

        holder.clickLayout.setOnClickListener(new ReelDoubleClickListener() {
            @Override
            public void double_click_listener(View v) {
                holder.reel_anim.setVisibility(View.VISIBLE);
                holder.reelLike.setTag("Liked");
                holder.reelLike.setBackgroundResource(R.drawable.red_like);
                Animation anim1 = AnimationUtils.loadAnimation(context,R.anim.like_pic_zoom_in);
                holder.reelLike.startAnimation(anim1);
                holder.reel_anim.playAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.reel_anim.setVisibility(View.INVISIBLE);
                    }
                },1000);
            }

            @Override
            public void single_click_listener(View v) {
                AudioManager manager = (AudioManager)context.getSystemService(context.AUDIO_SERVICE);
                int currVol = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
                holder.muteLayout.setVisibility(View.VISIBLE);
                if(currVol != 0){
                    manager.adjustVolume(AudioManager.ADJUST_MUTE,0);
                    holder.muteBtn.setBackgroundResource(R.drawable.muted);

                } else {
                    manager.adjustVolume(AudioManager.ADJUST_UNMUTE,0);
                    holder.muteBtn.setBackgroundResource(R.drawable.unmuted);

                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            System.out.println("Error in reel mute btn ");
                        }
                        holder.muteLayout.setVisibility(View.INVISIBLE);
                    }
                }).start();
            }
        });
//        holder.videoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int currVol = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                if(currVol != 0){
//                    manager.adjustVolume(AudioManager.ADJUST_MUTE,0);
//                } else {
//                    manager.adjustVolume(AudioManager.ADJUST_UNMUTE,0);
//                }
//            }
//        });
        holder.reelLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.reelLike.getTag().equals("Liked")){
                    holder.reelLike.setBackgroundResource(R.drawable.blank_like);
                    holder.reelLike.setTag("notLiked");
                    if(reel.getLikedArray().size() > 0){
                        //holder.numLikes.setText(post.getLikedArray().size() - 1 + "");
                    }
                    //removeLikeFromPost(postList,position);
                }
                else{
                    holder.reel_anim.setVisibility(View.VISIBLE);
                    holder.reelLike.setBackgroundResource(R.drawable.red_like);
                    Animation anim1 = AnimationUtils.loadAnimation(context,R.anim.like_pic_zoom_in);
                    holder.reelLike.startAnimation(anim1);
                    holder.reelLike.setTag("Liked");
                    holder.reel_anim.playAnimation();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.reel_anim.setVisibility(View.INVISIBLE);
                        }
                    },1000);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView reelProfile,reelLike,reelComment,reelSave,muteBtn;
        TextView reelOwner,reelDesc;
        LottieAnimationView reel_anim;
        Button follow;
        VideoView videoView;
        ConstraintLayout muteLayout;
        LinearLayout clickLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reelComment = itemView.findViewById(R.id.reelComment);
            reelProfile = itemView.findViewById(R.id.reel_owner_pic);
            reelLike = itemView.findViewById(R.id.reelLike);
            reelOwner = itemView.findViewById(R.id.reel_owner);
            reelDesc = itemView.findViewById(R.id.reel_desc);
            follow = itemView.findViewById(R.id.followFollowingBtnReel);
            videoView = itemView.findViewById(R.id.videoView);
            reel_anim = itemView.findViewById(R.id.reel_like_anim);
            muteBtn = itemView.findViewById(R.id.muteBtn);
            muteLayout = itemView.findViewById(R.id.muteLayout);
            clickLayout = itemView.findViewById(R.id.clickLayout);
        }
    }
}
