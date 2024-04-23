package com.example.insta_clone_firebase.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.bookmark_post_activity;
import com.example.insta_clone_firebase.model.post_create;

import java.util.ArrayList;

public class bookmark_small_grid_Adapter extends RecyclerView.Adapter<bookmark_small_grid_Adapter.ViewHolder> {


    ArrayList<post_create> postList;
    Context context;

    public bookmark_small_grid_Adapter(ArrayList<post_create> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public bookmark_small_grid_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.small_post_layout, parent, false);
        return new bookmark_small_grid_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bookmark_small_grid_Adapter.ViewHolder holder, int position) {
        Glide.with(context).load(postList.get(position).getPost_url()).into(holder.post);
        final int pos = position;
        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked at " + pos);
                bookmark_post_activity.show_big_bookmark(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView post;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post = itemView.findViewById(R.id.small_post_img);

        }
    }


}
