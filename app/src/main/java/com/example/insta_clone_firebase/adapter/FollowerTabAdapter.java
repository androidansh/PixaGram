package com.example.insta_clone_firebase.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

public class FollowerTabAdapter extends RecyclerView.Adapter<FollowerTabAdapter.ViewHolder> {
    ArrayList<Follower_Following_model> followerList;
    Context context;

    public FollowerTabAdapter(ArrayList<Follower_Following_model> followerList, Context context) {
        this.followerList = followerList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.follower_folloowing_item_layout,parent,false);
        return new FollowerTabAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Follower_Following_model user = followerList.get(position);
        holder.followerID.setText(user.getFollower_following_id());

        Glide.with(context).load(user.getProfile_url()).into(holder.followerPic);
        boolean click = true;

        if(ShowFollowerFollowing.isOwner){
            holder.removeFollower.setText("Remove");
            holder.removeFollower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(holder.removeFollower.getText().toString().equals("Remove")){
                        showDialog(user,holder);
                    }
                    else{
                        holder.removeFollower.setText("Remove");
                        holder.removeFollower.setBackgroundResource(R.drawable.gray_btn_bg);
                        addFollower(user);
                    }
                }
            });
        }
        else{
            holder.removeFollower.setVisibility(View.INVISIBLE);
        }
    }

    private void addFollower(Follower_Following_model user) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
        ref.update("followers", FieldValue.arrayUnion(user)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused)
            {
                DocumentReference pref = db.collection("Instagram User Private Data").document(user.getFollower_following_id());
                Follower_Following_model model2 = new Follower_Following_model();
                model2.setFollower_following_id(HomeScreenActivity.USER_DATA.getUser_id());
                model2.setProfile_url(HomeScreenActivity.USER_DATA.getUser_profile());

                pref.update("followings",FieldValue.arrayUnion(model2)).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void showDialog(Follower_Following_model user,ViewHolder holder){
        Dialog chatDialog = new Dialog(context);
        chatDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.remove_follower_dialog,null);
        chatDialog.setContentView(view);
        chatDialog.show();

        Button remove = view.findViewById(R.id.dialog_btn1);
        Button cancel = view.findViewById(R.id.dialog_btn2);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.removeFollower.setText("Follow");
                holder.removeFollower.setBackgroundResource(R.drawable.btn_blue_bg);
                removeFollower(user);
                chatDialog.dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatDialog.dismiss();
            }
        });
    }

    private void removeFollower(Follower_Following_model user) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Instagram User Private Data").document(user.getFollower_following_id());
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                create_user_model obj1 = documentSnapshot.toObject(create_user_model.class);
                ArrayList<Follower_Following_model> list = obj1.getFollowings();

                for(int i=0;i<list.size();i++){
                    if(list.get(i).getFollower_following_id().equals(HomeScreenActivity.USER_DATA.getUser_id())){
                        list.remove(i);
                        break;
                    }
                }

                ref.update("followings", list).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        DocumentReference pref = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
                        pref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot)
                            {
                                create_user_model obj2 = documentSnapshot.toObject(create_user_model.class);
                                ArrayList<Follower_Following_model> list2 = obj2.getFollowers();
                                for(int i=0;i<list2.size();i++){
                                    if(list2.get(i).getFollower_following_id().equals(user.getFollower_following_id())){
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
        return followerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView followerID;
        ImageView followerPic;
        Button removeFollower;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            followerID = itemView.findViewById(R.id.itemID);
            removeFollower = itemView.findViewById(R.id.itemBTN);
            followerPic = itemView.findViewById(R.id.itemPic);
        }
    }
}
