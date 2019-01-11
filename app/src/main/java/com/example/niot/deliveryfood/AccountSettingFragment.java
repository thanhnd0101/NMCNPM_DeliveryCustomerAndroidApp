package com.example.niot.deliveryfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niot.deliveryfood.model.User;

import java.io.InputStream;
import java.net.URL;

public class AccountSettingFragment extends Fragment {
    User user;
    View view;
    ActionBar actionBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.account_setting_fragment, container, false);

        Activity activity = getActivity();
        if(activity instanceof HasUserInfo){
            user = ((HasUserInfo) activity).getUser();
            actionBar = ((HasUserInfo) activity).myGetActionBar();
        }

        updateActionBar();

        //if(actionBar != null)
        //    ((TextView)actionBar.getCustomView().findViewById(R.id.account_name)).setText(user.getName());

        settingUpOnClick();
        return view;
    }

    private void settingUpOnClick() {
        TextView userInformationChange = view.findViewById(R.id.account_personal_information);
        userInformationChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = AccountSettingFragment.this.getActivity();
                if(activity == null)
                    return;
                Intent i = new Intent(activity, ChangeUserInformationActivity.class);
                i.putExtra("user", user);
                activity.startActivityForResult(i, 2525);
            }
        });
    }


    public void updateActionBar(User user) {
        if(user != null)
            this.user = user;
        Log.e("AccountSettingFrag:","updating!");
        new DownloadImageTask(actionBar).execute(this.user.getImage_path());

        if(actionBar != null)
           ((TextView)actionBar.getCustomView().findViewById(R.id.account_name)).setText(this.user.getName());
    }

    private void updateActionBar(){
        updateActionBar(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(false);
    }

    public interface HasUserInfo{
        User getUser();
        ActionBar myGetActionBar();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ActionBar actionBar;
        DownloadImageTask(ActionBar actionBar) {
            if(actionBar != null){
                actionBar.setCustomView(R.layout.account_custom_action_bar);

                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setDisplayShowHomeEnabled(false);

                this.actionBar = actionBar;
            }
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
            if(actionBar != null){
                ((ImageView)actionBar.getCustomView().findViewById(R.id.account_avatar)).setImageBitmap(result);
            }
        }
    }
}
