package com.example.niot.deliveryfood;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.niot.deliveryfood.Adapter.FoodDetailsAdapter;
import com.example.niot.deliveryfood.model.Food;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailFoodListFragment extends Fragment {
    RecyclerView recyclerView;
    List<Food> foods;
    FoodDetailsAdapter adapter;
    View thisView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.food_list_recycle_view, container, false);
        if(this.getContext() != null){
            Context context = getContext();
            if (context instanceof RestaurantDetailActivity) {
                RestaurantDetailActivity activity = (RestaurantDetailActivity) context;
                recyclerView = thisView.findViewById(R.id.res_detail_foods_recycler_view);
                foods = new ArrayList<>();
                adapter = new FoodDetailsAdapter(foods, activity);
                recyclerView.setAdapter(adapter);
                activity.setUpRecyclerViewAndAdapter(foods, adapter, recyclerView);
            }
        }
        return thisView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
