package com.example.insta_clone_firebase.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.activities.LoginActivity;
import com.example.insta_clone_firebase.activities.SignUpActivity;
import com.example.insta_clone_firebase.for_got_pass_word.forgotActivity;
import com.example.insta_clone_firebase.thread_package.get_user_db;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class EditProfileFrag extends Fragment {

    ConstraintLayout editProfilePic,collapseProfile,editPersonalData,collapsePersonal;
    ImageView profile_drop_down,personal_drop_down,profile_drop_up,personal_drop_up;

    private ImageView setting_profile_pic,camera,editUserName,editUserBio,editPhoneNumber,back;
    private EditText setting_userName,setting_Bio,setting_number;
    private Button profile_cancel,profile_save,personal_cancel,personal_save;
    private LinearLayout.LayoutParams show,hide;
    private  Uri newProfilePic;
    private Button changePassword,logout;
    private Boolean profileClick = true,personalClick = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        back = view.findViewById(R.id.back3);
        back.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().popBackStack();
        });

        initialiseView(view);
        setValue2View();

//  collapse layout
        show = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        hide = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);

        editProfilePic.setOnClickListener(v -> animateProfile());
        editPersonalData.setOnClickListener(v -> animatePersonal());

        editUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting_userName.setEnabled(true);
                setting_Bio.setEnabled(false);
                setting_number.setEnabled(false);

                setting_userName.setBackgroundResource(R.drawable.setting_et_white_black_border);
                setting_Bio.setBackgroundResource(R.drawable.et_setting_bg);
                setting_number.setBackgroundResource(R.drawable.et_setting_bg);

                editUserName.setBackgroundResource(R.drawable.edit);
                editUserBio.setBackgroundResource(R.drawable.setting_gray_edit);
                editPhoneNumber.setBackgroundResource(R.drawable.setting_gray_edit);
            }
        });

        editUserBio.setOnClickListener(v ->{
            setting_userName.setEnabled(false);
            setting_Bio.setEnabled(true);
            setting_number.setEnabled(false);

            setting_userName.setBackgroundResource(R.drawable.et_setting_bg);
            setting_Bio.setBackgroundResource(R.drawable.setting_et_white_black_border);
            setting_number.setBackgroundResource(R.drawable.et_setting_bg);

            editUserName.setBackgroundResource(R.drawable.setting_gray_edit);
            editUserBio.setBackgroundResource(R.drawable.edit);
            editPhoneNumber.setBackgroundResource(R.drawable.setting_gray_edit);
        });

        editPhoneNumber.setOnClickListener( v ->{
            setting_userName.setEnabled(false);
            setting_Bio.setEnabled(false);
            setting_number.setEnabled(true);

            setting_userName.setBackgroundResource(R.drawable.et_setting_bg);
            setting_Bio.setBackgroundResource(R.drawable.et_setting_bg);
            setting_number.setBackgroundResource(R.drawable.setting_et_white_black_border);

            editUserName.setBackgroundResource(R.drawable.setting_gray_edit);
            editUserBio.setBackgroundResource(R.drawable.setting_gray_edit);
            editPhoneNumber.setBackgroundResource(R.drawable.edit);
        });

        profile_cancel.setOnClickListener(v->{
            profileClick = true;
            if(newProfilePic != null){
                newProfilePic = null;
                Glide.with(getContext()).load(HomeScreenActivity.USER_DATA.getUser_profile()).into(setting_profile_pic);
            }
            profile_drop_down.setVisibility(View.VISIBLE);
            profile_drop_up.setVisibility(View.INVISIBLE);
            collapseProfile.setLayoutParams(hide);
        });

        personal_cancel.setOnClickListener(v->{
            personalClick = true;
            personal_drop_down.setVisibility(View.VISIBLE);
            personal_drop_up.setVisibility(View.INVISIBLE);
            collapsePersonal.setLayoutParams(hide);

            setting_userName.setEnabled(false);
            setting_Bio.setEnabled(false);
            setting_number.setEnabled(false);

            setting_userName.setBackgroundResource(R.drawable.et_setting_bg);
            setting_Bio.setBackgroundResource(R.drawable.et_setting_bg);
            setting_number.setBackgroundResource(R.drawable.et_setting_bg);

            editUserName.setBackgroundResource(R.drawable.setting_gray_edit);
            editUserBio.setBackgroundResource(R.drawable.setting_gray_edit);
            editPhoneNumber.setBackgroundResource(R.drawable.setting_gray_edit);
        });

        camera.setOnClickListener( v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent,2);
        });

        profile_save.setOnClickListener(v->{
            if(newProfilePic == null){
                return;
            }
            Toast.makeText(getContext(), "Profile Photo updated.", Toast.LENGTH_SHORT).show();
            new Thread(new updateProfile()).start();
        });

        personal_save.setOnClickListener(v->{
            update_personal_info();
        });


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseAuth auth = FirebaseAuth.getInstance();
//                String emailAddress = HomeScreenActivity.USER_DATA.getUser_mail();
//                auth.sendPasswordResetEmail(emailAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        System.out.println("Email sent to " + emailAddress);
//                        Toast.makeText(getContext(), "Email sent", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        System.out.println("Error in sending change password email.");
//                    }
//                });
                startActivity(new Intent(getActivity(), forgotActivity.class));

            }
        });
        
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref =getActivity().getSharedPreferences("Pref_Logged_Session", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("database_id","");
                edit.putBoolean("isLogged",false);
                edit.apply();

                pref = getActivity().getSharedPreferences("Pref_User_Data", Context.MODE_PRIVATE);
                edit = pref.edit();
                edit.putString("User_data","");
                edit.apply();

                pref = getActivity().getSharedPreferences("Pref_All_Post", Context.MODE_PRIVATE);
                edit = pref.edit();
                edit.putString("all_post","");
                edit.apply();

                pref = getActivity().getSharedPreferences("Pref_User_Post",Context.MODE_PRIVATE);
                edit = pref.edit();
                edit.putString("User_Post","");
                edit.apply();

                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();

                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });



        return view;
    }

    private void update_personal_info() {

        String newUserName = setting_userName.getText().toString();
        String newUserBio = setting_Bio.getText().toString();
        String newPhone = setting_number.getText().toString();

        if(newUserName.equals("") && newUserBio.equals("") && newPhone.equals("")){
            return;
        }
        if(newPhone.length() > 0 && newPhone.length() != 10){
            setting_number.setError("Invalid Number provided.");
            return;
        }
        if(newUserName.length() > 0 ){
            HomeScreenActivity.USER_DATA.setUser_name(newUserName);
        }
        if(newUserBio.length()>0){
            HomeScreenActivity.USER_DATA.setUser_name(newUserBio);
        }
        if(newPhone.length()>0){
            HomeScreenActivity.USER_DATA.setUser_phone(newPhone);
        }
        Toast.makeText(getContext(), "Personal Details updated.", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference ref = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
                ref.set(HomeScreenActivity.USER_DATA).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused)
                    {

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
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 2){
            if(data != null){
                Uri uri = data.getData();
                CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(3,4).start(getContext(), this);
            }
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            newProfilePic = result.getUri();
            setting_profile_pic.setImageURI(newProfilePic);
        }
    }
    private void setValue2View() {

        Glide.with(getContext()).load(HomeScreenActivity.USER_DATA.getUser_profile()).into(setting_profile_pic);

        if(HomeScreenActivity.USER_DATA.getUser_name() == null || HomeScreenActivity.USER_DATA.getUser_name().equals("")){
            setting_userName.setHint("no user name provided.");
            setting_userName.setHintTextColor(getResources().getColor(R.color.dark_gray));
        }else{
            setting_userName.setHint(HomeScreenActivity.USER_DATA.getUser_name());
            setting_userName.setHintTextColor(getResources().getColor(R.color.textColor));
        }
        if(HomeScreenActivity.USER_DATA.getUser_bio() == null || HomeScreenActivity.USER_DATA.getUser_bio().equals("")){
            setting_Bio.setHint("no bio provided.");
            setting_Bio.setHintTextColor(getResources().getColor(R.color.dark_gray));

        }else{
            setting_Bio.setHint(HomeScreenActivity.USER_DATA.getUser_bio());
            setting_Bio.setHintTextColor(getResources().getColor(R.color.textColor));
        }
        if(HomeScreenActivity.USER_DATA.getUser_phone() == null || HomeScreenActivity.USER_DATA.getUser_phone().equals("")){
            setting_number.setHint("no phone number provided.");
            setting_number.setHintTextColor(getResources().getColor(R.color.dark_gray));
        }else{
            setting_number.setHint(HomeScreenActivity.USER_DATA.getUser_phone());
            setting_number.setHintTextColor(getResources().getColor(R.color.textColor));
        }
    }

    private void initialiseView(View view){
        editProfilePic = view.findViewById(R.id.edit_profile_pic);
        collapseProfile = view.findViewById(R.id.collapse_profile);
        profile_drop_down = view.findViewById(R.id.profile_drop_down);
        profile_drop_up = view.findViewById(R.id.profile_drop_up);

        setting_profile_pic = view.findViewById(R.id.setting_profile_pic);
        profile_save = view.findViewById(R.id.profile_save);
        profile_cancel = view.findViewById(R.id.profile_cancel);
        camera = view.findViewById(R.id.camera);

        editPersonalData = view.findViewById(R.id.edit_personal_data);
        collapsePersonal = view.findViewById(R.id.collapse_personal_data);
        personal_drop_down = view.findViewById(R.id.personal_drop_down);
        personal_drop_up = view.findViewById(R.id.personal_drop_up);

        setting_userName = view.findViewById(R.id.personal_userName);
        setting_Bio = view.findViewById(R.id.personal_userBio);
        setting_number = view.findViewById(R.id.personal_userPhone);
        editUserName = view.findViewById(R.id.personal_UserNameEdit);
        editUserBio = view.findViewById(R.id.personal_UserBioEdit);
        editPhoneNumber = view.findViewById(R.id.personal_UserPhoneEdit);
        personal_save = view.findViewById(R.id.personal_save);
        personal_cancel = view.findViewById(R.id.personal_cancel);

        changePassword = view.findViewById(R.id.changePAssword);
        logout = view.findViewById(R.id.logout);
    }
    private void animateProfile(){
        if(profileClick){
            profileClick = false;
            profile_drop_down.setVisibility(View.INVISIBLE);
            profile_drop_up.setVisibility(View.VISIBLE);
            collapseProfile.setLayoutParams(show);
        }
        else{
            profileClick = true;
            profile_drop_down.setVisibility(View.VISIBLE);
            profile_drop_up.setVisibility(View.INVISIBLE);
            collapseProfile.setLayoutParams(hide);
        }
    }
    private void animatePersonal(){
        if(personalClick){
            personalClick = false;
            personal_drop_down.setVisibility(View.INVISIBLE);
            personal_drop_up.setVisibility(View.VISIBLE);
            collapsePersonal.setLayoutParams(show);
        }
        else{
            personalClick = true;
            personal_drop_down.setVisibility(View.VISIBLE);
            personal_drop_up.setVisibility(View.INVISIBLE);
            collapsePersonal.setLayoutParams(hide);
        }
    }

    class updateProfile implements Runnable{

        @Override
        public void run() {
            updateProfilePhoto();
        }
    }

    private void updateProfilePhoto(){
        if(newProfilePic == null){
            return;
        }
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(HomeScreenActivity.USER_DATA.getUser_profile());
        final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("instagram_data").child("profile_pics")
                .child("img" + Timestamp.now().getSeconds());
        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                filepath.putFile(newProfilePic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference reference = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
                                reference.update("user_profile",uri).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused)
                                    {
                                        System.out.println("Profile updated in storage and db");
                                        get_user_db.loadUserData(getContext());
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
                                System.out.println("error in getting download url :: " + e.getMessage());
                                e.printStackTrace();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error  in putting new profile pic :: " + e.getMessage());
                        e.printStackTrace();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error  in deleting image from storage. :: " + e.getMessage());
                e.printStackTrace();
            }
        });

    }
}