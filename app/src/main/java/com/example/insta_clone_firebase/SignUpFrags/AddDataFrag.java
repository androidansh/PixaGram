package com.example.insta_clone_firebase.SignUpFrags;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.SignUpActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import kotlin.Unit;

public class AddDataFrag extends Fragment {

    private EditText createBio,createPhone;
    private ImageView profile;
    private Button createAccount;
    private Uri profileUri;
    private TextView whiteScr;
    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_data, container, false);
        profile = view.findViewById(R.id.profile);
        createBio = view.findViewById(R.id.createBio);
        createPhone = view.findViewById(R.id.createNum);
        createAccount = view.findViewById(R.id.createAccount);
        whiteScr = view.findViewById(R.id.whiteScr);
        progress = view.findViewById(R.id.progress);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profileUri == null){
                    Toast.makeText(getContext(), "Please select your profile pic.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    whiteScr.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.VISIBLE);
                    new Thread(new StoreRun()).start();
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,1);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(resultCode == RESULT_OK && requestCode == 1){
            if(data != null){
                Uri uri = data.getData();
                CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(3,4).start(getContext(), this);
//                CropImage.activity()
//                        .start();
            }
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            profileUri = result.getUri();
            //System.out.println(profileUri);
            profile.setImageURI(profileUri);
            //Glide.with(getContext()).load(resultUri).into(profile);

        }
    }


    class StoreRun implements Runnable{

        @Override
        public void run() {
            storeProfile();
        }
    }
    private void storeProfile(){
        if(profileUri == null){
            return;
        }
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        final StorageReference filepath = ref.child("instagram_data").child("profile_pics")
                .child("img" + Timestamp.now().getSeconds());
        filepath.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        SignUpActivity.createUserModel.setUser_profile(uri.toString());
                        if(createBio.getText().toString().isEmpty()){
                            SignUpActivity.createUserModel.setUser_bio("");
                        }else{
                            SignUpActivity.createUserModel.setUser_bio(createBio.getText().toString());
                        }
                        if(createPhone.getText().toString().isEmpty()){
                            SignUpActivity.createUserModel.setUser_phone("");
                        }
                        else{
                            SignUpActivity.createUserModel.setUser_phone(createPhone.getText().toString());
                        }
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference ref = db.collection("Instagram User Private Data").document(SignUpActivity.createUserModel.getUser_id());
                        ref.set(SignUpActivity.createUserModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused)
                            {
                                SharedPreferences pref = getActivity().getSharedPreferences("Pref_Logged_Session", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = pref.edit();
                                edit.putString("database_id",SignUpActivity.createUserModel.getUser_id());
                                edit.putBoolean("isLogged",true);
                                edit.apply();
                                getContext().startActivity(new Intent(getContext(), HomeScreenActivity.class));
                                getActivity().finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "error in saving data in database", Toast.LENGTH_SHORT).show();
                                System.out.println("error in saving data in database ::" + e.getMessage());
                                e.printStackTrace();

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "error in getting download url", Toast.LENGTH_SHORT).show();
                        System.out.println("error in getting download url ::" + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error in saving image", Toast.LENGTH_SHORT).show();
                System.out.println("Error in saving image ::" + e.getMessage());
                e.printStackTrace();
            }
        });
    }


}



/*

com.google.firebase.auth.FirebaseAuthWeakPasswordException: The given password is invalid. [ Password should be at least 6 characters ]
if password is weak or 6 letters

 */