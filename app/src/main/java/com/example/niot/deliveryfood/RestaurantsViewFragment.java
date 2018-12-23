package com.example.niot.deliveryfood;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.niot.deliveryfood.Adapter.RestaurantsAdapter;
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

public class RestaurantsViewFragment extends Fragment implements RestaurantsAdapter.RestaurantViewOnClickListener {
    RecyclerView recyclerView;
    List<Restaurant> restaurants;
    RestaurantsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_restaurants_view, container, false);
        recyclerView = view.findViewById(R.id.recycle_view_list_restaurant);
        restaurants = new ArrayList<>();
        adapter = new RestaurantsAdapter(restaurants, this);
        recyclerView.setAdapter(adapter);
        getRestaurants();
        return view;
    }

    @Override
    public void onClickRestaurantView(Restaurant r) {
        // Create intent to call the detail activity
        Intent i = new Intent(this.getActivity(), RestaurantDetailActivity.class);
        i.putExtra("res", r);
        startActivity(i);
    }

    private void getRestaurants() {
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).getRestaurant().enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                List<Restaurant> restaurants = response.body();
                if(restaurants.size() > 0){
                    RestaurantsViewFragment.this.restaurants.clear();
                    RestaurantsViewFragment.this.restaurants.addAll(restaurants);
                    adapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(RestaurantsViewFragment.this.getActivity(), "Failed2", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Toast.makeText(RestaurantsViewFragment.this.getActivity(), "Failed3!", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(RestaurantsViewFragment.this.getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    //Toast.makeText(SignUpActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RestaurantsViewFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
