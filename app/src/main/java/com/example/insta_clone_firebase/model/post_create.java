package com.example.insta_clone_firebase.model;

import java.util.ArrayList;

public class post_create {

    String post_owner_name,post_owner_pic,post_url,post_description,post_uid;

    ArrayList<user_post_liked> likedArray;
    String date_created;

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    ArrayList<user_post_comment> postComments;

    public String getPost_uid() {
        return post_uid;
    }

    public void setPost_uid(String post_uid) {
        this.post_uid = post_uid;
    }

    ArrayList<String> bookmarkUsers;

    public post_create(String post_owner_name, String post_owner_pic, String post_url, String post_description, String post_uid, ArrayList<user_post_liked> likedArray, ArrayList<user_post_comment> postComments, ArrayList<String> bookmarkUsers) {
        this.post_owner_name = post_owner_name;
        this.post_owner_pic = post_owner_pic;
        this.post_url = post_url;
        this.post_description = post_description;
        this.post_uid = post_uid;
        this.likedArray = likedArray;
        this.postComments = postComments;
        this.bookmarkUsers = bookmarkUsers;
    }

    public ArrayList<user_post_liked> getLikedArray() {
        return likedArray;
    }

    public void setLikedArray(ArrayList<user_post_liked> likedArray) {
        this.likedArray = likedArray;
    }

    public ArrayList<user_post_comment> getPostComments() {
        return postComments;
    }

    public void setPostComments(ArrayList<user_post_comment> postComments) {
        this.postComments = postComments;
    }

    public ArrayList<String> getBookmarkUsers() {
        return bookmarkUsers;
    }

    public void setBookmarkUsers(ArrayList<String> bookmarkUsers) {
        this.bookmarkUsers = bookmarkUsers;
    }

    public post_create() {
    }

    public String getPost_owner_name() {
        return post_owner_name;
    }

    public void setPost_owner_name(String post_owner_name) {
        this.post_owner_name = post_owner_name;
    }

    public String getPost_owner_pic() {
        return post_owner_pic;
    }

    public void setPost_owner_pic(String post_owner_pic) {
        this.post_owner_pic = post_owner_pic;
    }

    public String getPost_url() {
        return post_url;
    }

    public void setPost_url(String post_url) {
        this.post_url = post_url;
    }

    public String getPost_description() {
        return post_description;
    }

    public void setPost_description(String post_description) {
        this.post_description = post_description;
    }

    public post_create(String post_owner_name, String post_owner_pic, String post_url, String post_description) {
        this.post_owner_name = post_owner_name;
        this.post_owner_pic = post_owner_pic;
        this.post_url = post_url;
        this.post_description = post_description;
    }
}
