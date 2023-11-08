package com.example.onstagram.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    public Message(String idx, String from_idx, String to_idx, String content, String userImage, int viewType, String image, String room_idx) {
        this.idx = idx;
        this.from_idx = from_idx;
        this.to_idx = to_idx;
        this.content = content;
        this.userImage = userImage;
        ViewType = viewType;
        this.image = image;
        this.room_idx = room_idx;
    }

    @Expose
    @SerializedName("idx") private String idx;

    @Expose
    @SerializedName("from_idx") private String from_idx;

    @Expose
    @SerializedName("to_idx") private String to_idx;

    @Expose
    @SerializedName("content") private String content;

    @Expose
    @SerializedName("userImage") private String userImage;

    @Expose
    @SerializedName("ViewType") private int ViewType;

    @Expose
    @SerializedName("image") private String image;

    @Expose
    @SerializedName("room_idx") private String room_idx;

    public String getRoom_idx() {
        return room_idx;
    }

    public void setRoom_idx(String room_idx) {
        this.room_idx = room_idx;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getFrom_idx() {
        return from_idx;
    }

    public void setFrom_idx(String from_idx) {
        this.from_idx = from_idx;
    }

    public String getTo_idx() {
        return to_idx;
    }

    public void setTo_idx(String to_idx) {
        this.to_idx = to_idx;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public int getViewType() {
        return ViewType;
    }

    public void setViewType(int viewType) {
        ViewType = viewType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
