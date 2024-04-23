package com.example.insta_clone_firebase.adapter;

import static android.graphics.Shader.TileMode.MIRROR;

import android.content.Context;
import android.graphics.RenderEffect;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.model.post_create;
import com.example.insta_clone_firebase.thread_package.get_user_db;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class bookmark_big_post_adapter extends RecyclerView.Adapter<bookmark_big_post_adapter.ViewHolder> {

    private ArrayList<post_create> bookMarkList;
    private Context context;

    public bookmark_big_post_adapter(ArrayList<post_create> bookMarkList, Context context) {
        this.bookMarkList = bookMarkList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.bookmark_post_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        post_create post = bookMarkList.get(position);
        Glide.with(context).load(post.getPost_owner_pic()).into(holder.postOwnerImg);

        holder.postOwnerName.setText(post.getPost_owner_name());

        Glide.with(context).load(post.getPost_url()).into(holder.post1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            holder.post1.setRenderEffect(RenderEffect.createBlurEffect(20,20, MIRROR));
        }
        else{
            holder.post1.setBackgroundResource(R.color.bgColor);
        }
        Glide.with(context).load(post.getPost_url()).into(holder.post2);

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.options.getTag().toString().equals("yes")){
                    holder.options.setTag("no");
                    holder.options.setBackgroundResource(R.drawable.saved_not);
                    removePost2BookMark(post);
                }
                else{
                    holder.options.setBackgroundResource(R.drawable.saved);
                    holder.options.setTag("yes");
                    savePost2BookMark(post);
                }
            }
        });

    }

    private void removePost2BookMark(post_create post) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
        ref.update("bookmarks", FieldValue.arrayRemove(post)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Post Removed from bookmark");
                get_user_db.loadUserData(context);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error in saving post to book mark" + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void savePost2BookMark(post_create post) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
        ref.update("bookmarks", FieldValue.arrayUnion(post)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Post saved to bookmark");
                get_user_db.loadUserData(context);
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
        return bookMarkList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
