package com.example.insta_clone_firebase.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class create_user_model implements Parcelable {
    String user_name;
    String user_id;
    String user_uid;
    String user_password;
    String user_bio;
    String user_phone;
    String user_profile;
    String user_mail;

    private ArrayList<post_create> posts = new ArrayList<>();

    private ArrayList<post_create> reels = new ArrayList<>();
    private ArrayList<String> likedPosts = new ArrayList<>();
    private ArrayList<String> bookmarks = new ArrayList<>();
    private ArrayList<Follower_Following_model> followers = new ArrayList<>();
    private ArrayList<Follower_Following_model> followings = new ArrayList<>();

    protected create_user_model(Parcel in) {
        user_name = in.readString();
        user_id = in.readString();
        user_uid = in.readString();
        user_password = in.readString();
        user_bio = in.readString();
        user_phone = in.readString();
        user_profile = in.readString();
        user_mail = in.readString();
    }

    public static final Creator<create_user_model> CREATOR = new Creator<create_user_model>() {
        @Override
        public create_user_model createFromParcel(Parcel in) {
            return new create_user_model(in);
        }

        @Override
        public create_user_model[] newArray(int size) {
            return new create_user_model[size];
        }
    };

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }



    public ArrayList<post_create> getReels() {
        return reels;
    }

    public void setReels(ArrayList<post_create> reels) {
        this.reels = reels;
    }

    public ArrayList<String> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(ArrayList<String> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public ArrayList<String> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(ArrayList<String> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public create_user_model(String user_name, String user_id, String user_uid, String user_password, String user_bio, String user_phone, String user_profile, String user_mail, ArrayList<post_create> posts, ArrayList<String> likedPosts, ArrayList<String> bookmarks, ArrayList<Follower_Following_model> followers, ArrayList<Follower_Following_model> followings) {
        this.user_name = user_name;
        this.user_id = user_id;
        this.user_uid = user_uid;
        this.user_password = user_password;
        this.user_bio = user_bio;
        this.user_phone = user_phone;
        this.user_profile = user_profile;
        this.user_mail = user_mail;
        this.posts = posts;
        this.likedPosts = likedPosts;
        this.bookmarks = bookmarks;
        this.followers = followers;
        this.followings = followings;
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

    public ArrayList<post_create> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<post_create> posts) {
        this.posts = posts;
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

    public create_user_model() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(user_name);
        dest.writeString(user_id);
        dest.writeString(user_uid);
        dest.writeString(user_password);
        dest.writeString(user_bio);
        dest.writeString(user_phone);
        dest.writeString(user_profile);
        dest.writeString(user_mail);
    }
}
