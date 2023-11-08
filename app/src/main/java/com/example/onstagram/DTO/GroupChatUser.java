package com.example.onstagram.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GroupChatUser {
    public GroupChatUser(ArrayList<String> idx_list, String room_idx, String username) {
        this.idx_list = idx_list;
        this.room_idx = room_idx;
        this.username = username;
    }

    @Expose
    @SerializedName("idx_list") private ArrayList<String> idx_list;

    @Expose
    @SerializedName("room_idx") private String room_idx;

    @Expose
    @SerializedName("username") private String username;


    public ArrayList<String> getIdx_list() {
        return idx_list;
    }

    public void setIdx_list(ArrayList<String> idx_list) {
        this.idx_list = idx_list;
    }

    public String getRoom_idx() {
        return room_idx;
    }

    public void setRoom_idx(String room_idx) {
        this.room_idx = room_idx;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
