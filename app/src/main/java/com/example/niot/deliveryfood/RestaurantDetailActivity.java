package com.example.niot.deliveryfood;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niot.deliveryfood.Adapter.FoodDetailsAdapter;
import com.example.niot.deliveryfood.Adapter.RestaurantDetailTabAdapter;
import com.example.niot.deliveryfood.Adapter.RestaurantsAdapter;
import com.example.niot.deliveryfood.model.BillResponse;
import com.example.niot.deliveryfood.model.Cart;
import com.example.niot.deliveryfood.model.Food;
import com.example.niot.deliveryfood.model.PostResponse;
import com.example.niot.deliveryfood.model.Restaurant;
import com.example.niot.deliveryfood.model.User;
import com.example.niot.deliveryfood.retrofit.CvlApi;
import com.example.niot.deliveryfood.retrofit.RetrofitObject;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RestaurantDetailActivity extends AppCompatActivity implements FoodDetailsAdapter.FoodChangeQuantityOnClickListener, RestaurantDetailCommentFragment.hasARestaurantAndUser {

    RecyclerView foodRecyclerView;
    List<Food> foods;
    ArrayList<Food> selectedFoods;
    FoodDetailsAdapter adapter;
    Restaurant restaurant;
    RestaurantDetailFoodListFragment foodListFragment;
    RestaurantDetailCommentFragment commentFragment;
    Cart cart;
    User user;
    boolean fav_status;
    boolean commentEditShow = false;
    int res_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        Intent intent = getIntent();
        restaurant = (Restaurant)intent.getExtras().get("res");
        user = (User)intent.getExtras().get("user");

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        foodListFragment = new RestaurantDetailFoodListFragment();
        commentFragment = new RestaurantDetailCommentFragment();
        // Create a temporary cart
        res_id = restaurant.getId();
        cart = new Cart(user, restaurant, null);
        selectedFoods = new ArrayList<>();

        // Set up Tab and Pager
        setUpPager();

        // Put restaurant data to the view
        setUpRestaurantInfo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

    private void setUpPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(foodListFragment);
        fragments.add(commentFragment);
        ViewPager pager = findViewById(R.id.res_detail_viewpager);
        TabLayout tabLayout = findViewById(R.id.res_detail_tab_layout);
        FragmentManager fm = getSupportFragmentManager();
        RestaurantDetailTabAdapter adapter = new RestaurantDetailTabAdapter(fm, fragments, findViewById(R.id.res_detail_fab));
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
    }

    public void setUpRecyclerViewAndAdapter(List<Food> foods, FoodDetailsAdapter adapter, RecyclerView recyclerView){
        this.foods = foods;
        this.adapter = adapter;
        this.foodRecyclerView = recyclerView;
        getFoods();
    }

    private void setUpRestaurantInfo() {
        ImageView img = findViewById(R.id.res_detail_res_img);
        TextView desc = findViewById(R.id.res_detail_res_description);
        TextView name = findViewById(R.id.res_detail_res_name);
        final ImageView fav = findViewById(R.id.res_detail_fav_btn);

        new DownloadImageTask(img).execute(restaurant.getImage_path());
        name.setText(restaurant.getName());

        // Get the favorite status of this restaurant
        getFavStatus();
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFavStatus();
                fav_status = !fav_status;
                if(fav_status)
                    fav.setImageResource(R.drawable.baseline_favorite_black_36dp);
                else
                    fav.setImageResource(R.drawable.baseline_favorite_border_black_36dp);
            }
        });
    }

    private void getFavStatus() {
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).checkFav(user.getId(), restaurant.getId()).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.body() != null){
                    // Status = 0 nghĩa là có thích
                    int status = 1 - response.body().getStatus();
                    RestaurantDetailActivity.this.updateFavIcon(status);
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(RestaurantDetailActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!commentEditShow)
            return super.dispatchTouchEvent(ev);
        LinearLayout linearLayout = findViewById(R.id.send_comment_layout);
        Rect hit = new Rect();
        linearLayout.getGlobalVisibleRect(hit);
        if(hit.contains((int)ev.getX(), (int)ev.getY()))
            return super.dispatchTouchEvent(ev);
        TextInputLayout title = findViewById(R.id.comment_title_layout);
        TextInputLayout detail = findViewById(R.id.comment_detail_layout);

        if(commentEditShow){
            hideCommentEdit(title, detail);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void changeFavStatus() {
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).changeFav(user.getId(), restaurant.getId()).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                Log.e("FAV:","Changed!");
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(RestaurantDetailActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFavIcon(int status) {
        ImageView fav = findViewById(R.id.res_detail_fav_btn);
        if(status == 1){
            fav.setImageResource(R.drawable.baseline_favorite_black_36dp);
            fav_status = true;
        }
        else{
            fav.setImageResource(R.drawable.baseline_favorite_border_black_36dp);
            fav_status = false;
        }
    }

    public void sendComment(View view) {
        final TextInputLayout title = findViewById(R.id.comment_title_layout);
        final TextInputLayout detail = findViewById(R.id.comment_detail_layout);

        if(commentEditShow){
            commentFragment.sendComment();
            hideCommentEdit(title, detail);
        }
        else{
            showCommentEdit(title, detail);
        }
    }

    private void showCommentEdit(TextInputLayout title, TextInputLayout detail) {
        commentEditShow = true;

        // Prepare the View for the animation
        title.setVisibility(View.VISIBLE);
        title.setAlpha(0.0f);

        // Start the animation
        title.animate()
                .translationY(0)
                .alpha(1.0f)
                .setListener(null);

        // Prepare the View for the animation
        detail.setVisibility(View.VISIBLE);
        detail.setAlpha(0.0f);

        // Start the animation
        detail.animate()
                .translationY(0)
                .alpha(1.0f)
                .setListener(null);
    }

    private void hideCommentEdit(final TextInputLayout title, final TextInputLayout detail) {
        commentEditShow = false;

        title.animate()
                .translationY(title.getHeight())
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        title.setVisibility(View.GONE);
                    }
                });

        detail.animate()
                .translationY(title.getHeight())
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        detail.setVisibility(View.GONE);
                    }
                });
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
            builder.setTitle("Chưa chọn món mà!").setMessage("Chọn ít nhất một món để đặt.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
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

    @Override
    public Restaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public User getUser(){
        return user;
    }
}