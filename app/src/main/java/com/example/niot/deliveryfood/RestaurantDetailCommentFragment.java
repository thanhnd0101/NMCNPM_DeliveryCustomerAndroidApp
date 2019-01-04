package com.example.niot.deliveryfood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.niot.deliveryfood.Adapter.CommentViewAdapter;
import com.example.niot.deliveryfood.model.Comment;
import com.example.niot.deliveryfood.model.PostResponse;
import com.example.niot.deliveryfood.model.Restaurant;
import com.example.niot.deliveryfood.model.User;
import com.example.niot.deliveryfood.retrofit.CvlApi;
import com.example.niot.deliveryfood.retrofit.RetrofitObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RestaurantDetailCommentFragment extends Fragment {
    Restaurant restaurant;
    User user;
    RecyclerView recyclerView;
    CommentViewAdapter adapter;
    List<Comment> comments;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(RestaurantDetailCommentFragment.this.getContext() != null){
            Context context = RestaurantDetailCommentFragment.this.getContext();
            // Get the restaurant and user id => send and receive corresponding comments
            if(context instanceof hasARestaurantAndUser){
                RestaurantDetailCommentFragment.this.restaurant = ((hasARestaurantAndUser) context).getRestaurant();
                RestaurantDetailCommentFragment.this.user = ((hasARestaurantAndUser) context).getUser();
            }
        }
        // Get the reference of all the views in the fragment (after being created)
        view = inflater.inflate(R.layout.comment_list_fragment, container, false);

        recyclerView = view.findViewById(R.id.comment_list_recycler_view);
        comments = new ArrayList<>();
        adapter = new CommentViewAdapter(comments);
        recyclerView.setAdapter(adapter);

        getComments();

        return view;
    }

    private void getComments() {
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).getComments(restaurant.getId()).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.body() != null){
                    comments.clear();
                    comments.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                else
                    Log.e("COMMENT:", "Response body is null");
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                    Log.e("COMMENT:", t.getMessage());
            }
        });
    }

    public void sendComment(){
        String title = ((EditText)view.findViewById(R.id.comment_add_comment_title)).getText().toString();
        String detail = ((EditText)view.findViewById(R.id.comment_add_comment_detail)).getText().toString();
        Comment comment = new Comment(user.getId(), restaurant.getId(), title, detail);

        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).sendComment(comment).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.body() != null){
                    // Status != 0 mean there's an error
                    if(response.body().getStatus() != 0)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantDetailCommentFragment.this.getContext());
                        builder.setTitle("Lỗi").setMessage("Bạn không thể đánh giá tiếp").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                    // Got it! Sent comment successfully
                    else
                        getComments();
                }
                else
                    Log.e("COMMENTS:","Null response after sent");
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.e("COMMENTS:", t.getMessage());
            }
        });
    }

    // Interface needs to be implemented by the activity holding this fragment
    public interface hasARestaurantAndUser{
        Restaurant getRestaurant();
        User getUser();
    }
}
