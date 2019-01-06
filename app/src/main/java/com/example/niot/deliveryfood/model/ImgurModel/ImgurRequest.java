package com.example.niot.deliveryfood.model.ImgurModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImgurRequest implements Serializable {
    @SerializedName("image")
    String data;

    public ImgurRequest(String data){
        this.data = data;
    }
}
