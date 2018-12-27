package com.example.niot.deliveryfood;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.niot.deliveryfood.Adapter.FoodDetailsAdapter;
import com.example.niot.deliveryfood.Adapter.RestaurantsAdapter;
import com.example.niot.deliveryfood.model.BillResponse;
import com.example.niot.deliveryfood.model.Cart;
import com.example.niot.deliveryfood.model.Food;
import com.example.niot.deliveryfood.model.Restaurant;
import com.example.niot.deliveryfood.model.User;
import com.example.niot.deliveryfood.retrofit.CvlApi;
import com.example.niot.deliveryfood.retrofit.RetrofitObject;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RestaurantDetailActivity extends AppCompatActivity implements FoodDetailsAdapter.FoodChangeQuantityOnClickListener {

    RecyclerView foodRecyclerView;
    RecyclerView resRecyclerView;
    List<Food> foods;
    ArrayList<Food> selectedFoods;
    FoodDetailsAdapter adapter;
    Cart cart;
    int res_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        Intent intent = getIntent();
        Restaurant restaurant = (Restaurant)intent.getExtras().get("res");
        User user = (User)intent.getExtras().get("user");

        // Create a temporary cart
        res_id = restaurant.getId();
        cart = new Cart(user, restaurant, null);
        selectedFoods = new ArrayList<>();

        // Get the ViewGroups
        foodRecyclerView = findViewById(R.id.res_detail_foods_recycler_view);
        resRecyclerView = findViewById(R.id.res_detail_res_info);

        // Put restaurant data to the view
        ArrayList<Restaurant> temp = new ArrayList<>();
        temp.add(restaurant);
        RestaurantsAdapter resAdapter = new RestaurantsAdapter(temp, null);

        // Adapter and recycler view
        resRecyclerView.setAdapter(resAdapter);
        foods = new ArrayList<>();
        adapter = new FoodDetailsAdapter(foods, this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK)
                this.finish();
        }
    }

    public void fabOnClick(View view) {
        Log.e("RES DETAIL LOG:", cart.toString());
        if(cart.getDetail().size() == 0){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("There's nothing in your cart!").setMessage("Bạn phải chọn món chứ!").setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();

            return;
        }

        Intent i = new Intent(this, CartPreviewActivity.class);
        String json = new Gson().toJson(cart);
        i.putExtra("cartJson", json);
        startActivityForResult(i, 1);
    }


    @Override
    public void onClickChangeQuantity(Food f, int quantity) {
        cart.addDetail(f, quantity);
    }
}
