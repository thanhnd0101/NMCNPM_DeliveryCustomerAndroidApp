package com.example.niot.deliveryfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.niot.deliveryfood.model.User;


public class MainActivity extends AppCompatActivity implements BillsViewFragment.HasUserId, AccountSettingFragment.HasUserInfo{
    public static final int CHANGE_AVATAR_CODE = 2525;
    static private User user = null;
    private FrameLayout frame;
    private RestaurantsViewFragment restaurantsViewFragment = new RestaurantsViewFragment();
    private BillsViewFragment billViewFragment = new BillsViewFragment();
    private CurrentBillsViewFragment currentBillsViewFragment = new CurrentBillsViewFragment();
    private AccountSettingFragment accountSettingFragment = new AccountSettingFragment();
    private boolean isBacked = false;
    // More fragment

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(getSupportActionBar() != null)
                        getSupportActionBar().setTitle("Quán ăn");
                    loadFragment(restaurantsViewFragment);
                    return true;
                case R.id.navigation_dashboard:
                    if(getSupportActionBar() != null)
                        getSupportActionBar().setTitle("Tất cả đơn");
                    loadFragment(billViewFragment);
                    return true;
                case R.id.navigation_notifications:
                    if(getSupportActionBar() != null)
                        getSupportActionBar().setTitle("Đơn đang thực hiện");
                    loadFragment(currentBillsViewFragment);
                    return true;
                case R.id.navigation_account:
                    loadFragment(accountSettingFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            if(bundle.get("user") != null)
                user = (User) bundle.get("user");
        frame = findViewById(R.id.fragment_container);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Quán ăn");
        }
        loadFragment(restaurantsViewFragment);
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    // Change Avatar Result

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHANGE_AVATAR_CODE){
            if(resultCode == Activity.RESULT_OK){
                if(data.getExtras() != null)
                    user = (User) data.getExtras().get("user");
                accountSettingFragment.updateActionBar(user);
            }
        }
    }

    @Override
    public int getUserId() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }

    public ActionBar myGetActionBar(){
        return getSupportActionBar();
    }

    @Override
    public void onBackPressed() {
        if (isBacked) {
            super.onBackPressed();
            return;
        }

        this.isBacked = true;
        Toast.makeText(this, "Ấn BACK lần nữa để thoát", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                isBacked=false;
            }
        }, 2000);
    }
}
