package com.example.insta_clone_firebase.thread_package;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.example.insta_clone_firebase.Fragments.ShowPostFrag;
import com.example.insta_clone_firebase.model.post_create;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;

public class get_all_reels {

    public static Context context;

    public static  void getReels(Context Context) {
        context = Context;
        if(ShowPostFrag.allReel.size() > 0){
            ShowPostFrag.allReel.removeAll(ShowPostFrag.allReel);
        }
        new Thread(new ReelThread()).start();
    }


    static class ReelThread implements Runnable {

        @Override
        public void run() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference reference = db.collection("Instagram_user_reel_data");
            reference.get().addOnSuccessListener(queryArray -> {
                for (int i = 0; i < queryArray.getDocuments().size(); i++) {
                    post_create user_reel = queryArray.getDocuments().get(i).toObject(post_create.class);
                    ShowPostFrag.allReel.add(user_reel);
                }

                ShowPostFrag.allReel.sort(new Comparator<post_create>() {
                    @Override
                    public int compare(post_create o1, post_create o2) {
                        return o1.getDate_created().compareTo(o2.getDate_created());
                    }
                });
                Collections.reverse(ShowPostFrag.allReel);
                System.out.println("Got All reels");
//                SharedPreferences pref = context.getSharedPreferences("Pref_All_Post",Context.MODE_PRIVATE);
//                Gson gson = new Gson();
//                String json = gson.toJson(ShowPostFrag.allPost);
//                SharedPreferences.Editor edit = pref.edit();
//                edit.putString("all_post",json);
//                edit.apply();
//                Handler handler = new Handler(Looper.getMainLooper());
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        ShowPostFrag.adapter.notifyDataSetChanged();
//                    }
//                });
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("error in getting data from collection of all reels " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }

}
