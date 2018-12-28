package com.example.niot.deliveryfood.Adapter;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import java.util.List;

public class RestaurantDetailTabAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragments;
    View button;

    public RestaurantDetailTabAdapter(FragmentManager fm, List<Fragment> fragments, View button){
        super(fm);
        this.button = button;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        switch (position){
            case 0:
                title = "Món ăn";
                break;
            case 1:
                title = "Đánh giá";
                break;
            default:
                title= "Sure là không có :D";
                break;
        }
        return title;
    }
}
