package com.example.insta_clone_firebase.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.activities.bookmark_post_activity;
import com.example.insta_clone_firebase.adapter.bookmark_small_grid_Adapter;
import com.example.insta_clone_firebase.adapter.small_post_adapter;
import com.example.insta_clone_firebase.model.post_create;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class user_bookmark extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressBar progress;
    private RecyclerView recyclerView;
    private bookmark_small_grid_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_bookmark, container, false);

        progress= view.findViewById(R.id.savedProgress);
        recyclerView = view.findViewById(R.id.bookmark_recycler);
        recyclerView.setVisibility(View.INVISIBLE);
        adapter = new bookmark_small_grid_Adapter(bookmark_post_activity.savedPost, getContext());
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        progress.setVisibility(View.VISIBLE);
        if(bookmark_post_activity.savedPost.size() == 0) {
            for (int i = 0; i < HomeScreenActivity.USER_DATA.getBookmarks().size(); i++) {
                DocumentReference ref = db.collection("Instagram_user_post_data").document(HomeScreenActivity.USER_DATA.getBookmarks().get(i));
                ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        post_create saved = documentSnapshot.toObject(post_create.class);
                        bookmark_post_activity.savedPost.add(saved);
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }else{
            adapter.notifyDataSetChanged();
        }
        progress.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        return view;
    }
}