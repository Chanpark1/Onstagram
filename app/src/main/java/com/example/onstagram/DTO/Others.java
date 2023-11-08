package com.example.onstagram.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Others {


    public Others(String isLocked, String isFollower) {
        this.isLocked = isLocked;
        this.isFollower = isFollower;
    }

    @Expose
    @SerializedName("isLocked") private String isLocked;

    @Expose
    @SerializedName("isFollower") private String isFollower;

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getIsFollower() {
        return isFollower;
    }

    public void setIsFollower(String isFollower) {
        this.isFollower = isFollower;
    }
}
