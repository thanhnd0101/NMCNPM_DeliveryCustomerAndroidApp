package com.example.niot.deliveryfood.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.niot.deliveryfood.R;
import com.example.niot.deliveryfood.model.BillDetail;

import java.util.List;

// This adapter is to view the bill detail
public class FoodOnlyTextAdapter extends RecyclerView.Adapter<FoodOnlyTextAdapter.FoodTextViewHolder> {
    private List<BillDetail> details;

    @NonNull
    @Override
    public FoodTextViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.food_simple_text_view, viewGroup, false);
        return new FoodTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodTextViewHolder viewHolder, final int i) {
        BillDetail d = details.get(i);
        viewHolder.food_price.setText(String.valueOf(d.getGia()));
        String count = "SL: " + String.valueOf(d.getSoluong());
        viewHolder.food_count.setText(count);
        viewHolder.food_name.setText(d.getFood_name());
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public FoodOnlyTextAdapter(List<BillDetail> billDetails){
        details = billDetails;
    }

    public class FoodTextViewHolder extends RecyclerView.ViewHolder {
        TextView food_name;
        TextView food_count;
        TextView food_price;

        public FoodTextViewHolder(@NonNull View itemView) {
            super(itemView);
            food_name = itemView.findViewById(R.id.food_simple_name);
            food_count = itemView.findViewById(R.id.food_simple_count);
            food_price = itemView.findViewById(R.id.food_simple_price);
        }
    }
}
