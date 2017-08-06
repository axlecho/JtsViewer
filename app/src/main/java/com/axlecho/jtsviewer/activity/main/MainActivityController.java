package com.axlecho.jtsviewer.activity.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.network.JtsSearchAction;
import com.axlecho.jtsviewer.action.tab.JtsShowGtpTabAction;
import com.axlecho.jtsviewer.action.user.JtsLoginAction;
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

                if (user.uid <= 0) {
                    drawerUserInfoImageView.setOnClickListener(new JtsLoginAction());
                }
            }
        });

    }

    public void processLogin() {
        activity.webView.loadUrl(JtsConf.LOGIN_URL);
        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void processLoadHome() {
        activity.webView.loadUrl(JtsConf.HOST_URL);
    }

    public void processShowLogin() {
        Snackbar.make(activity.webView, activity.getResources().getString(R.string.unlogin_tip_long), Snackbar.LENGTH_LONG)
                .setAction(activity.getResources().getString(R.string.login), new JtsLoginAction()).show();
    }

    public void processSearchView() {
        if (activity.searchView == null) {
            return;
        }

        activity.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!activity.searchView.isIconified()) {
                    activity.searchView.setIconified(true);
                }
                activity.searchItem.collapseActionView();
                JtsSearchAction action = new JtsSearchAction();
                action.setKey(JtsSearchAction.SEARCH_CONTENT_KEY, query);
                action.execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
    }

    public void processSearch(String keyword) {
        activity.webView.loadUrl("http://m.jitashe.org/search/tab/" + keyword);
    }

    public void processRefresh() {
        activity.webView.reload();
    }
}
