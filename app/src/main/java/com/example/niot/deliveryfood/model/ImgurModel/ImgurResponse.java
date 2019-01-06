package com.example.niot.deliveryfood.model.ImgurModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImgurResponse implements Serializable {
    @SerializedName("data")
    Data data;

    @SerializedName("success")
    boolean success;

    @SerializedName("status")
    int status;

    public Data getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getStatus() {
        return status;
    }
}
