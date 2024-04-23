package com.example.insta_clone_firebase.post_reel;


import static com.example.insta_clone_firebase.post_reel.AddPostFrag.post_uri;
import static com.example.insta_clone_firebase.post_reel.AddPostFrag.selectPic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.adapter.tabbedAdapter2;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class AddPostReelActivity extends AppCompatActivity {

    ViewPager viewPager;
    private ImageView back;
    TabItem addPost,addReel;
    static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_reel);
        activity = getParent();

        back = findViewById(R.id.back);
        viewPager = findViewById(R.id.viewPager1);
        addPost = findViewById(R.id.addPostTab);
        addReel = findViewById(R.id.addReelTab);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabbedAdapter2 adapter  = new tabbedAdapter2(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);

        back.setOnClickListener(v->{
            finish();
        });

    }
    public static void cropImage(Uri imageUri){
        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(3,4).start(activity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            post_uri = result.getUri();
            //Glide.with(this).load(post_uri).into(selectPic);
            selectPic.setImageURI(post_uri);
        }
    }
}
