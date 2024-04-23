package com.example.insta_clone_firebase.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.insta_clone_firebase.Fragments.CommentFrag;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.model.post_create;
import com.example.insta_clone_firebase.model.user_post_liked;
import com.example.insta_clone_firebase.thread_package.get_user_db;
import com.example.insta_clone_firebase.utility_packs.BlurUtils;
import com.example.insta_clone_firebase.utility_packs.DoubleClickListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;

public class allPostAdapter extends RecyclerView.Adapter<allPostAdapter.ViewHolder> {
    public static ArrayList<post_create> postList;
    Context context;
    Activity activity;
    public allPostAdapter(ArrayList<post_create> postList, Context context) {
        allPostAdapter.postList = postList;
        this.context = context;
    }

    public allPostAdapter(ArrayList<post_create> postList, Context context, Activity activity) {
        allPostAdapter.postList = postList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_post_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int pos = position;
        post_create post = postList.get(position);

//        setting post owner profile and name
        Glide.with(context).load(post.getPost_owner_pic()).into(holder.postOwnerImg);
        holder.postOwnerName.setText(post.getPost_owner_name());

//        setting post image
        Glide.with(context)
                .asBitmap()
                .load(post.getPost_url())
                .apply(new RequestOptions().override(Target.SIZE_ORIGINAL)) // Specify size to get original image size
                .into(new BitmapImageViewTarget(holder.post1) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        super.onResourceReady(bitmap, transition);
                        BitmapDrawable drawable = (BitmapDrawable) holder.post1.getDrawable();
                        Bitmap bitmapFromImageView = drawable.getBitmap();

                        Bitmap blurredBitmap = BlurUtils.blur(context, bitmapFromImageView, 25f);

                        holder.post1.setImageBitmap(blurredBitmap);

                    }
                });

        Glide.with(context).load(post.getPost_url()).into(holder.post2);

        holder.options.setOnClickListener(v->{
            PopupMenu menu = new PopupMenu(context,v);
            menu.inflate(R.menu.save_menu);
            menu.show();
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                    //savePost2BookMark(post);
                    return true;
                }
            });
        });

        holder.post2.setOnClickListener(new DoubleClickListener()
        {
            @Override
            public void double_click_listener(View v) {

            // animation code
                holder.pop_like.setVisibility(View.VISIBLE);
                holder.likeBtn.setTag("Liked");
                holder.likeBtn.setBackgroundResource(R.drawable.red_like);
                Animation anim1 = AnimationUtils.loadAnimation(context,R.anim.like_pic_zoom_in);
                holder.likeBtn.startAnimation(anim1);
                holder.pop_like.playAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.pop_like.setVisibility(View.INVISIBLE);
                    }
                },1000);

            // liked post db code
                if(!check_for_user_in_liked_array(post)){
                    if(post.getLikedArray() == null || post.getLikedArray().size() == 0){
                        holder.numLikes.setText("1");
                    }
                    else{
                        holder.numLikes.setText(post.getLikedArray().size()+ 1+"");
                    }
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            like_posts(postList,pos);
                        }
                    });
                }
            }
        });

//  post likes number
        if(post.getLikedArray() == null || post.getLikedArray().size() == 0){
            holder.numLikes.setText("0");
        }
        else{
            holder.numLikes.setText(post.getLikedArray().size() + "");
        }

// like button code

        if(check_for_user_in_liked_array(post)){
            holder.likeBtn.setBackgroundResource(R.drawable.red_like);
            holder.likeBtn.setTag("Liked");
        }
        holder.likeBtn.setOnClickListener(v->{
            if(holder.likeBtn.getTag().equals("Liked")){
                holder.likeBtn.setBackgroundResource(R.drawable.blank_like);
                holder.likeBtn.setTag("notLiked");
                if(post.getLikedArray().size() > 0){
                    holder.numLikes.setText(post.getLikedArray().size() - 1 + "");
                }
                removeLikeFromPost(postList,position);
            }
            else{
                holder.pop_like.setVisibility(View.VISIBLE);
                holder.likeBtn.setBackgroundResource(R.drawable.red_like);
                Animation anim1 = AnimationUtils.loadAnimation(context,R.anim.like_pic_zoom_in);
                holder.likeBtn.startAnimation(anim1);
                holder.likeBtn.setTag("Liked");
                holder.pop_like.playAnimation();
               // System.out.println(holder.pop_like.getDuration());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.pop_like.setVisibility(View.INVISIBLE);
                    }
                },1000);
            }
        });

