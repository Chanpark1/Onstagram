package com.example.onstagram.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    public User (String idx,
                String phone,
                String username,
                String name,
                String path) {
        this.idx = idx;
        this.phone = phone;
        this.username = username;
        this.name = name;
        this.path = path;
    }

    @Expose
    @SerializedName("idx") private String idx;
    @Expose
    @SerializedName("phone") private String phone;
    @Expose
    @SerializedName("username") private String username;
    @Expose
    @SerializedName("name") private String name;
    @Expose
    @SerializedName("profileImage") private String path;

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
