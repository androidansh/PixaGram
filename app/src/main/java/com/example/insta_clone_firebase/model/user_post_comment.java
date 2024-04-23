package com.example.insta_clone_firebase.model;

public class user_post_comment {
    String userID,user_comment,userIDPic;

    public user_post_comment() {
    }

    public user_post_comment(String userID, String user_comment, String userIDPic) {
        this.userID = userID;
        this.user_comment = user_comment;
        this.userIDPic = userIDPic;
    }

    public String getUserIDPic() {
        return userIDPic;
    }

    public void setUserIDPic(String userIDPic) {
        this.userIDPic = userIDPic;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUser_comment() {
        return user_comment;
    }

    public void setUser_comment(String user_comment) {
        this.user_comment = user_comment;
    }

    public user_post_comment(String userID, String user_comment) {
        this.userID = userID;
        this.user_comment = user_comment;
    }
}
