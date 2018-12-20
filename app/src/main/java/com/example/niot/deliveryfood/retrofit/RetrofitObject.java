package com.example.niot.deliveryfood.retrofit;

import com.example.niot.deliveryfood.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitObject {
    private static String BASE_URL = "https://fast-atoll-52540.herokuapp.com";
    private static Gson gson = new GsonBuilder().create();
    private static Retrofit retrofit;

    private RetrofitObject(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
    }

    public static Retrofit getInstance(){
        if(retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();
        return retrofit;
    }
}
