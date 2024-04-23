package com.example.insta_clone_firebase.activities;

public class dustBin {

    /*
    Add post Activity old code


    private void send_post_to_storage(Uri postUri) {

        //  metadata for image
        StorageReference ref = FirebaseStorage.getInstance().getReference();

        SharedPreferences preferences = getSharedPreferences("Pref_User_Data", MODE_PRIVATE);
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
                                            Toast.makeText(getApplicationContext(), "Your Post has been posted.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    get_user_db.loadUserData(getApplicationContext());
                                    get_all_post_storage.getPosts(getApplicationContext());
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
}
