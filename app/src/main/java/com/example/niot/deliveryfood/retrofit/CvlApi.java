package com.example.niot.deliveryfood.retrofit;

import com.example.niot.deliveryfood.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface CvlApi {
    @GET("/login/nd")
    Call<List<User>> loginUser(@Query("phone") String phone, @Query("pass") String password);

    @GET("/signup/nd")
    Call<List<User>> newUser(@QueryMap Map<String, String> info);
}
