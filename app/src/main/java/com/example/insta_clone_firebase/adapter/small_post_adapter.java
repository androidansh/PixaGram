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
import com.example.insta_clone_firebase.model.post_create;

import java.util.ArrayList;

public class small_post_adapter extends RecyclerView.Adapter<small_post_adapter.ViewHolder> {

    ArrayList<post_create> postList;
    Activity activity;
    Context context;

    public small_post_adapter(ArrayList<post_create> postList, Context context,Activity activity) {
        this.postList = postList;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public small_post_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.small_post_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull small_post_adapter.ViewHolder holder, int position) {
        Glide.with(context).load(postList.get(position).getPost_url()).into(holder.post);
        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // System.out.println(activity.getSupportFragmentManager());
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView post;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post = itemView.findViewById(R.id.small_post_img);

        }
    }
}
