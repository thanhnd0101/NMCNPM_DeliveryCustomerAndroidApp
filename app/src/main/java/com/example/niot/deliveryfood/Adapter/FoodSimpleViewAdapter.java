package com.example.niot.deliveryfood.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niot.deliveryfood.R;
import com.example.niot.deliveryfood.model.Cart;
import com.example.niot.deliveryfood.model.Food;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FoodSimpleViewAdapter extends RecyclerView.Adapter<FoodSimpleViewAdapter.FoodHolder> {
    Cart cart;
    List<Food> foods;
    int[] quantity;

    @NonNull
    @Override
    public FoodSimpleViewAdapter.FoodHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.food_info_view_layout,viewGroup,false);
        return new FoodSimpleViewAdapter.FoodHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodSimpleViewAdapter.FoodHolder foodHolder, final int i) {
        foodHolder.food_name.setText(foods.get(i).getName());
        foodHolder.food_price.setText(String.valueOf(foods.get(i).getPrice()));
        foodHolder.food_count.setText(String.valueOf(quantity[i]));

        new FoodSimpleViewAdapter.DownloadImageTask(foodHolder.food_img).execute(foods.get(i).getImg_path());

        foodHolder.food_minus.setVisibility(View.INVISIBLE);
        foodHolder.food_add.setVisibility(View.INVISIBLE);
        foodHolder.food_desc.setVisibility(View.INVISIBLE);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            if(bmImage != null)
                this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public FoodSimpleViewAdapter(Cart cart){
        this.cart = cart;
        List<Cart.Detail> detail = cart.getDetail();
        foods = new ArrayList<>();
        quantity = new int[detail.size()];

        int i = 0;
        for(Cart.Detail d : cart.getDetail()){
            foods.add(d.getFood());
            quantity[i++] = d.getQuantity();
        }
    }

    public class FoodHolder extends RecyclerView.ViewHolder {
        TextView food_name;
        TextView food_desc;
        TextView food_price;
        ImageView food_add;
        ImageView food_minus;
        TextView food_count;
        ImageView food_img;

        public FoodHolder(@NonNull View itemView) {
            super(itemView);
            food_name = itemView.findViewById(R.id.food_detail_name);
            food_img = itemView.findViewById(R.id.food_detail_img);
            food_price = itemView.findViewById(R.id.food_detail_price);
            food_count = itemView.findViewById(R.id.food_count);
            food_desc = itemView.findViewById(R.id.food_detail_desc);
            food_add = itemView.findViewById(R.id.food_add);
            food_minus = itemView.findViewById(R.id.food_remove);
        }
    }
}
