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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantsHolder> {
    List<Restaurant> restaurants;

    @NonNull
    @Override
    public RestaurantsAdapter.RestaurantsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.restaurant_full_info_view_layout,viewGroup,false);
        return new RestaurantsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsAdapter.RestaurantsHolder restaurantsHolder, int i) {
        restaurantsHolder.res_name.setText(restaurants.get(i).getName());
        restaurantsHolder.res_addr.setText(restaurants.get(i).getAddress());
        new DownloadImageTask(restaurantsHolder.res_img).execute(restaurants.get(i).getImage_path());
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
                InputStream in = new java.net.URL(urldisplay).openStream();
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
        return restaurants.size();
    }

    public RestaurantsAdapter(List<Restaurant> restaurants){
        this.restaurants = restaurants;
    }

    public class RestaurantsHolder extends RecyclerView.ViewHolder {
        TextView res_name;
        TextView res_addr;
        ImageView res_img;

        public RestaurantsHolder(@NonNull View itemView) {
            super(itemView);
            res_name = itemView.findViewById(R.id.res_view_res_name);
            res_addr = itemView.findViewById(R.id.res_view_res_description);
            res_img = itemView.findViewById(R.id.res_view_res_img);
        }
    }
}
