package com.example.niot.deliveryfood.model.ImgurModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {
    @SerializedName("id")
    String id;

    @SerializedName("title")
    String title;

    @SerializedName("name")
    String name;

    @SerializedName("link")
    String link;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}
