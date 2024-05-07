package com.example.insta_clone_firebase.SignUpFrags;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.SignUpActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserIdFrag extends Fragment {
    private EditText createUserId;
    private ImageView edit;
    private ImageView proceed;
    private ProgressBar progress;
    private Button chkUserId;

    private boolean isUnique = false,isClicked = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_id, container, false);


        createUserId = view.findViewById(R.id.createUserID);
        chkUserId = view.findViewById(R.id.chkID);
        progress = view.findViewById(R.id.progress);
        proceed = view.findViewById(R.id.proceedUserId);
        edit = view.findViewById(R.id.editUserID);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUnique){
                    createUserId.setTextColor(getResources().getColor(R.color.dark_gray));
                }
                createUserId.setEnabled(true);
                isClicked = false;
            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked){
                    isClicked = true;
                    if(!isUnique){
                        if(createUserId.getText().toString().length() < 5){
                            createUserId.setError("user-id should at least be of 5 characters.");
                            isClicked = false;
                            return;
                        }
                        checkValidID();
                    }else{
                        SignUpActivity.createUserModel.setUser_id(createUserId.getText().toString().trim());
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.signUpFrame,new MailPassFrag());
                        ft.commit();
                    }
                }
            }
        });


        chkUserId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked){
                    isClicked = true;
                    if(!isUnique){
                        if(createUserId.getText().toString().length() < 5){
                            createUserId.setError("Please enter a valid user id of length at 5 characters.");
                            return;
                        }
                        checkValidID();
                    }else{
                        SignUpActivity.createUserModel.setUser_id(createUserId.getText().toString().trim());
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.signUpFrame,new MailPassFrag());
                        ft.commit();
                    }
                }
            }
        });

        return view;
    }

    private void checkValidID(){
        if(createUserId.getText().toString().length() > 12){
            isClicked = false;
            createUserId.setTextColor(getResources().getColor(R.color.red));
            createUserId.setError("user-id can contain at max 12 characters.");
            return;
        }
        for(int i=0;i<createUserId.getText().toString().length();i++){
            int code = createUserId.getText().toString().charAt(i);
            if(!((code >= 65 && code <= 90 ) || (code >=97 && code <= 122) ||(code >= 48 && code <= 57)|| code == 95 || code ==46)){
                isClicked = false;
                createUserId.setTextColor(getResources().getColor(R.color.red));
                createUserId.setError("user-id  can only contain 0-9|A-Z|a-z|_|.");
                return;
            }
        }
        progress.setVisibility(View.VISIBLE);
        chkUserId.setVisibility(View.INVISIBLE);
        CheckUserID(createUserId.getText().toString());

    }
    public void CheckUserID(String userid)
    {
        ArrayList<String> userIdList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collection = db.collection("Instagram User Private Data");
        collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot query)
            {
                for(DocumentSnapshot snap : query.getDocuments()){
                    userIdList.add(snap.getId());
                }
                if(!userIdList.contains(userid)){
                    chkUserId.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.INVISIBLE);
                    isUnique = true;
                    createUserId.setTextColor(getResources().getColor(R.color.green));
                    createUserId.setEnabled(false);
                    isClicked = false;
                }
                else{
                    createUserId.setError("User Id already taken.");
                    isUnique =false;
                    createUserId.setTextColor(getResources().getColor(R.color.red));
                    isClicked = false;
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                isClicked = false;
                isUnique = false;
                createUserId.setEnabled(true);
                System.out.println("Got errro in checking id = " + e.getMessage());

            }
        });

    }
}