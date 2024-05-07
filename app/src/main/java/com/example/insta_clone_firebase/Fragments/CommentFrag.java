package com.example.insta_clone_firebase.Fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.adapter.CommentAdapter;
import com.example.insta_clone_firebase.adapter.allPostAdapter;
import com.example.insta_clone_firebase.model.post_create;
import com.example.insta_clone_firebase.model.user_post_comment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class CommentFrag extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private ImageView profile,sendBtn;
    private EditText comment;
    private int postPosition;
    int original_margin = 0;
    private ScrollView s12;
    private ConstraintLayout cl;
    private CommentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        //requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        postPosition = getArguments().getInt("postPosition");

        recyclerView = view.findViewById(R.id.commentRecycler);
        profile = view.findViewById(R.id.commentOwnerPost);
        comment = view.findViewById(R.id.commentPost);
        sendBtn = view.findViewById(R.id.comment_send);

        s12 = view.findViewById(R.id.s12);

        adapter = new CommentAdapter(allPostAdapter.postList.get(postPosition).getPostComments(),getContext());
        recyclerView.setAdapter(adapter);
        cl = view.findViewById(R.id.comment_constraint_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Glide.with(getContext()).load(HomeScreenActivity.USER_DATA.getUser_profile()).into(profile);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment.getText().toString().length()>0){
                    postComment(comment.getText().toString(),postPosition);
                }
            }
        });

//        comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    Toast.makeText(getContext(), "Focused", Toast.LENGTH_SHORT).show();
////                    s12.smoothScrollTo(0, s12.getBottom());
//                    moveEditTextUp();
//                }else{
//                    Toast.makeText(getContext(), "Lost Focused", Toast.LENGTH_SHORT).show();
//                    s12.smoothScrollTo(0, s12.getTop());
//                }
//            }
//        });


        return view;


    }

    private void postComment(String comment, int postPosition) {
        post_create post = allPostAdapter.postList.get(postPosition);

        user_post_comment postComment = new user_post_comment();
        postComment.setUser_comment(comment);
        postComment.setUserID(HomeScreenActivity.USER_DATA.getUser_id());
        postComment.setUserIDPic(HomeScreenActivity.USER_DATA.getUser_profile());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Instagram_user_post_data").document(post.getPost_uid());
        ref.update("postComments", FieldValue.arrayUnion(postComment)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Comment added");
                SharedPreferences pref = getContext().getSharedPreferences("Pref_All_Post", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = gson.toJson(allPostAdapter.postList);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("all_post",json);
                edit.apply();
                onDestroyView();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error in add post comment to db");
                e.printStackTrace();
            }
        });

    }

    private void moveEditTextUp() {
        // Calculate the height by which EditText needs to be moved up
        original_margin = 300;

        // Translate the EditText up using animations
        ObjectAnimator animator = ObjectAnimator.ofFloat(cl, "translationY", -original_margin);
        animator.setDuration(300);
        animator.start();

        // Adjust bottom margin of the EditText to prevent layout issues
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) cl.getLayoutParams();
        params.bottomMargin = 20 + original_margin;
        cl.setLayoutParams(params);

       // isEditTextMoved = true;
    }

    private void moveEditTextDown() {
        // Reset EditText position to its original state
        ObjectAnimator animator = ObjectAnimator.ofFloat(cl, "translationY", 0);
        animator.setDuration(300);
        animator.start();

        // Restore original bottom margin of the EditText
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) cl.getLayoutParams();
        params.bottomMargin -= original_margin;
        cl.setLayoutParams(params);

    }


}