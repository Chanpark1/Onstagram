package com.example.onstagram.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    public Profile(String idx, String post_num, String follower_num, String following_num, String username, String name, String content, String profile, String isLocked, String isFollower) {
        this.idx = idx;
        this.post_num = post_num;
        this.follower_num = follower_num;
        this.following_num = following_num;
        this.username = username;
        this.name = name;
        this.content = content;
        this.profile = profile;
        this.isLocked = isLocked;
        this.isFollower = isFollower;

    }

    public String getIsFollower() {
        return isFollower;
    }

    public void setIsFollower(String isFollower) {
        this.isFollower = isFollower;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getPost_num() {
        return post_num;
    }

    public void setPost_num(String post_num) {
        this.post_num = post_num;
    }

    public String getFollower_num() {
        return follower_num;
    }

    public void setFollower_num(String follower_num) {
        this.follower_num = follower_num;
    }

    public String getFollowing_num() {
        return following_num;
    }

    public void setFollowing_num(String following_num) {
        this.following_num = following_num;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    @Expose
    @SerializedName("idx") private String idx;

    @Expose
    @SerializedName("post_num") private String post_num;

    @Expose
    @SerializedName("follower_num") private String follower_num;

    @Expose
    @SerializedName("following_num") private String following_num;

    @Expose
    @SerializedName("username") private String username;

    @Expose
    @SerializedName("name") private String name;

    @Expose
    @SerializedName("content") private String content;

    @Expose
    @SerializedName("profileImage") private String profile;

    @Expose
    @SerializedName("isLocked") private String isLocked;

    @Expose
    @SerializedName("isFollower") private String isFollower;


}
