package com.example.insta_clone_firebase.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.adapter.small_post_adapter;
import com.example.insta_clone_firebase.model.Follower_Following_model;
import com.example.insta_clone_firebase.model.actual_database_user_data;
import com.example.insta_clone_firebase.model.create_user_model;
import com.example.insta_clone_firebase.model.post_create;
import com.example.insta_clone_firebase.thread_package.get_user_db;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Search_User_Activity extends AppCompatActivity
{
    private ImageView back,searchProfile;
    private TextView searchUserId,searchUserName,searchUserBio,searchNumPost,searchNumFollower,searchNumFollowing;
    private Button actionBtn;
    String searchID;
    public Window windowFrame;
    public static create_user_model foundUserObj;
    private small_post_adapter adapter;
    private RecyclerView recyclerView;
    private ConstraintLayout noPostMsg;
    private LinearLayout searchPost,searchFollower,searchFollowing;
    private Boolean isFollowing = false;
    private Boolean isFollower = false;
    private Boolean isOwner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        windowFrame = getWindow();
        windowFrame.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        windowFrame.setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));

        back = findViewById(R.id.searchBack);
        searchProfile = findViewById(R.id.search_userProfile);
        searchUserId = findViewById(R.id.search_UserID);
        searchUserBio = findViewById(R.id.search_userBio);
        searchNumPost = findViewById(R.id.numPost);
        searchNumFollower = findViewById(R.id.numFollower);
        searchNumFollowing = findViewById(R.id.numFollowing);
        actionBtn = findViewById(R.id.actionBTN);
        noPostMsg = findViewById(R.id.no_post_found_msg1);
        recyclerView = findViewById(R.id.searchPostRecycler);

        searchPost = findViewById(R.id.searchUserPost);
        searchFollower = findViewById(R.id.searchUserFollower);
        searchFollowing = findViewById(R.id.searchUserFollowing);

        Intent i = getIntent();
        searchID = i.getExtras().getString("userID");

        if(searchID.equals(HomeScreenActivity.USER_DATA.getUser_id())){
            actionBtn.setVisibility(View.INVISIBLE);
            isOwner = true;
        }
        load_search_data();

        back.setOnClickListener(v->{
            finish();
        });


        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFollowing){
                    isFollowing = false;
                    if(isFollower){
                        actionBtn.setText("Follow back");
                    }
                    else{
                        actionBtn.setText("Follow");
                    }
                    removeFollowing();
                }
                else{
                    isFollowing = true;
                    actionBtn.setText("Following");
                    addFollower();
                }
            }
        });

        searchPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Search_User_Activity.this, Show_User_PostActivity.class);
                i.putExtra("code","2");
                startActivity(i);
            }
        });

        searchFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Search_User_Activity.this, ShowFollowerFollowing.class);
                i.putExtra("pageNum","0");
                i.putExtra("isOwner",isOwner);
                i.putExtra("ownerData","searchFrag");
                startActivity(i);
            }
        });
        searchFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Search_User_Activity.this, ShowFollowerFollowing.class);
                i.putExtra("pageNum","1");
                i.putExtra("isOwner",isOwner);
                i.putExtra("ownerData","searchFrag");
                startActivity(i);
            }
        });

    }

    private void addFollower() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Instagram User Private Data").document(searchID);

        Follower_Following_model model1 = new Follower_Following_model();
        model1.setProfile_url(HomeScreenActivity.USER_DATA.getUser_profile());
        model1.setFollower_following_id(HomeScreenActivity.USER_DATA.getUser_id());

        ref.update("followers", FieldValue.arrayUnion(model1)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused)
            {
                DocumentReference pref = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
                Follower_Following_model model2 = new Follower_Following_model();
                model2.setFollower_following_id(searchID);
                model2.setProfile_url(foundUserObj.getUser_profile());

                pref.update("followings",FieldValue.arrayUnion(model2)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Follower added to " + searchID);
                        System.out.println("Following added to " + HomeScreenActivity.USER_DATA.getUser_id());
                        get_user_db.loadUserData(getApplicationContext());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void removeFollowing() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Instagram User Private Data").document(searchID);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                create_user_model obj1 = documentSnapshot.toObject(create_user_model.class);
                ArrayList<Follower_Following_model> list = obj1.getFollowers();

                for(int i=0;i<list.size();i++){
                    if(list.get(i).getFollower_following_id().equals(HomeScreenActivity.USER_DATA.getUser_id())){
                        list.remove(i);
                        break;
                    }
                }

                ref.update("followers", list).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        DocumentReference pref = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
                        pref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot)
                            {
                                create_user_model obj2 = documentSnapshot.toObject(create_user_model.class);
                                ArrayList<Follower_Following_model> list2 = obj2.getFollowings();
                                for(int i=0;i<list2.size();i++){
                                    if(list2.get(i).getFollower_following_id().equals(searchID)){
                                        list2.remove(i);
                                        break;
                                    }
                                }
                                //System.out.print
                                pref.update("followings",list2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        System.out.println("Follower removed in " + searchID);
                                        System.out.println("Following removed in " + HomeScreenActivity.USER_DATA.getUser_id());
                                        get_user_db.loadUserData(getApplicationContext());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        System.out.println(e.getMessage());
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void load_search_data() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Instagram User Private Data").document(searchID);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                actual_database_user_data localUser = documentSnapshot.toObject(actual_database_user_data.class);
                foundUserObj = new create_user_model();
                if(localUser != null){

                    foundUserObj.setUser_name(localUser.getUser_name());
                    foundUserObj.setUser_id(localUser.getUser_id());
                    foundUserObj.setUser_uid(localUser.getUser_uid());
                    foundUserObj.setUser_password(localUser.getUser_password());
                    foundUserObj.setUser_bio(localUser.getUser_bio());
                    foundUserObj.setUser_phone(localUser.getUser_phone());
                    foundUserObj.setUser_profile(localUser.getUser_profile());
                    foundUserObj.setUser_mail(localUser.getUser_mail());
                    foundUserObj.setFollowers(localUser.getFollowers());
                    foundUserObj.setFollowings(localUser.getFollowings());

                    if(localUser.getPosts().size() != 0){
                        // get search user posts
                        for(int i = 0;i<localUser.getPosts().size();i++){
                            CollectionReference collRef = FirebaseFirestore.getInstance().collection("Instagram_user_post_data");
                            collRef.document(localUser.getPosts().get(i)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    post_create post = documentSnapshot.toObject(post_create.class);
                                    if(post!= null){
                                        foundUserObj.getPosts().add(post);
                                    }

                                }
                            }).addOnFailureListener(e -> {
                                System.out.println("Error in getting search user post -> " + e.getMessage());
                                e.printStackTrace();
                            });
                        }

                    }
                    if(localUser.getReels().size() != 0){
                        // get search user reels
                    }

                }

                System.out.println("Data received for searched user");
                setUI();
                if(foundUserObj == null){
                    System.out.println("object is null");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error in getting searched user data = " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    void setUI(){
        if(!(HomeScreenActivity.USER_DATA.getFollowers() == null || HomeScreenActivity.USER_DATA.getFollowers().size() == 0)){
            for(Follower_Following_model evt : HomeScreenActivity.USER_DATA.getFollowers()){
                if(evt.getFollower_following_id().equals(searchID)){
                    actionBtn.setText("Follow back");
                    isFollower = true;
                }
            }
        }

        if(!(HomeScreenActivity.USER_DATA.getFollowings() == null || HomeScreenActivity.USER_DATA.getFollowings().size() == 0)){
            for(Follower_Following_model evt : HomeScreenActivity.USER_DATA.getFollowings()){
                if(evt.getFollower_following_id().equals(searchID)){
                    actionBtn.setText("Following");
                    isFollowing = true;
                }
            }
        }

        actionBtn.setBackgroundResource(R.drawable.btn_blue_bg);

        searchUserId.setText(foundUserObj.getUser_id());
//        searchUserName.setText(foundUserObj.getUser_name());
        searchUserBio.setText(foundUserObj.getUser_bio());
        Glide.with(this).load( foundUserObj.getUser_profile()).into(searchProfile);
        if( foundUserObj.getPosts() == null){
            searchNumPost.setText("0");
            noPostMsg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        else{
            if(foundUserObj.getPosts().size() == 0){
                searchNumPost.setText("0");
                noPostMsg.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }else{
                searchNumPost.setText( foundUserObj.getPosts().size() + "");
                noPostMsg.setVisibility(View.INVISIBLE);
                noPostMsg.setVisibility(View.VISIBLE);
            }

        }
        if( foundUserObj.getFollowers() == null){
            System.out.println("Search id follower is null");
            searchNumFollower.setText("0");
        }
        else{
            System.out.println("Search id follower is "+ foundUserObj.getFollowers().size());
            searchNumFollower.setText( foundUserObj.getFollowers().size() + "");
        }
        if( foundUserObj.getFollowings() == null){
            //System.out.println("Search id following is null");
            searchNumFollowing.setText("0");
        }
        else{
            //System.out.println("Search id following is "+ foundUserObj.getFollowings().size());
            searchNumFollowing.setText( foundUserObj.getFollowings().size() + "");
        }

    // to load posts data using uid stored as posts
        
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Instagram_user_post_data").document();
        if(foundUserObj.getPosts() == null){
            adapter = new small_post_adapter(new ArrayList<>(),this,this);
        }
        else{
            adapter = new small_post_adapter(foundUserObj.getPosts(),this,this);
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));


    }
}