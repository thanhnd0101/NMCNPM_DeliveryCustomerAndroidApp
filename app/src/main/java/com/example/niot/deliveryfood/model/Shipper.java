package com.example.niot.deliveryfood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shipper extends Account {
    @SerializedName("cmnd")
    @Expose
    private String cmnd;
    @SerializedName("anh")
    @Expose
    private String image_path;

    public String getAddress() {
        return cmnd;
    }

    public String getImage_path() {
        return image_path;
    }

    @Override
    public String toString() {
        return super.toString() + "\nCMND: " + cmnd;
    }
}
