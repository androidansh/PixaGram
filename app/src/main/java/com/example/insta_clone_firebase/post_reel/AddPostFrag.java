package com.example.insta_clone_firebase.post_reel;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class AddPostFrag extends Fragment {
    public static  ImageView  selectPic;
    private EditText post_desc;
    private Button post_btn;
    private TextView whiteScr;
    private ProgressBar progress;
    public static Uri post_uri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);

        selectPic = view.findViewById(R.id.select_post);
        post_desc = view.findViewById(R.id.post_desc);
        post_btn = view.findViewById(R.id.post_post);
        whiteScr = view.findViewById(R.id.whiteScr);
        progress = view.findViewById(R.id.progress);

        selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,1);
            }
        });

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(post_uri == null){
                    System.out.println("POst uri is null");
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
                    post_desc.setEnabled(false);
                    whiteScr.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.VISIBLE);
                }
            });
            add_post2DB_storage(post_uri);
        }
    }

    private void add_post2DB_storage(Uri post_uri){

        final String post_description;
        if (post_desc.getText().toString().isEmpty()) {
            post_description = "";
        } else {
            post_description = post_desc.getText().toString();
        }


        String image_name = "img__" + Timestamp.now().getSeconds();

        StorageReference ref = FirebaseStorage.getInstance().getReference();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()) + "";
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()) + "";

        StorageReference filepath = ref.child("instagram_data").child("posts").child(image_name);
        filepath.putFile(post_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference ref1 = db.collection("Instagram_user_post_data");
                        post_create post = new post_create();
                        post.setPost_url(uri.toString());
                        post.setPost_description(post_description);
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

                                DocumentReference r1 = db.collection("Instagram_user_post_data").document(post_db_ref);
                                r1.update("post_uid",post_db_ref).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        DocumentReference ref2 = db.collection("Instagram User Private Data").document(HomeScreenActivity.USER_DATA.getUser_id());
                                        ref2.update("posts",FieldValue.arrayUnion(post_db_ref)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                System.out.println("Post added to db");
                                                Handler threadHandler = new Handler(Looper.getMainLooper());
                                                threadHandler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getContext(), "Your Post has been posted.", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                                get_user_db.loadUserData(getContext());
                                                get_all_post_storage.getPosts(getContext());
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                System.out.println("error in adding post ref to user db :: "+ e.getMessage());
                                                e.printStackTrace();
                                                Handler threadHandler = new Handler(Looper.getMainLooper());
                                                threadHandler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        post_desc.setEnabled(true);
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
                                        System.out.println("error in adding post ref to post db :: "+ e.getMessage());
                                        e.printStackTrace();
                                        Handler threadHandler = new Handler(Looper.getMainLooper());
                                        threadHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                post_desc.setEnabled(true);
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
                                System.out.println("Error in adding post to db :: " + e.getMessage());
                                e.printStackTrace();
                                Handler threadHandler = new Handler(Looper.getMainLooper());
                                threadHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        post_desc.setEnabled(true);
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
                        System.out.println("error in getting file url = " + e.getMessage());
                        e.printStackTrace();
                        Handler threadHandler = new Handler(Looper.getMainLooper());
                        threadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                post_desc.setEnabled(true);
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
                System.out.println("error in sending file to storage = " + e.getMessage());
                e.printStackTrace();
                Handler threadHandler = new Handler(Looper.getMainLooper());
                threadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        post_desc.setEnabled(true);
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
        if(resultCode == RESULT_OK && requestCode == 1){
            if(data != null){
                Uri uri = data.getData();
                CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(3,4).start(getActivity());
            }
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            post_uri = result.getUri();
            Glide.with(getContext()).load(post_uri).into(selectPic);
            //selectPic.setImageURI(post_uri);
        }
    }


}

/*
   private void send_post_to_storage(Uri postUri) {

        //  metadata for image
        StorageReference ref = FirebaseStorage.getInstance().getReference();

        SharedPreferences preferences = getContext().getSharedPreferences("Pref_User_Data", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("User_data", "");
        create_user_model obj = gson.fromJson(json, create_user_model.class);
        final String post_description;
        if (post_desc.getText().toString().isEmpty()) {
            post_description = "";
        } else {
            post_description = post_desc.getText().toString();
        }


        String image_name = "img__" + Timestamp.now().getSeconds();

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("user_id", obj.getUser_id())
                .setCustomMetadata("user_profile", obj.getUser_profile())
                .setCustomMetadata("post_desc", post_description)
                .setContentType("image/jpeg").build();

        StorageReference filepath = ref.child("instagram_data").child("posts")
                .child(image_name);
        filepath.putFile(post_uri, metadata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println("url = " + uri);
                        post_create userPost = new post_create();
                        userPost.setPost_description(post_description);
                        userPost.setPost_owner_name(obj.getUser_id());
                        userPost.setPost_owner_pic(obj.getUser_profile());
                        userPost.setPost_url(uri.toString());
                        userPost.setPost_uid("");

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference documentReference = db.collection("Instagram User Private Data").document(obj.getUser_id());
                        documentReference.update("posts", FieldValue.arrayUnion(userPost)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                System.out.println("Post added to db");
                                Handler threadHandler = new Handler(Looper.getMainLooper());
                                threadHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Your Post has been posted.", Toast.LENGTH_LONG).show();
                                    }
                                });
                                get_user_db.loadUserData(getContext());
                                get_all_post_storage.getPosts(getContext());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Handler threadHandler = new Handler(Looper.getMainLooper());
                                threadHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        post_desc.setEnabled(true);
                                        back.setEnabled(true);
                                        whiteScr.setVisibility(View.INVISIBLE);
                                        progress.setVisibility(View.INVISIBLE);
                                    }
                                });
                                System.out.println("error in putting file in db = " + e.getMessage());
                                e.printStackTrace();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("error in getting file = " + e.getMessage());
                        e.printStackTrace();
                        Handler threadHandler = new Handler(Looper.getMainLooper());
                        threadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                post_desc.setEnabled(true);
                                back.setEnabled(true);
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
                System.out.println("error in sending file to storage = " + e.getMessage());
                e.printStackTrace();
                Handler threadHandler = new Handler(Looper.getMainLooper());
                threadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        post_desc.setEnabled(true);
                        back.setEnabled(true);
                        whiteScr.setVisibility(View.INVISIBLE);
                        progress.setVisibility(View.INVISIBLE);

                    }
                });
            }
        });


    }
 */