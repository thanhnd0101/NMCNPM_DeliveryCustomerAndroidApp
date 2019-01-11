package com.example.niot.deliveryfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niot.deliveryfood.model.User;
import com.example.niot.deliveryfood.retrofit.CvlApi;
import com.example.niot.deliveryfood.retrofit.RetrofitObject;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    String phone_number;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void registerBtnClicked(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        this.startActivity(intent);
    }

    public void loginBtnClicked(View view) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Đang đăng nhập...");
        dialog.setMessage("Vui lòng đợi trong giây lát");
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            }
        }).start();

        Retrofit retrofit = RetrofitObject.getInstance();

        GetUsernamePassword();
        if(isValidUsernamePassword())
            retrofit.create(CvlApi.class)
                .loginUser(phone_number, password)
                .enqueue(new Callback<List<User>>(){
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(dialog.isShowing())
                                    dialog.dismiss();
                            }
                        });
                    }
                }).start();
                String title, msg;

                if(response.body() != null){
                    List<User> users = response.body();

                    if(users.size() > 0){
                        //msg = users.get(0).toString();
                        msg = "Chào mừng " + users.get(0).getName() + ". Bạn đã đăng nhập thành công!";
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("user",users.get(0));
                        startActivity(i);
                        LoginActivity.this.finish();
                    }
                    else{
                        msg = "Sai số điện thoại hoặc mật khẩu!";
                    }
                }
                else
                    msg = "Something is wrong with our server, please try next time!";
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(dialog.isShowing())
                                    dialog.dismiss();
                            }
                        });
                    }
                }).start();
                Toast.makeText(LoginActivity.this, "Request failed! Check your connection and try again.", Toast.LENGTH_SHORT).show();
            }
        });

        // Hide the keyboard if user click button

        try{
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            if(imm != null)
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            Log.e("Login: ", e.getMessage());
        }
    }

    private boolean isValidUsernamePassword() {
        return phone_number.length() > 0 && password.length() > 0;
    }

    private void GetUsernamePassword() {
        TextInputEditText phoneET = findViewById(R.id.login_layout_edit_text_sdt);
        TextInputEditText passET = findViewById(R.id.login_layout_edit_text_password);

        passET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    loginBtnClicked(v);
                    handled = true;
                }
                return handled;
            }
        });

        phone_number = phoneET.getText().toString();
        password = passET.getText().toString();
    }
}
