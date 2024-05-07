package com.example.insta_clone_firebase.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.ShowFollowerFollowing;
import com.example.insta_clone_firebase.activities.Show_User_PostActivity;
import com.example.insta_clone_firebase.adapter.small_post_adapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AboutUserFrag extends Fragment {

    private TextView userId,numPost,numFollower,numFollowing;
//    private TextView userName,userBio;
    private ImageView userProfile,subMenu;
    private ConstraintLayout noPostMsg;
    private LinearLayout posts,followers,following;
    private RecyclerView recyclerView;
    private small_post_adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_user, container, false);

        HomeScreenActivity.windowFrame.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        HomeScreenActivity.windowFrame.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.transparent));

        HomeScreenActivity.showPost.setBackgroundResource(R.drawable.home);
        HomeScreenActivity.searchUser.setBackgroundResource(R.drawable.find);
        HomeScreenActivity.activeAccount.setVisibility(View.VISIBLE);
        HomeScreenActivity.addPost.setBackgroundResource(R.drawable.add);
        HomeScreenActivity.reel.setBackgroundResource(R.drawable.reels);

        userId = view.findViewById(R.id.about_UserID);
//        userName = view.findViewById(R.id.about_userName);
//        userBio = view.findViewById(R.id.about_userBio);
        userProfile = view.findViewById(R.id.about_userProfile);
        numPost = view.findViewById(R.id.numPost);
        numFollower = view.findViewById(R.id.numFollower);
        numFollowing = view.findViewById(R.id.numFollowing);
        noPostMsg = view.findViewById(R.id.no_post_found_msg);

        posts = view.findViewById(R.id.aboutUserPost);
        followers = view.findViewById(R.id.aboutUserFollower);
        following = view.findViewById(R.id.aboutUserFollowing);

        subMenu = view.findViewById(R.id.subMenu);

        recyclerView = view.findViewById(R.id.smallPostRecycler);

        posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Show_User_PostActivity.class);
                i.putExtra("code","1");
                startActivity(i);
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ShowFollowerFollowing.class);
                i.putExtra("pageNum","0");
                i.putExtra("isOwner",true);
                i.putExtra("ownerData","aboutFrag");
                getContext().startActivity(i);
            }
        });
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ShowFollowerFollowing.class);
                i.putExtra("pageNum","1");
                i.putExtra("isOwner",true);
                i.putExtra("ownerData","aboutFrag");
                getContext().startActivity(i);
            }
        });

        subMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting_pop_frag frag = new setting_pop_frag();
                frag.show(getParentFragmentManager(),frag.getTag());

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeScreenActivity.showPost.setBackgroundResource(R.drawable.home);
        HomeScreenActivity.searchUser.setBackgroundResource(R.drawable.find);
        HomeScreenActivity.activeAccount.setVisibility(View.VISIBLE);
        HomeScreenActivity.addPost.setBackgroundResource(R.drawable.add);
        HomeScreenActivity.reel.setBackgroundResource(R.drawable.reels);
        loadUI();
    }

    void loadUI(){
        userId.setText( HomeScreenActivity.USER_DATA.getUser_id());
//        userName.setText( HomeScreenActivity.USER_DATA.getUser_name());
//        userBio.setText( HomeScreenActivity.USER_DATA.getUser_bio());
        Glide.with(getContext()).load( HomeScreenActivity.USER_DATA.getUser_profile()).into(userProfile);
        if( HomeScreenActivity.USER_POSTS== null){
            numPost.setText("0");
            noPostMsg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        else{
            if(HomeScreenActivity.USER_POSTS.size() == 0){
                numPost.setText("0");
                noPostMsg.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }
            else{
                numPost.setText( HomeScreenActivity.USER_POSTS.size() + "");
                noPostMsg.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);

            }

        }
        if( HomeScreenActivity.USER_DATA.getFollowers() == null){
            numFollower.setText("0");

        }
        else{
            numFollower.setText( HomeScreenActivity.USER_DATA.getFollowers().size() + "");

        }
        if( HomeScreenActivity.USER_DATA.getFollowings() == null){
            numFollowing.setText("0");
        }
        else{
            numFollowing.setText( HomeScreenActivity.USER_DATA.getFollowings().size() + "");
        }

        adapter = new small_post_adapter(HomeScreenActivity.USER_POSTS,getContext(),getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
    }

}