//  post description
        holder.post_desc_owner_name.setText("__" + post.getPost_owner_name() + "__");
        holder.postDesc.setText(post.getPost_description());

//  post bookmark

        if(post.getBookmarkUsers().contains(HomeScreenActivity.USER_DATA.getUser_id())){
            holder.save2Bookmark.setBackgroundResource(R.drawable.saved);
        }

        holder.save2Bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(post.getBookmarkUsers().contains(HomeScreenActivity.USER_DATA.getUser_id())){
                    holder.save2Bookmark.setBackgroundResource(R.drawable.saved_not);
                    removePostFromBookmark(postList,position);
                }
                else{
                    holder.save2Bookmark.setBackgroundResource(R.drawable.saved);
                    savePost2BookMark(postList,position);
                }
            }
        });
// post comments

        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("postPosition",position);
                CommentFrag frag = new CommentFrag();
                frag.setArguments(bundle);
                frag.show(((AppCompatActivity)context).getSupportFragmentManager(), frag.getTag());
            }
        });


        String com = "View all  " + post.getPostComments().size() + " comments";
        holder.comments.setText(com);


    }

    private boolean check_for_bookmark(post_create post) {

        if(post.getBookmarkUsers() == null || post.getLikedArray().size() == 0){
            return false;
        }
        String owner_name = HomeScreenActivity.USER_DATA.getUser_id();
        for(int i=0;i<post.getBookmarkUsers().size();i++){
            if(post.getBookmarkUsers().get(i).equals(HomeScreenActivity.USER_DATA.getUser_id())){
                return true;
            }
        }
        return false;
    }

    private Boolean check_for_user_in_liked_array(post_create post){

        if(post.getLikedArray() == null || post.getLikedArray().size() == 0){
            return false;
        }
        String owner_name = HomeScreenActivity.USER_DATA.getUser_id();
        for(int i=0;i<post.getLikedArray().size();i++){
            if(post.getLikedArray().get(i).getLiked_user_name().equals(owner_name)){
                return true;
            }
        }
        return false;
    }

    private void like_posts(ArrayList<post_create> post_array,int  position){

        post_create postCreate = post_array.get(position);

        user_post_liked userLiked = new user_post_liked();
        userLiked.setLiked_user_name(HomeScreenActivity.USER_DATA.getUser_id());
        userLiked.setLiked_user_profile(HomeScreenActivity.USER_DATA.getUser_profile());
        post_array.get(position).getLikedArray().add(userLiked);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Instagram_user_post_data").document(post_array.get(position).getPost_uid());
        ref.update("likedArray",FieldValue.arrayUnion(userLiked)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                DocumentReference reference = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
                reference.update("liked",FieldValue.arrayUnion(postCreate.getPost_uid())).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Post liked and added in db");
                        SharedPreferences pref = context.getSharedPreferences("Pref_All_Post",Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = gson.toJson(post_array);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("all_post",json);
                        edit.apply();
                        get_user_db.loadUserData(context);
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
                System.out.println("Error in setting post liked :: " + e.getMessage());
                e.printStackTrace();
            }
        });

    }

    private void removeLikeFromPost(ArrayList<post_create> post_array, int pos) {

        post_create post = post_array.get(pos);
        for(int i=0;i<post.getLikedArray().size();i++){
            if(post.getLikedArray().get(i).getLiked_user_name().equals(HomeScreenActivity.USER_DATA.getUser_id())){
                post_array.get(pos).getLikedArray().remove(i);
                break;
            }
        }
        user_post_liked userLiked = new user_post_liked();
        userLiked.setLiked_user_name(HomeScreenActivity.USER_DATA.getUser_id());
        userLiked.setLiked_user_profile(HomeScreenActivity.USER_DATA.getUser_profile());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Instagram_user_post_data").document(post_array.get(pos).getPost_uid());
        ref.update("likedArray",FieldValue.arrayRemove(userLiked)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                DocumentReference reference = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
                reference.update("liked",FieldValue.arrayRemove(post.getPost_uid())).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Post liked and added in db");
                        SharedPreferences pref = context.getSharedPreferences("Pref_All_Post",Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = gson.toJson(post_array);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("all_post",json);
                        edit.apply();
                        get_user_db.loadUserData(context);
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
                System.out.println("Error in setting post liked :: " + e.getMessage());
                e.printStackTrace();
            }
        });

    }

    private void savePost2BookMark(ArrayList<post_create> post_array,int  position) {

        post_create post = post_array.get(position);
        post_array.get(position).getBookmarkUsers().add(HomeScreenActivity.USER_DATA.getUser_id());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

// saving post to user db
        DocumentReference ref = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
        ref.update("bookmarks", FieldValue.arrayUnion(post.getPost_uid())).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Post saved to bookmark");
    // saving user name to post bookmark array
                DocumentReference ref = db.collection("Instagram_user_post_data").document(post_array.get(position).getPost_uid());
                ref.update("bookmarkUsers",FieldValue.arrayUnion(HomeScreenActivity.USER_DATA.getUser_id())).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Bookmark added in db in user and post");
                        SharedPreferences pref = context.getSharedPreferences("Pref_All_Post",Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = gson.toJson(post_array);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("all_post",json);
                        edit.apply();
                        get_user_db.loadUserData(context);
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
                System.out.println("Error in saving post to book mark" + e.getMessage());
                e.printStackTrace();
            }
        });

    }

    private void removePostFromBookmark(ArrayList<post_create> post_array,int  position){
        post_create post = post_array.get(position);
        post_array.get(position).getBookmarkUsers().remove(HomeScreenActivity.USER_DATA.getUser_id());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

// saving post to user db
        DocumentReference ref = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
        ref.update("bookmarks", FieldValue.arrayRemove(post.getPost_uid())).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Post saved to bookmark");
                // saving user name to post bookmark array
                DocumentReference ref = db.collection("Instagram_user_post_data").document(post_array.get(position).getPost_uid());
                ref.update("bookmarkUsers",FieldValue.arrayRemove(HomeScreenActivity.USER_DATA.getUser_id())).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Bookmark removed in db of user and post");
                        SharedPreferences pref = context.getSharedPreferences("Pref_All_Post",Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = gson.toJson(post_array);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("all_post",json);
                        edit.apply();
                        get_user_db.loadUserData(context);
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
                System.out.println("Error in saving post to book mark" + e.getMessage());
                e.printStackTrace();
            }
        });
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView postOwnerImg,post1,post2,options,likeBtn,save2Bookmark,commentBtn,shareBtn;
        LottieAnimationView pop_like;
        TextView postOwnerName,postDesc,post_desc_owner_name,comments,numLikes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postOwnerImg = itemView.findViewById(R.id.postOwnerImage);
            post1 = itemView.findViewById(R.id.postBg1);
            post2 = itemView.findViewById(R.id.post);
            options = itemView.findViewById(R.id.postOptions);
            postOwnerImg = itemView.findViewById(R.id.postOwnerImage);
            postOwnerName = itemView.findViewById(R.id.postOwner);
            postDesc = itemView.findViewById(R.id.post_owner_name_desc);
            pop_like = itemView.findViewById(R.id.pop_like);
            likeBtn = itemView.findViewById(R.id.likeBtn);
            post_desc_owner_name = itemView.findViewById(R.id.post_desc_owner_name);
            comments = itemView.findViewById(R.id.post_comments);
            numLikes = itemView.findViewById(R.id.numLikes);
            save2Bookmark = itemView.findViewById(R.id.save2Bookmark);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            shareBtn = itemView.findViewById(R.id.sharePostBtn);

        }
    }

}
