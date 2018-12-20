package com.example.niot.deliveryfood;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Account implements Serializable {
    @SerializedName("id_nd")
    @Expose
    private int id;
    @SerializedName("ten_nd")
    @Expose
    private String name;
    @SerializedName("email_nd")
    @Expose
    private String email;
    @SerializedName("sdt_nd")
    @Expose
    private String phone;
    @SerializedName("password_nd")
    @Expose
    private String password;

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        String ans = "ID = " + String.valueOf(id) + "\nName = " + name + "\nEmail = "
                + email + "\nPhone = " + phone;
        return ans;
    }
}
