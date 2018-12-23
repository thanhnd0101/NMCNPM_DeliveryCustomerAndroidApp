package com.example.niot.deliveryfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.niot.deliveryfood.Adapter.FoodDetailsAdapter;
import com.example.niot.deliveryfood.Adapter.RestaurantsAdapter;
import com.example.niot.deliveryfood.model.Food;
import com.example.niot.deliveryfood.model.Restaurant;
import com.example.niot.deliveryfood.retrofit.CvlApi;
import com.example.niot.deliveryfood.retrofit.RetrofitObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RestaurantDetailActivity extends AppCompatActivity {

    RecyclerView foodRecyclerView;
    RecyclerView resRecyclerView;
    List<Food> foods;
    FoodDetailsAdapter adapter;
    int res_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        Intent intent = getIntent();
        Restaurant restaurant = (Restaurant)intent.getExtras().get("res");
        res_id = restaurant.getId();

        foodRecyclerView = findViewById(R.id.res_detail_foods_recycler_view);
        resRecyclerView = findViewById(R.id.res_detail_res_info);

        // Put restaurant data to the view
        ArrayList<Restaurant> temp = new ArrayList<>();
        temp.add(restaurant);
        RestaurantsAdapter resAdapter = new RestaurantsAdapter(temp, null);

        // Adapter and recycler view
        resRecyclerView.setAdapter(resAdapter);
        foods = new ArrayList<>();
        adapter = new FoodDetailsAdapter(foods);
        foodRecyclerView.setAdapter(adapter);
        getFoods();
    }

    private void getFoods() {
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).getFoodsList(res_id).enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                List<Food> foods = response.body();
                if(foods.size() > 0){
                    RestaurantDetailActivity.this.foods.clear();
                    RestaurantDetailActivity.this.foods.addAll(foods);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Toast.makeText(RestaurantDetailActivity.this, "Failed3!", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(RestaurantDetailActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    //Toast.makeText(SignUpActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RestaurantDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
