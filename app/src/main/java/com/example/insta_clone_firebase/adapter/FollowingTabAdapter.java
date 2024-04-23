package com.example.insta_clone_firebase.adapter;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.insta_clone_firebase.Fragments.FollowingTab;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.activities.ShowFollowerFollowing;
import com.example.insta_clone_firebase.model.Follower_Following_model;
import com.example.insta_clone_firebase.model.create_user_model;
import com.example.insta_clone_firebase.thread_package.get_user_db;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FollowingTabAdapter extends RecyclerView.Adapter<FollowingTabAdapter.ViewHolder> {

    ArrayList<Follower_Following_model> followingList;
    Context context;

    public FollowingTabAdapter(ArrayList<Follower_Following_model> followingList, Context context) {
        this.followingList = followingList;
        this.context = context;
    }

    @NonNull
    @Override
    public FollowingTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.follower_folloowing_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingTabAdapter.ViewHolder holder, int position) {
        Follower_Following_model user = followingList.get(position);
        holder.followingID.setText(user.getFollower_following_id());

        Glide.with(context).load(user.getProfile_url()).into(holder.followingPic);

        if(ShowFollowerFollowing.isOwner){
            holder.removeFollowing.setText("Following");
            holder.removeFollowing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.removeFollowing.getText().toString().equals("Following")){
                        showDialog(user,holder);
                    }else{
                        holder.removeFollowing.setBackgroundResource(R.drawable.gray_btn_bg);
                        holder.removeFollowing.setText("Following");
                        addFollowing(user);
                    }


                }
            });
        }
        else{
            holder.removeFollowing.setVisibility(View.INVISIBLE);
        }


    }

    private void addFollowing(Follower_Following_model user) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Instagram User Private Data").document(user.getFollower_following_id());
        Follower_Following_model model1 = new Follower_Following_model();
        model1.setProfile_url(HomeScreenActivity.USER_DATA.getUser_profile());
        model1.setFollower_following_id(HomeScreenActivity.USER_DATA.getUser_id());

        ref.update("followers", FieldValue.arrayUnion(model1)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused)
            {
                DocumentReference pref = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
                pref.update("followings",FieldValue.arrayUnion(user)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Follower added to " + HomeScreenActivity.USER_DATA.getUser_id());
                        System.out.println("Following added to " + user.getFollower_following_id());
                        get_user_db.loadUserData(context);


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

    public void showDialog(Follower_Following_model user, FollowingTabAdapter.ViewHolder holder){
        Dialog chatDialog = new Dialog(context);
        chatDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.unfollow_dialog,null);
        chatDialog.setContentView(view);
        chatDialog.show();

        Button remove = view.findViewById(R.id.dialog_btn3);
        Button cancel = view.findViewById(R.id.dialog_btn4);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok = false;
                for(Follower_Following_model model : HomeScreenActivity.USER_DATA.getFollowers()){
                    if(model.getFollower_following_id().equals(user.getFollower_following_id())){
                        ok = true;
                        break;
                    }
                }
                if(ok){
                    holder.removeFollowing.setText("Follow back");
                }
                else{
                    holder.removeFollowing.setText("Follow");
                }

                holder.removeFollowing.setBackgroundResource(R.drawable.btn_blue_bg);
                removeFollowing(user);
                chatDialog.dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok = false;
                for(Follower_Following_model model : HomeScreenActivity.USER_DATA.getFollowers()){
                    if(model.getFollower_following_id().equals(user.getFollower_following_id())){
                        ok = true;
                        break;
                    }
                }
                if(ok){
                    System.out.println("follow back");
                }
                else{
                    System.out.println("follow");
                }
                chatDialog.dismiss();
            }
        });
    }

    private void removeFollowing(Follower_Following_model user) {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                create_user_model obj1 = documentSnapshot.toObject(create_user_model.class);
                ArrayList<Follower_Following_model> list = obj1.getFollowings();

                for(int i=0;i<list.size();i++){
                    if(list.get(i).getFollower_following_id().equals(user.getFollower_following_id())){
                        list.remove(i);
                        break;
                    }
                }

                ref.update("followings", list).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        DocumentReference pref = db.collection("Instagram User Private Data").document(user.getFollower_following_id());
                        pref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot)
                            {
                                create_user_model obj2 = documentSnapshot.toObject(create_user_model.class);
                                ArrayList<Follower_Following_model> list2 = obj2.getFollowers();
                                for(int i=0;i<list2.size();i++){
                                    if(list2.get(i).getFollower_following_id().equals(HomeScreenActivity.USER_DATA.getUser_id())){
                                        list2.remove(i);
                                        break;
                                    }
                                }
                                //System.out.print
                                pref.update("followers",list2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        System.out.println("Follower removed in " + user.getFollower_following_id());
                                        System.out.println("Following removed in " + HomeScreenActivity.USER_DATA.getUser_id());
                                        get_user_db.loadUserData(context);
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

    @Override
    public int getItemCount() {
        return followingList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView followingID;
        ImageView followingPic;
        Button removeFollowing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            followingID = itemView.findViewById(R.id.itemID);
            removeFollowing = itemView.findViewById(R.id.itemBTN);
            followingPic = itemView.findViewById(R.id.itemPic);
        }
    }
}
