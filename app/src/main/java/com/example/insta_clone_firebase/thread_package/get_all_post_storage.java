package com.example.insta_clone_firebase.thread_package;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.example.insta_clone_firebase.Fragments.ShowPostFrag;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.model.post_create;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class get_all_post_storage {
    static int numPosts = 0;
    static Context context1;

    public static void getPosts(Context context){
        context1 = context;
        if(ShowPostFrag.allPost.size() > 0){
            ShowPostFrag.allPost.clear();
        }
        new Thread(new th1()).start();
    }

    static class th1 implements Runnable{

        @Override
        public void run() {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference reference = db.collection("Instagram_user_post_data");
            reference.get().addOnSuccessListener(queryArray -> {
                for(int i = 0 ; i < queryArray.getDocuments().size() ; i++){
                    post_create user_posts = queryArray.getDocuments().get(i).toObject(post_create.class);
                    ShowPostFrag.allPost.add(user_posts);
                }

                ShowPostFrag.allPost.sort(new Comparator<post_create>() {
                    @Override
                    public int compare(post_create o1, post_create o2) {
                        return o1.getDate_created().compareTo(o2.getDate_created());
                    }
                });
                Collections.reverse(ShowPostFrag.allPost);
                System.out.println("All posts got from db.");
                SharedPreferences pref = context1.getSharedPreferences("Pref_All_Post",Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = gson.toJson(ShowPostFrag.allPost);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("all_post",json);
                edit.apply();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ShowPostFrag.adapter.notifyDataSetChanged();
                        }
                    });
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("error in getting data from collection of all posts " + e.getMessage());
                    e.printStackTrace();
                }
            });
            System.out.println("Got All posts");
        }
    }

/*
    public static void getPostsFromStorage(Context context){
        context1 = context;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("instagram_data").child("posts");

        reference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                numPosts = listResult.getItems().size();
                if(ShowPostFrag.allPost.size() > 0){
                    ShowPostFrag.allPost.clear();
                }
                for (StorageReference item : listResult.getItems()) {
                    getData(item);
                }
                new Thread(new th1()).start();

            }
        });
    }
    private static void getData(StorageReference item){
        post_create post = new post_create();
            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    post.setPost_url(uri.toString());
                    item.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                        @Override
                        public void onSuccess(StorageMetadata storageMetadata)
                        {
                            post.setPost_description(storageMetadata.getCustomMetadata("post_desc"));
                            post.setPost_owner_pic(storageMetadata.getCustomMetadata("user_profile"));
                            post.setPost_owner_name(storageMetadata.getCustomMetadata("user_id"));
                            ShowPostFrag.allPost.add(post);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Error in getting pic metadata = " + e.getMessage());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Error in getting pic = " + e.getMessage());
                }
            });
        }

 */

}
