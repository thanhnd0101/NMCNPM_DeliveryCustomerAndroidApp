package com.example.niot.deliveryfood.Adapter;

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

import com.example.niot.deliveryfood.R;
import com.example.niot.deliveryfood.model.Comment;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class CommentViewAdapter extends RecyclerView.Adapter<CommentViewAdapter.CommentViewHolder> {
    private List<Comment> comments;

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_view, viewGroup, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder viewHolder, final int i) {
        viewHolder.detail.setText(comments.get(i).getDetail());
        viewHolder.userName.setText(comments.get(i).getUserName());
        viewHolder.title.setText(comments.get(i).getTitle());

        new DownloadImageTask(viewHolder.avatar).execute(comments.get(i).getImg());
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        DownloadImageTask(ImageView bmImage) {
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
        return comments.size();
    }

    public CommentViewAdapter(List<Comment> comments){
        this.comments = comments;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView title;
        TextView userName;
        TextView detail;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.comment_user_avatar);
            title = itemView.findViewById(R.id.comment_title);
            userName = itemView.findViewById(R.id.comment_user_name);
            detail = itemView.findViewById(R.id.comment_detail);
        }
    }
}
