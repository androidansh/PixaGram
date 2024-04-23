package com.example.insta_clone_firebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.Search_User_Activity;

import java.util.ArrayList;

public class found_user_adapter extends RecyclerView.Adapter<found_user_adapter.ViewHolder> {
    private ArrayList<String> idList;
    Context context;

    public found_user_adapter(ArrayList<String> idList, Context context) {
        this.idList = idList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.found_id_small_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.foundId.setText(idList.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Search_User_Activity.class);
                i.putExtra("userID",idList.get(position));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return idList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView foundId;
        private ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foundId = itemView.findViewById(R.id.foundId);
            layout = itemView.findViewById(R.id.foundIdLayout);
        }
    }

}
