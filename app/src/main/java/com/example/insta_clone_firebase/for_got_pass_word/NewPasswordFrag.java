package com.example.insta_clone_firebase.for_got_pass_word;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.thread_package.get_user_db;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class NewPasswordFrag extends Fragment {
    private ImageView back,showPass1,showPass2;
    private EditText newPass1,newPass2;
    private Button resetPassword;

    private Boolean show1 = false,show2 = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_password, container, false);
        back = view.findViewById(R.id.backForgot);
        newPass1 = view.findViewById(R.id.newPass1);
        newPass2 = view.findViewById(R.id.newPass2);
        showPass1 = view.findViewById(R.id.showPass1);
        showPass2 = view.findViewById(R.id.showPass2);
        resetPassword = view.findViewById(R.id.reset_password);

        showPass1.setOnClickListener(v->{
            if(newPass1.getText().toString().length()<1){
                return;
            }
            if(show1){
                show1 = false;
                showPass1.setBackgroundResource(R.drawable.eye_on);
                newPass1.setInputType(InputType.TYPE_CLASS_TEXT);
            }
            else{
                show1 = true;
                showPass1.setBackgroundResource(R.drawable.eye_off);
                newPass1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
        showPass2.setOnClickListener(v->{
            if(newPass2.getText().toString().length()<1){
                return;
            }
            if(show2){
                show2 = false;
                showPass2.setBackgroundResource(R.drawable.eye_on);
                newPass2.setInputType(InputType.TYPE_CLASS_TEXT);
            }
            else{
                show2 = true;
                showPass2.setBackgroundResource(R.drawable.eye_off);
                newPass2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        resetPassword.setOnClickListener(v->{

            if(resetPassword.getText().toString().equals("Continue.")){
                requireActivity().finish();
            }
            String password1 = newPass1.getText().toString();
            String password2 = newPass2.getText().toString();

            if(password1.length() == 0 && password2.length() == 0){
                newPass1.setError("Type new Password");
                return ;
            }
            if(!password1.equals(password2)){
                newPass1.setError("Password do not match.");
                newPass2.setError("Password do not match.");
                return;
            }
            reset_pass_word2(password1);
        });
        back.setOnClickListener(v->{
            getActivity().finish();
        });

        return view;
    }

    private void reset_pass_word(String password) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(HomeScreenActivity.USER_DATA.getUser_mail(), HomeScreenActivity.USER_DATA.getUser_password());

        user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference reference = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
                        reference.update("user_password",password).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused)
                            {
                                System.out.println("Profile updated in storage and db");
                                Toast.makeText(getContext(), "Password re-set successful.", Toast.LENGTH_SHORT).show();
                                resetPassword.setText("Continue.");
                                get_user_db.loadUserData(getContext());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("error in saving data in database in forgot password::" + e.getMessage());
                                e.printStackTrace();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error in resetting password :: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error in re-authenticating :: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void reset_pass_word2(String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(HomeScreenActivity.USER_DATA.getUser_mail(), HomeScreenActivity.USER_DATA.getUser_password());
        auth.signOut();
        FirebaseAuth auth1 = FirebaseAuth.getInstance();
        auth1.signInWithEmailAndPassword(HomeScreenActivity.USER_DATA.getUser_mail(),HomeScreenActivity.USER_DATA.getUser_password()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                auth1.getCurrentUser().updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference reference = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
                        reference.update("user_password",password).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused)
                            {
                                HomeScreenActivity.USER_DATA.setUser_password(password);
                                System.out.println("Profile updated in storage and db");
                                Toast.makeText(getContext(), "Password re-set successful.", Toast.LENGTH_SHORT).show();
                                resetPassword.setText("Continue.");
                                newPass1.setEnabled(false);
                                newPass1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                newPass2.setEnabled(false);
                                newPass2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("error in saving data in database in forgot password::" + e.getMessage());
                                e.printStackTrace();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error in resetting password :: " + e.getMessage());
                        e.printStackTrace();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error in login in forgot :: " + e.getMessage());
                e.printStackTrace();
            }
        });


    }
}