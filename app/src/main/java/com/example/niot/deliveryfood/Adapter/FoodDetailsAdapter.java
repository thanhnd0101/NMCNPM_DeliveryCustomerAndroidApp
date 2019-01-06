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
import com.example.niot.deliveryfood.model.Food;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class FoodDetailsAdapter extends RecyclerView.Adapter<FoodDetailsAdapter.FoodDetailsHolder> {
    List<Food> foods;
    FoodChangeQuantityOnClickListener listener;

    @NonNull
    @Override
    public FoodDetailsAdapter.FoodDetailsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.food_info_view_layout,viewGroup,false);
        return new FoodDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodDetailsAdapter.FoodDetailsHolder foodDetailsHolder, final int i) {
        foodDetailsHolder.food_name.setText(foods.get(i).getName());
        foodDetailsHolder.food_desc.setText(foods.get(i).getDescription());
        foodDetailsHolder.food_price.setText(String.valueOf(foods.get(i).getPrice()));
        foodDetailsHolder.itemView.setClickable(true);

        foodDetailsHolder.food_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = ++foodDetailsHolder.food_count_int;
                foodDetailsHolder.food_count.setText(String.valueOf(count));
                listener.onClickChangeQuantity(foods.get(i), count);
            }
        });

        foodDetailsHolder.food_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(foodDetailsHolder.food_count_int == 0)
                    return;
                int count = --foodDetailsHolder.food_count_int;
                foodDetailsHolder.food_count.setText(String.valueOf(count));
                listener.onClickChangeQuantity(foods.get(i), count);
            }
        });

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

    public FoodDetailsAdapter(List<Food> foods, FoodChangeQuantityOnClickListener listener){
        this.foods = foods;
        this.listener = listener;
    }

    public class FoodDetailsHolder extends RecyclerView.ViewHolder {
        TextView food_name;
        TextView food_desc;
        TextView food_price;
        ImageView food_add;
        ImageView food_minus;
        TextView food_count;
        ImageView food_img;
        int food_count_int = 0;

        public FoodDetailsHolder(@NonNull View itemView) {
            super(itemView);
            food_name = itemView.findViewById(R.id.food_detail_name);
            food_desc = itemView.findViewById(R.id.food_detail_desc);
            food_img = itemView.findViewById(R.id.food_detail_img);
            food_price = itemView.findViewById(R.id.food_detail_price);
            food_count = itemView.findViewById(R.id.food_count);
            food_add = itemView.findViewById(R.id.food_add);
            food_minus = itemView.findViewById(R.id.food_remove);
        }
    }

    public interface FoodChangeQuantityOnClickListener{
        void onClickChangeQuantity(Food f, int quantity);
    }
}
