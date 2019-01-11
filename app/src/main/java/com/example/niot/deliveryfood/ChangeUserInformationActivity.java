package com.example.niot.deliveryfood;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niot.deliveryfood.model.ImgurModel.ImgurRequest;
import com.example.niot.deliveryfood.model.ImgurModel.ImgurResponse;
import com.example.niot.deliveryfood.model.User;
import com.example.niot.deliveryfood.retrofit.CvlApi;
import com.example.niot.deliveryfood.retrofit.RetrofitObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangeUserInformationActivity extends AppCompatActivity {
    final int PICK_IMAGE = 5012;
    Bitmap avatar = null;
    User user;

    boolean isAvatarChanged;
    boolean needToLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_information);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Thông tin cá nhân");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle b = getIntent().getExtras();
        if(b != null)
            user = (User) b.get("user");
        else
            finish();

        displayUserInfo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

    private void displayUserInfo() {
        final EditText id = findViewById(R.id.account_info_id);
        final EditText name = findViewById(R.id.account_info_name);
        final EditText email = findViewById(R.id.account_info_email);
        final EditText phone = findViewById(R.id.account_info_phone);
        final EditText address = findViewById(R.id.account_info_address);
        final ImageView avatarView = findViewById(R.id.account_info_avatar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        id.setText(String.valueOf(user.getId()));
                        name.setText(user.getName());
                        email.setText(user.getEmail());
                        phone.setText(user.getPhone());
                        address.setText(user.getAddress());
                        if(avatar != null)
                            avatarView.setImageBitmap(avatar);
                        else
                            new DownloadImageTask(avatarView).execute(user.getImage_path());
                    }
                });
            }
        }).start();
    }

    public void ChangeAvatar(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE){
            if(resultCode == Activity.RESULT_OK){
                if(data == null || data.getData() == null)
                    return;
                try {
                    InputStream is = ChangeUserInformationActivity.this.getContentResolver().openInputStream(data.getData());
                    avatar = BitmapFactory.decodeStream(is);
                    avatar = Bitmap.createScaledBitmap(avatar, 150, 150, false);
                    avatar = bitmapCircleClipping(avatar);

                    final ImageView avatarView = findViewById(R.id.account_info_avatar);
                    avatarView.post(new Runnable() {
                        @Override
                        public void run() {
                            needToLoad = false;
                            avatarView.setImageBitmap(avatar);
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("ChangeUserInfoActivity", e.getMessage());
                }
            }
        }
    }

    public void UploadImage(View view) {
        if(avatar == null){
            UpdateUserAvatar(user.getImage_path());
            return;
        }
        Retrofit retrofit = RetrofitObject.getInstance();
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), bitmapToBase64(avatar));
        retrofit.create(CvlApi.class).imgurUpload(body).enqueue(new Callback<ImgurResponse>() {
            @Override
            public void onResponse(Call<ImgurResponse> call, Response<ImgurResponse> response) {
                if(response.body() != null){
                    ImgurResponse response1 = response.body();
                    if(response1.isSuccess()){
                        Log.e("UserInfo: ", "Uploaded Successfully");
                        UpdateUserAvatar(response1.getData().getLink());
                    }
                    else
                        Log.e("UserInfo: ", "Uploaded Failed");
                }
            }

            @Override
            public void onFailure(Call<ImgurResponse> call, Throwable t) {
                if (t instanceof IOException) {
                    Log.e("UserInfo: ", "Network error");
                }
                else {
                    Log.e("UserInfo: ", "Conversion error");
                    Log.e("UserInfo: ", t.getMessage());
                }
            }
        });
    }

    private void UpdateUserAvatar(String link) {
        getNewInfo();
        user.setImage_path(link);
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).updateUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    Toast.makeText(ChangeUserInformationActivity.this, "Lưu thành công!!", Toast.LENGTH_SHORT).show();
                    user = response.body();
                    displayUserInfo();
                    isAvatarChanged = true;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ChangeUserInformationActivity.this, "Lưu không thành công!!", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Log.e("UserInfo: ", "Network error");
                } else {
                    Log.e("UserInfo: ", "Conversion error");
                    Log.e("UserInfo: ", t.getMessage());
                }
            }
        });
    }

    private void getNewInfo() {
        String name = ((EditText)findViewById(R.id.account_info_name)).getText().toString();
        String addr = ((EditText)findViewById(R.id.account_info_address)).getText().toString();
        String email = ((EditText)findViewById(R.id.account_info_email)).getText().toString();

        user.setAddress(addr);
        user.setName(name);
        user.setEmail(email);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap bitmapCircleClipping(Bitmap bitmap){
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    @Override
    public void onBackPressed() {
        if(isAvatarChanged){
            Intent i = new Intent();
            i.putExtra("user", user);
            setResult(Activity.RESULT_OK, i);
        }
        else
            setResult(Activity.RESULT_CANCELED);

        finish();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        DownloadImageTask(ImageView bmImage) {
            if(bmImage != null && needToLoad)
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
}
