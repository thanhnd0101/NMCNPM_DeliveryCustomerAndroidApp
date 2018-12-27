package com.example.niot.deliveryfood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// True false
public class PostResponse implements Serializable {
    @SerializedName("status")
    @Expose
    private int status;


    public int getStatus() {
        return status;
    }
}
