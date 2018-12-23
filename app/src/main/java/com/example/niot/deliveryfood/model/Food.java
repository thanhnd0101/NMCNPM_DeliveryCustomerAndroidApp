package com.example.niot.deliveryfood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Food {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("id_quan_an")
    @Expose
    private int id_quan;
    @SerializedName("ten")
    @Expose
    private String name;
    @SerializedName("gia")
    @Expose
    private int price;
    @SerializedName("mota")
    @Expose
    private String description;
    @SerializedName("anh")
    @Expose
    private String img_path;

    public int getId() {
        return id;
    }

    public int getId_quan() {
        return id_quan;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImg_path() {
        return img_path;
    }
}
