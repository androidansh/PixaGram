package com.example.insta_clone_firebase.model;

public class user_post_liked {

    String liked_user_name;
    String liked_user_profile;

    public user_post_liked() {
    }

    public String getLiked_user_name() {
        return liked_user_name;
    }

    public void setLiked_user_name(String liked_user_name) {
        this.liked_user_name = liked_user_name;
    }

    public String getLiked_user_profile() {
        return liked_user_profile;
    }

    public void setLiked_user_profile(String liked_user_profile) {
        this.liked_user_profile = liked_user_profile;
    }

    public user_post_liked(String liked_user_name, String liked_user_profile) {
        this.liked_user_name = liked_user_name;
        this.liked_user_profile = liked_user_profile;
    }
}
