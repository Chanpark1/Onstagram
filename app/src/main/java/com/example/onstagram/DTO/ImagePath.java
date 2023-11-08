package com.example.onstagram.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImagePath {

    public ImagePath(String image) {
        this.image = image;
    }

    @Expose
    @SerializedName("image") private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
