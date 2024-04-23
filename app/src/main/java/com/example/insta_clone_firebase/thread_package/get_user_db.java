package com.example.insta_clone_firebase.thread_package;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.model.actual_database_user_data;
import com.example.insta_clone_firebase.model.create_user_model;
import com.example.insta_clone_firebase.model.post_create;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageTask;
import com.google.gson.Gson;

import java.util.ArrayList;

public class get_user_db {
    static int totalSize = 0;
    static int incSize = 0;
    static Context con;

    public static void loadUserData(Context context){
        con = context;
        SharedPreferences pref = context.getSharedPreferences("Pref_Logged_Session",Context.MODE_PRIVATE);
        String database_id = pref.getString("database_id","");
        if(database_id.equals("")){
            System.out.println("no database id provided.");
            return;
        }
        else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Instagram User Private Data").document(database_id);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

            // getting raw data
                    actual_database_user_data rawData= documentSnapshot.toObject(actual_database_user_data.class);
                    ArrayList<post_create> post1 = new ArrayList<>();

            // converting raw data of user from db to create user data
                    if(rawData.getFollowers() == null){
                        HomeScreenActivity.USER_DATA.setFollowers(new ArrayList<>());
                    }else{
                        HomeScreenActivity.USER_DATA.setFollowers(rawData.getFollowers());
                    }
                    if(rawData.getFollowings() == null){
                        HomeScreenActivity.USER_DATA.setFollowings(new ArrayList<>());
                    }else{
                        HomeScreenActivity.USER_DATA.setFollowings(rawData.getFollowings());
                    }

                    HomeScreenActivity.USER_DATA.setUser_bio(rawData.getUser_bio());
                    HomeScreenActivity.USER_DATA.setUser_id(rawData.getUser_id());
                    HomeScreenActivity.USER_DATA.setUser_mail(rawData.getUser_mail());
                    HomeScreenActivity.USER_DATA.setUser_name(rawData.getUser_name());
                    HomeScreenActivity.USER_DATA.setUser_password(rawData.getUser_password());
                    HomeScreenActivity.USER_DATA.setUser_profile(rawData.getUser_profile());
                    HomeScreenActivity.USER_DATA.setUser_uid(rawData.getUser_uid());
                    HomeScreenActivity.USER_DATA.setBookmarks(rawData.getBookmarks());
                    HomeScreenActivity.USER_DATA.setLikedPosts(rawData.getLiked());
                    totalSize = rawData.getPosts().size();

            //  getting posts of user from db
                    if(rawData.getPosts().size() > 0){
                        new Thread(new thread1()).start();
                        if(HomeScreenActivity.USER_POSTS.size()>0){
                            HomeScreenActivity.USER_POSTS.removeAll(HomeScreenActivity.USER_POSTS);
                        }
                        for(int i = 0;i<rawData.getPosts().size();i++){
                            DocumentReference ref = db.collection("Instagram_user_post_data").document(rawData.getPosts().get(i));
                            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    post_create user_posts = documentSnapshot.toObject(post_create.class);
                                    HomeScreenActivity.USER_POSTS.add(user_posts);
                                    System.out.println("User post array size = " + HomeScreenActivity.USER_POSTS.size());
                                    incSize++;
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("error in getting posts  number :: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            });
                        }
                        System.out.println("User post array size = " + HomeScreenActivity.USER_POSTS.size());
                    }else{
                        System.out.println("Post array is empty");
                        HomeScreenActivity.USER_POSTS = post1;
                    }


//            // getting user bookmarked posts
//                    if(rawData.getBookmarks().size() > 0){
//                        System.out.println("Number of bookmarks  " + rawData.getBookmarks().size());
//
//                        ArrayList<post_create> post1 = new ArrayList<>();
//                        for(int j = 0;j<rawData.getPosts().size();j++){
//                            DocumentReference ref = db.collection("Instagram_user_post_data").document(rawData.getPosts().get(j));
//                            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                                    post_create user_posts = documentSnapshot.toObject(post_create.class);
//                                    //user_posts.setPost_uid(documentSnapshot.getId());
//                                    post1.add(user_posts);
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    System.out.println("error in getting posts  number :: " + e.getMessage());
//                                    e.printStackTrace();
//                                }
//                            });
//                        }
//
//                        HomeScreenActivity.USER_DATA.setPosts(post1);
//                    }else{
//                        System.out.println("saved Post array is empty");
//                    }
//            //  getting liked posts
//                    if(rawData.getLiked().size() > 0){
//                        System.out.println("Number of liked posts  " + rawData.getLiked().size());
//                        for(int k = 0; k<rawData.getLiked().size(); k++){
//                            DocumentReference ref = db.collection("Instagram_user_post_data").document(rawData.getLiked().get(k));
//                            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                                    post_create user_posts = documentSnapshot.toObject(post_create.class);
//                                    //user_posts.setPost_uid(documentSnapshot.getId());
//                                    HomeScreenActivity.USER_DATA.getLikedPosts().add(user_posts);
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    System.out.println("error in getting posts  number :: " + e.getMessage());
//                                    e.printStackTrace();
//                                }
//                            });
//                        }
//                    }else{
//                        System.out.println("liked Post array is empty");
//                    }

                    //System.out.println("User posts = " + HomeScreenActivity.USER_DATA.getPosts());

                    Glide.with(context).load(HomeScreenActivity.USER_DATA.getUser_profile()).into(HomeScreenActivity.aboutUser);
                    SharedPreferences preferences = context.getSharedPreferences("Pref_User_Data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = preferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(HomeScreenActivity.USER_DATA);
                    prefsEditor.putString("User_data", json);
                    prefsEditor.apply();

                    System.out.println("Data got from db for user details");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Error in getting user data " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }

    }

    static class thread1 implements Runnable{

        @Override
        public void run() {
            while(totalSize != incSize){
                continue;
            }

            SharedPreferences preferences1 = con.getSharedPreferences("Pref_User_Post", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            SharedPreferences.Editor prefsEditor1 = preferences1.edit();
            String json_post = gson.toJson(HomeScreenActivity.USER_POSTS);
            System.out.println("User posts from db in thread = " + json_post);
            prefsEditor1.putString("User_Post", json_post);
            prefsEditor1.apply();

        }
    }

}
