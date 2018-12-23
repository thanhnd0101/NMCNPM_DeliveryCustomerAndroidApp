package com.example.niot.deliveryfood;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class FoodDetailsAdapter extends RecyclerView.Adapter<FoodDetailsAdapter.FoodDetailsHolder> {
    List<Food> foods;

    @NonNull
    @Override
    public FoodDetailsAdapter.FoodDetailsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.food_info_view_layout,viewGroup,false);
        return new FoodDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodDetailsAdapter.FoodDetailsHolder foodDetailsHolder, int i) {
        foodDetailsHolder.food_name.setText(foods.get(i).getName());
        foodDetailsHolder.food_desc.setText(foods.get(i).getDescription());
        foodDetailsHolder.food_price.setText(String.valueOf(foods.get(i).getPrice()));
        new DownloadImageTask(foodDetailsHolder.food_img).execute(foods.get(i).getImg_path());
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

    public FoodDetailsAdapter(List<Food> foods){
        this.foods = foods;
    }

    public class FoodDetailsHolder extends RecyclerView.ViewHolder {
        TextView food_name;
        TextView food_desc;
        TextView food_price;
        ImageView food_img;


        public FoodDetailsHolder(@NonNull View itemView) {
            super(itemView);
            food_name = itemView.findViewById(R.id.food_detail_name);
            food_desc = itemView.findViewById(R.id.food_detail_desc);
            food_img = itemView.findViewById(R.id.food_detail_img);
            food_price = itemView.findViewById(R.id.food_detail_price);
        }
    }
}
