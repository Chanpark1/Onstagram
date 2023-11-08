package com.example.onstagram.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfilePost {

    public ProfilePost(String postIdx, String thumbnail, String image_num, String idx) {
        this.postIdx = postIdx;
        this.thumbnail = thumbnail;
        this.image_num = image_num;
        this.idx = idx;
    }


    @Expose
    @SerializedName("postIdx") private String postIdx;

    @Expose
    @SerializedName("thumbnail") private String thumbnail;

    @Expose
    @SerializedName("image_num") private String image_num;

    @Expose
    @SerializedName("idx") private String idx;

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getPostIdx() {
        return postIdx;
    }

    public void setPostIdx(String postIdx) {
        this.postIdx = postIdx;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImage_num() {
        return image_num;
    }

    public void setImage_num(String image_num) {
        this.image_num = image_num;
    }
}
