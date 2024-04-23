package com.example.insta_clone_firebase.SignUpFrags;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
    private TextView whiteScr;
    private ProgressBar progress;
    private Button chkUserId , next;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_id, container, false);


        createUserId = view.findViewById(R.id.createUserID);
        chkUserId = view.findViewById(R.id.chkID);
        next = view.findViewById(R.id.next);
        whiteScr = view.findViewById(R.id.whiteScr);
        progress = view.findViewById(R.id.progress);

        edit = view.findViewById(R.id.editUserID);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.setVisibility(View.INVISIBLE);

                chkUserId.setVisibility(View.VISIBLE);
                chkUserId.setEnabled(true);
                createUserId.setEnabled(true);
            }
        });


        chkUserId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(createUserId.getText().toString().length() < 5){
                    createUserId.setError("Please enter a valid user id of length at 5 characters.");
                    return;
                }
                whiteScr.setVisibility(View.VISIBLE);
                progress.setVisibility(View.VISIBLE);
                chkUserId.setEnabled(false);
                CheckUserID(createUserId.getText().toString());
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity.createUserModel.setUser_id(createUserId.getText().toString().trim());
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.signUpFrame,new MailPassFrag());
                ft.commit();
            }
        });


        return view;
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
                    //System.out.println(snap.getId());
                    userIdList.add(snap.getId());
                }
                if(!userIdList.contains(userid)){
                    chkUserId.setVisibility(View.INVISIBLE);
                    whiteScr.setVisibility(View.INVISIBLE);
                    progress.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                    createUserId.setEnabled(false);
                }
                else{
                    createUserId.setError("User Id already taken.");

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {

            }
        });

    }
}