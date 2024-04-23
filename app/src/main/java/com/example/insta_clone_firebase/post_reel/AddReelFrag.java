package com.example.insta_clone_firebase.post_reel;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;
import com.example.insta_clone_firebase.model.create_user_model;
import com.example.insta_clone_firebase.model.post_create;
import com.example.insta_clone_firebase.thread_package.get_all_post_storage;
import com.example.insta_clone_firebase.thread_package.get_user_db;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AddReelFrag extends Fragment {

    private VideoView selectVideo;
    private EditText reel_desc;
    private Button reel_btn;
    private TextView whiteScr,error;
    private ProgressBar progress;
    AssetFileDescriptor fileDescriptor = null;
    Uri video_uri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_reel, container, false);

        selectVideo = view.findViewById(R.id.select_reel);
        reel_desc = view.findViewById(R.id.reel_desc);
        reel_btn = view.findViewById(R.id.post_reel);
        whiteScr = view.findViewById(R.id.whiteScr);
        error = view.findViewById(R.id.errorText);
        progress = view.findViewById(R.id.progress);

        selectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                videoIntent.setType("video/*");
                startActivityForResult(videoIntent,2);
            }
        });

        reel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(video_uri == null){
                    System.out.println("reel uri is null");
                    return;
                }
                new Thread(new thread_post()).start();
                startActivity(new Intent(getContext(), HomeScreenActivity.class));

            }
        });

        return view;
    }

    class thread_post implements Runnable{

        @Override
        public void run() {
            Handler threadHandler = new Handler(Looper.getMainLooper());
            threadHandler.post(new Runnable() {
                @Override
                public void run() {
                    reel_desc.setEnabled(false);
                    whiteScr.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.VISIBLE);
                }
            });
            add_reel2DB_storage(video_uri);
        }
    }

    private void add_reel2DB_storage(Uri videoUri) {

        final String reel_description;
        if (reel_desc.getText().toString().isEmpty()) {
            reel_description = "";
        } else {
            reel_description = reel_desc.getText().toString();
        }

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()) + "";
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()) + "";

        String image_name = "video__" + Timestamp.now().getSeconds();

        StorageReference ref = FirebaseStorage.getInstance().getReference();

        StorageReference filepath = ref.child("instagram_data").child("reels").child(image_name);
        filepath.putFile(video_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference ref1 = db.collection("Instagram_user_reel_data");
                        post_create post = new post_create();
                        post.setPost_url(uri.toString());
                        post.setPost_description(reel_description);
                        post.setPost_owner_name(HomeScreenActivity.USER_DATA.getUser_id());
                        post.setPost_owner_pic(HomeScreenActivity.USER_DATA.getUser_profile());
                        post.setLikedArray(new ArrayList<>());
                        post.setPostComments(new ArrayList<>());
                        post.setBookmarkUsers(new ArrayList<>());
                        post.setDate_created(date + "-" + time);
                        post.setPost_uid("");
                        ref1.add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                String post_db_ref = documentReference.getId();

                                DocumentReference r1 = db.collection("Instagram_user_reel_data").document(post_db_ref);
                                r1.update("post_uid",post_db_ref).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        DocumentReference ref2 = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
                                        ref2.update("reels", FieldValue.arrayUnion(post_db_ref)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                System.out.println("Reel added to db");
                                                Handler threadHandler = new Handler(Looper.getMainLooper());
                                                threadHandler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getContext(), "Your Reel has been posted.", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                                get_user_db.loadUserData(getContext());
                                                get_all_post_storage.getPosts(getContext());
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                System.out.println("error in adding reel ref to user db :: "+ e.getMessage());
                                                e.printStackTrace();
                                                Handler threadHandler = new Handler(Looper.getMainLooper());
                                                threadHandler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        reel_desc.setEnabled(true);
                                                        whiteScr.setVisibility(View.INVISIBLE);
                                                        progress.setVisibility(View.INVISIBLE);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        System.out.println("error in adding reel ref to post db :: "+ e.getMessage());
                                        e.printStackTrace();
                                        Handler threadHandler = new Handler(Looper.getMainLooper());
                                        threadHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                reel_desc.setEnabled(true);
                                                whiteScr.setVisibility(View.INVISIBLE);
                                                progress.setVisibility(View.INVISIBLE);

                                            }
                                        });
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Error in adding reel to db :: " + e.getMessage());
                                e.printStackTrace();
                                Handler threadHandler = new Handler(Looper.getMainLooper());
                                threadHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        reel_desc.setEnabled(true);
                                        whiteScr.setVisibility(View.INVISIBLE);
                                        progress.setVisibility(View.INVISIBLE);

                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("error in getting video url = " + e.getMessage());
                        e.printStackTrace();
                        Handler threadHandler = new Handler(Looper.getMainLooper());
                        threadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                reel_desc.setEnabled(true);
                                whiteScr.setVisibility(View.INVISIBLE);
                                progress.setVisibility(View.INVISIBLE);

                            }
                        });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("error in sending video to storage = " + e.getMessage());
                e.printStackTrace();
                Handler threadHandler = new Handler(Looper.getMainLooper());
                threadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        reel_desc.setEnabled(true);
                        whiteScr.setVisibility(View.INVISIBLE);
                        progress.setVisibility(View.INVISIBLE);
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
                video_uri = data.getData();
                try {
                    fileDescriptor = getContext().getContentResolver().openAssetFileDescriptor(video_uri , "r");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
//                long fileSizeInBytes = fileDescriptor.getLength();
//                long fileSizeInKB = fileSizeInBytes / 1024;
                long fileMB = ((fileDescriptor.getLength()) / 1024) /1024;
                if(fileMB > 5){
                    Toast.makeText(getContext(), "File is too big. Select file with less than 5 MB size for Reel.", Toast.LENGTH_LONG).show();
                    return;
                }
                selectVideo.setVideoURI(video_uri);
                selectVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(true);
                        int videoLength = mp.getDuration();

                        if(videoLength > 5000 && videoLength < 45000){
                            selectVideo.setBackground(null);
                            selectVideo.start();
                        }
                    }
                });

            }
        }
    }
}