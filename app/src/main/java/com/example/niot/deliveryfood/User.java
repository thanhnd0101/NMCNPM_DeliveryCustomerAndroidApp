package com.example.niot.deliveryfood;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User extends Account implements Serializable {
    @SerializedName("dchi_nd")
    @Expose
    private String address;
    @SerializedName("image_nd")
    @Expose
    private String image_path;
    @SerializedName("is_login_nd")
    @Expose
    private boolean isLogin;

    public String getAddress() {
        return address;
    }

    public String getImage_path() {
        return image_path;
    }

    public boolean getIsLogin() {
        return isLogin;
    }

    @Override
    public String toString() {
        return super.toString() + "\nAddress: " + address + "\nisLogin: " + isLogin;
    }
}
