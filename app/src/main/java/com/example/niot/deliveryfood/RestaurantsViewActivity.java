package com.example.niot.deliveryfood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.niot.deliveryfood.retrofit.CvlApi;
import com.example.niot.deliveryfood.retrofit.RetrofitObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RestaurantsViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Restaurant> restaurants;
    RestaurantsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_view);
        recyclerView = findViewById(R.id.recycle_view_list_restaurant);
        restaurants = new ArrayList<>();
        adapter = new RestaurantsAdapter(restaurants);
        recyclerView.setAdapter(adapter);
        getRestaurants();
    }

    private void getRestaurants() {
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).getRestaurant().enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                List<Restaurant> restaurants = response.body();
                if(restaurants.size() > 0){
                    RestaurantsViewActivity.this.restaurants.clear();
                    RestaurantsViewActivity.this.restaurants.addAll(restaurants);
                    adapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(RestaurantsViewActivity.this, "Failed2", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Toast.makeText(RestaurantsViewActivity.this, "Failed3!", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(RestaurantsViewActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    //Toast.makeText(SignUpActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RestaurantsViewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
