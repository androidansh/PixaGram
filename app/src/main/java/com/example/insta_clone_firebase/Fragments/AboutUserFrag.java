package com.example.insta_clone_firebase.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.ShowFollowerFollowing;
import com.example.insta_clone_firebase.activities.Show_User_PostActivity;
import com.example.insta_clone_firebase.adapter.small_post_adapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AboutUserFrag extends Fragment {

    private TextView userId,userName,userBio,numPost,numFollower,numFollowing;
    private ImageView userProfile,subMenu;
    private LinearLayout posts,followers,following;
    private RecyclerView recyclerView;
    private small_post_adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_user, container, false);

        userId = view.findViewById(R.id.about_UserID);
        userName = view.findViewById(R.id.about_userName);
        userBio = view.findViewById(R.id.about_userBio);
        userProfile = view.findViewById(R.id.about_userProfile);
        numPost = view.findViewById(R.id.numPost);
        numFollower = view.findViewById(R.id.numFollower);
        numFollowing = view.findViewById(R.id.numFollowing);

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
        loadUI();
    }

    void loadUI(){
        userId.setText( HomeScreenActivity.USER_DATA.getUser_id());
        userName.setText( HomeScreenActivity.USER_DATA.getUser_name());
        userBio.setText( HomeScreenActivity.USER_DATA.getUser_bio());
        Glide.with(getContext()).load( HomeScreenActivity.USER_DATA.getUser_profile()).into(userProfile);
        if( HomeScreenActivity.USER_POSTS== null){
            numPost.setText("0");
        }
        else{
            numPost.setText( HomeScreenActivity.USER_POSTS.size() + "");
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