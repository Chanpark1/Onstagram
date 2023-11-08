package com.example.onstagram.DTO;

import android.media.Image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Post {

    public Post(String postIdx,
                String userIdx,
                String userImage,
                String username,
                ArrayList<ImagePath> image_path,
                String content,
                String like_num,
                String isLike) {
        this.postIdx = postIdx;
        this.userIdx = userIdx;
        this.userImage = userImage;
        this.username = username;
        this.image_path = image_path;
        this.content = content;
        this.like_num = like_num;
        this.isLike = isLike;
    }

    public String getPostIdx() {
        return postIdx;
    }

    public void setPostIdx(String postIdx) {
        this.postIdx = postIdx;
    }

    public String getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(String userIdx) {
        this.userIdx = userIdx;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<ImagePath> getImage_path() {
        return image_path;
    }

    public void setImage_path(ArrayList<ImagePath> image_path) {
        this.image_path = image_path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    @Expose
    @SerializedName("postIdx") private String postIdx;

    @Expose
    @SerializedName("userIdx") private String userIdx;

    @Expose
    @SerializedName("userImage") private String userImage;

    @Expose
    @SerializedName("username") private String username;

    @Expose
    @SerializedName("image_path") private ArrayList<ImagePath> image_path;

    @Expose
    @SerializedName("content") private String content;

    @Expose
    @SerializedName("like_num") private String like_num;

    @Expose
    @SerializedName("isLike") private String isLike;

//    @Expose
//    @SerializedName("comment") private String comment;
//
//    @Expose
//    @SerializedName("user_like") private ArrayList<String> user_like;


}
