package com.example.insta_clone_firebase.Fragments;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.adapter.found_user_adapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchUserFrag extends Fragment {
    private EditText searchET;
    private TextView msg;
    private ImageView search;
    private RecyclerView searchList;
    ArrayList<String> allUserIDS = new ArrayList<>();
    ArrayList<String> foundId = new ArrayList<>();
    private found_user_adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_user, container, false);

        HomeScreenActivity.windowFrame.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        HomeScreenActivity.windowFrame.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.bgColor));

        HomeScreenActivity.showPost.setBackgroundResource(R.drawable.home);
        HomeScreenActivity.searchUser.setBackgroundResource(R.drawable.active_find);
        HomeScreenActivity.activeAccount.setVisibility(View.INVISIBLE);
        HomeScreenActivity.addPost.setBackgroundResource(R.drawable.add);
        HomeScreenActivity.reel.setBackgroundResource(R.drawable.reels);

        searchET = view.findViewById(R.id.searchEditText);
        search = view.findViewById(R.id.search);
        searchList = view.findViewById(R.id.searchResult);
        msg = view.findViewById(R.id.noMatchMsg);

        adapter = new found_user_adapter(foundId,getContext());
        searchList.setAdapter(adapter);
        searchList.setLayoutManager(new LinearLayoutManager(getContext()));

        getAllIds();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(searchET.getText().toString().isEmpty()){
                    return;
                }
                if(foundId.size() != 0){
                    foundId.clear();
                }
               // System.out.println("All id num = " + allUserIDS.size());
                Pattern pattern = Pattern.compile("^" + searchET.getText().toString());
                for(String i : allUserIDS){
                    Matcher matcher = pattern.matcher(i);

                    if(matcher.find()){
                       // System.out.println(i+ " -> " + matcher.find());
                        foundId.add(i);
                    }
                }
                //System.out.println("found ids = " + foundId.size());
                if(foundId.size() == 0){
                    msg.setVisibility(View.VISIBLE);
                    searchList.setVisibility(View.INVISIBLE);
                }else{
                    msg.setVisibility(View.INVISIBLE);
                    searchList.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeScreenActivity.showPost.setBackgroundResource(R.drawable.home);
        HomeScreenActivity.searchUser.setBackgroundResource(R.drawable.active_find);
        HomeScreenActivity.activeAccount.setVisibility(View.INVISIBLE);
        HomeScreenActivity.addPost.setBackgroundResource(R.drawable.add);
        HomeScreenActivity.reel.setBackgroundResource(R.drawable.reels);
    }

    private void getAllIds() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collection = db.collection("Instagram User Private Data");
        collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot query)
            {

                for(DocumentSnapshot snap : query.getDocuments()){
                    //System.out.println(snap.getId());
                    allUserIDS.add(snap.getId());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                System.out.println();
            }
        });
    }
}