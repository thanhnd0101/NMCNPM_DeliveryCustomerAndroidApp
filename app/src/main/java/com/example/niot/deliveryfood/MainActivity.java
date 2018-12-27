package com.example.niot.deliveryfood;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.niot.deliveryfood.Adapter.BillViewAdapter;
import com.example.niot.deliveryfood.model.Bill;
import com.example.niot.deliveryfood.model.User;

public class MainActivity extends AppCompatActivity implements BillsViewFragment.HasUserId {
    static private User user = null;
    private FrameLayout frame;
    private RestaurantsViewFragment restaurantsViewFragment = new RestaurantsViewFragment();
    private BillsViewFragment billViewFragment = new BillsViewFragment();
    // More fragment

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(restaurantsViewFragment);
                    return true;
                case R.id.navigation_dashboard:
                    loadFragment(billViewFragment);
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(user == null)
            user = (User) getIntent().getExtras().get("user");
        frame = findViewById(R.id.fragment_container);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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

    @Override
    public int getUserId() {
        return user.getId();
    }

    public static User getUser() {
        return user;
    }
}
