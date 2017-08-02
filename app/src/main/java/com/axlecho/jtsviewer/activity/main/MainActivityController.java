package com.axlecho.jtsviewer.activity.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.user.JtsParseUserInfoAction;
import com.axlecho.jtsviewer.activity.cache.CacheActivity;
import com.axlecho.jtsviewer.module.UserModule;
import com.axlecho.jtsviewer.network.JtsConf;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.squareup.picasso.Picasso;

public class MainActivityController {
    private static final String TAG = MainActivityController.class.getSimpleName();
    private static MainActivityController instance;
    private MainActivity activity;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    public static MainActivityController getInstance() {
        if (instance == null) {
            synchronized (MainActivity.class) {
                if (instance == null) {
                    instance = new MainActivityController();
                }
            }
        }
        return instance;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public MainActivity getActivity() {
        return this.activity;
    }

    public void enableFloatingActionButton(JtsBaseAction action) {
        activity.floatingActionButton.setVisibility(View.VISIBLE);
        activity.floatingActionButton.setOnClickListener(action);
    }

    public void disableFloatingActionButton() {
        activity.floatingActionButton.setVisibility(View.GONE);
        activity.floatingActionButton.setOnClickListener(null);
    }

    public void processProgressChanged(int progress) {
        if (progress == 100) {
            activity.progressBar.setVisibility(View.INVISIBLE);
        } else {
            if (View.INVISIBLE == activity.progressBar.getVisibility()) {
                activity.progressBar.setVisibility(View.VISIBLE);
            }
            activity.progressBar.setProgress(progress);
        }
    }

    public void processJumpCache() {
        Intent intent = new Intent();
        intent.setClass(activity, CacheActivity.class);
        activity.startActivity(intent);
    }

    public void verifyStoragePermissions() {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadUserInfo() {
        JtsParseUserInfoAction action = new JtsParseUserInfoAction();
        JtsNetworkManager.getInstance(activity).get(JtsConf.HOST_URL, action);
    }

    public void processLoadUserInfo(final UserModule user) {
        JtsViewerLog.d(TAG, user.toString());
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View headerView = activity.navigationView.getHeaderView(0);
                ImageView drawerUserInfoImageView = (ImageView) headerView.findViewById(R.id.nav_user_imageView);
                TextView userNameTextView = (TextView) headerView.findViewById(R.id.nav_user_name);

                if (user.avatarUrl != null) {
                    Picasso.with(activity).load(user.avatarUrl).into(drawerUserInfoImageView);
                }

                if (user.userName != null) {
                    userNameTextView.setText(user.userName);
                }
            }
        });

    }
}
