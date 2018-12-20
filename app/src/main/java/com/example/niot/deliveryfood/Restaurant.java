package com.example.niot.deliveryfood;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Restaurant extends Account {
    @SerializedName("dchi")
    @Expose
    private String address;
    @SerializedName("anh")
    @Expose
    private String image_path;

    public String getAddress() {
        return address;
    }

    public String getImage_path() {
        return image_path;
    }

    @Override
    public String toString() {
        return super.toString() + "\nAddress: " + address;
    }
}
