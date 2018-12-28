package com.example.niot.deliveryfood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comment implements Serializable {
    @SerializedName("ten")
    @Expose
    private String user_name;

    @SerializedName("tieu_de")
    @Expose
    private String title;

    @SerializedName("noi_dung")
    @Expose
    private String detail;

    @SerializedName("anh")
    @Expose
    private String img;

    @SerializedName("id_nguoi_dung")
    @Expose
    private int userId;

    @SerializedName("id_quan_an")
    @Expose
    private int resId;

    public Comment(int user, int res, String title, String detail){
        userId = user;
        resId = res;
        this.title = title;
        this.detail = detail;
    }

    public String getUserName() {
        return user_name;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getImg() {
        return img;
    }
}
