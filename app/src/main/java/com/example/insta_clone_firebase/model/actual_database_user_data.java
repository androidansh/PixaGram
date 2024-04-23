package com.example.insta_clone_firebase.model;

import java.util.ArrayList;

public class actual_database_user_data {

    String user_name,user_id,user_uid,user_password,user_bio,user_phone,user_profile,user_mail;
    private ArrayList<String> posts = new ArrayList<>();
    private ArrayList<String> reels = new ArrayList<>();

    public ArrayList<String> getReels() {
        return reels;
    }

    public void setReels(ArrayList<String> reels) {
        this.reels = reels;
    }

    private ArrayList<String> bookmarks = new ArrayList<>();
    private ArrayList<String> liked = new ArrayList<>();
    private ArrayList<Follower_Following_model> followers = new ArrayList<>();
    private ArrayList<Follower_Following_model> followings = new ArrayList<>();

    public ArrayList<String> getLiked() {
        return liked;
    }

    public void setLiked(ArrayList<String> liked) {
        this.liked = liked;
    }

    public actual_database_user_data(String user_name, String user_id, String user_uid, String user_password, String user_bio, String user_phone, String user_profile, String user_mail, ArrayList<String> posts, ArrayList<String> dbBookmarks, ArrayList<String> liked, ArrayList<Follower_Following_model> followers, ArrayList<Follower_Following_model> followings) {
        this.user_name = user_name;
        this.user_id = user_id;
        this.user_uid = user_uid;
        this.user_password = user_password;
        this.user_bio = user_bio;
        this.user_phone = user_phone;
        this.user_profile = user_profile;
        this.user_mail = user_mail;
        this.posts = posts;
        this.bookmarks = dbBookmarks;
        this.liked = liked;
        this.followers = followers;
        this.followings = followings;
    }

    public actual_database_user_data() {
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_bio() {
        return user_bio;
    }

    public void setUser_bio(String user_bio) {
        this.user_bio = user_bio;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public ArrayList<String> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<String> posts) {
        this.posts = posts;
    }

    public ArrayList<String> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(ArrayList<String> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public ArrayList<Follower_Following_model> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<Follower_Following_model> followers) {
        this.followers = followers;
    }

    public ArrayList<Follower_Following_model> getFollowings() {
        return followings;
    }

    public void setFollowings(ArrayList<Follower_Following_model> followings) {
        this.followings = followings;
    }
}
