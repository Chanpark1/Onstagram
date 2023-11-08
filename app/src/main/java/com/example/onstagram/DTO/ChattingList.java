package com.example.onstagram.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.FormUrlEncoded;

public class ChattingList {

    public ChattingList( String message, String room_idx, int count_members, List<User> list, ArrayList<String> idx_list) {

        this.message = message;
        this.room_idx = room_idx;
        this.count_members = count_members;
        this.list = list;
        this.idx_list = idx_list;
    }


    @Expose
    @SerializedName("user_info") private List<User> list;

    @Expose
    @SerializedName("message") private String message;

    @Expose
    @SerializedName("room_idx") private String room_idx;

    @Expose
    @SerializedName("count_members") private int count_members;

    @Expose
    @SerializedName("idx_list") private ArrayList<String> idx_list;

    public ArrayList<String> getIdx_list() {
        return idx_list;
    }

    public void setIdx_list(ArrayList<String> idx_list) {
        this.idx_list = idx_list;
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoom_idx() {
        return room_idx;
    }

    public void setRoom_idx(String room_idx) {
        this.room_idx = room_idx;
    }

    public int getCount_members() {
        return count_members;
    }

    public void setCount_members(int count_members) {
        this.count_members = count_members;
    }

}
