package com.example.insta_clone_firebase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.model.user_post_comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    ArrayList<user_post_comment> postComments;

    Context context;

    public CommentAdapter(ArrayList<user_post_comment> postComments, Context context) {
        this.postComments = postComments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.comment_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        user_post_comment comment = postComments.get(position);
        holder.commentOwner.setText("@ " + comment.getUserID());
        holder.commentText.setText(comment.getUser_comment());
        Glide.with(context).load(comment.getUserIDPic()).into(holder.commentOwnerPic);
    }

    @Override
    public int getItemCount() {
        return postComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView commentOwner,commentText;
        ImageView commentOwnerPic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentOwner = itemView.findViewById(R.id.specificCommentOwner);
            commentText = itemView.findViewById(R.id.specificComment);
            commentOwnerPic = itemView.findViewById(R.id.specificCommentOwnerPic);
        }
    }
}
