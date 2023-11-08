package com.example.onstagram.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupMessage {

    public GroupMessage(String message_idx, String sender_idx, String content, String profileImage, int viewType, String room_idx, String username, String name) {
        this.message_idx = message_idx;
        this.sender_idx = sender_idx;
        this.content = content;
        this.profileImage = profileImage;
        ViewType = viewType;
        this.room_idx = room_idx;
        this.username = username;
        this.name = name;
    }

    @Expose
    @SerializedName("idx") private String message_idx;

    @Expose
    @SerializedName("sender_idx") private String sender_idx;

    @Expose
    @SerializedName("content") private String content;

    @Expose
    @SerializedName("profileImage") private String profileImage;

    @Expose
    @SerializedName("ViewType") private int ViewType;

    @Expose
    @SerializedName("room_idx") private String room_idx;

    @Expose
    @SerializedName("username") private String username;

    @Expose
    @SerializedName("name") private String name;

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

    public String getMessage_idx() {
        return message_idx;
    }

    public void setMessage_idx(String message_idx) {
        this.message_idx = message_idx;
    }

    public String getSender_idx() {
        return sender_idx;
    }

    public void setSender_idx(String sender_idx) {
        this.sender_idx = sender_idx;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public int getViewType() {
        return ViewType;
    }

    public void setViewType(int viewType) {
        ViewType = viewType;
    }

    public String getRoom_idx() {
        return room_idx;
    }

    public void setRoom_idx(String room_idx) {
        this.room_idx = room_idx;
    }
}
