package com.example.insta_clone_firebase.model;

public class Follower_Following_model {
    String follower_following_id,profile_url;

    public void setFollower_following_id(String follower_following_id) {
        this.follower_following_id = follower_following_id;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getFollower_following_id() {
        return follower_following_id;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public Follower_Following_model() {
    }

    public Follower_Following_model(String follower_following_id, String profile_url) {
        this.follower_following_id = follower_following_id;
        this.profile_url = profile_url;
    }
}
