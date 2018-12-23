package com.example.niot.deliveryfood.retrofit;

import com.example.niot.deliveryfood.Food;
import com.example.niot.deliveryfood.Restaurant;
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
    @GET("/login/nguoi_dung")
    Call<List<User>> loginUser(@Query("sdt") String phone, @Query("pass") String password);

    @GET("/signup/nguoi_dung")
    Call<List<User>> newUser(@QueryMap Map<String, String> info);

    @GET("/db/quan_an")
    Call<List<Restaurant>> getRestaurant();

    @GET("/mon_an")
    Call<List<Food>> getFoodsList(@Query("id_quan_an") int res_id);
}
