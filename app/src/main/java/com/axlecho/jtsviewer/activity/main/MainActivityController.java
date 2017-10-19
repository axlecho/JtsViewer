package com.axlecho.jtsviewer.activity.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.tab.JtsParseTabListAction;
import com.axlecho.jtsviewer.action.network.JtsSearchAction;
import com.axlecho.jtsviewer.action.user.JtsParseUserInfoAction;
import com.axlecho.jtsviewer.action.user.JtsShowLoginAction;
import com.axlecho.jtsviewer.activity.cache.HistoryActivity;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.module.UserModule;
import com.axlecho.jtsviewer.network.JtsConf;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivityController {
    private static final String TAG = MainActivityController.class.getSimpleName();
    private static MainActivityController instance;
    private MainActivity activity;
    private JtsTabListAdapter adapter;
    private int currentPage = 1;
    private boolean isLoading = false;

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

    public void processJumpHistory() {
        Intent intent = new Intent();
        intent.setClass(activity, HistoryActivity.class);
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

    public void stopRefreshing() {
        activity.swipeRefreshLayout.setRefreshing(false);
    }

    public void startRefreshing() {
        activity.swipeRefreshLayout.setRefreshing(true);
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
                    JtsShowLoginAction action = new JtsShowLoginAction();
                    action.setKey(JtsBaseAction.CONTEXT_KEY, activity);
                    drawerUserInfoImageView.setOnClickListener(action);
                }
            }
        });

    }

    public void processLogin() {
        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        JtsShowLoginAction action = new JtsShowLoginAction();
        action.setKey(JtsBaseAction.CONTEXT_KEY, activity);
        action.execute();
    }

    public void processLoadHome() {
        startRefreshing();
        JtsParseTabListAction action = new JtsParseTabListAction();
        action.setKey(JtsBaseAction.CONTEXT_KEY, activity);
        action.setKey(JtsParseTabListAction.SRC_FROM_KEY, JtsParseTabListAction.SRC_FROM_DIALY);
        JtsNetworkManager.getInstance(activity).get(JtsConf.DESKTOP_NEW_URL + currentPage, action);
    }

    public void processLoadMore() {
        if(isLoading) {
            JtsViewerLog.d(TAG,"loading more");
            return;
        }

        this.currentPage ++;
        isLoading = true;
        processLoadHome();
    }

    public void processShowLogin() {
        JtsShowLoginAction action = new JtsShowLoginAction();
        action.setKey(JtsBaseAction.CONTEXT_KEY, activity);
        Snackbar.make(activity.recyclerView, activity.getResources().getString(R.string.unlogin_tip_long), Snackbar.LENGTH_LONG)
                .setAction(activity.getResources().getString(R.string.login), action).show();
    }

    public void processShowHome(final List<JtsTabInfoModel> content) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adapter == null) {
                    adapter = new JtsTabListAdapter(activity, content);
                } else {
                    adapter.addData(content);
                }

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                activity.recyclerView.setLayoutManager(layoutManager);
                activity.recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                stopRefreshing();
                isLoading = false;
            }
        });
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
                action.setKey(JtsBaseAction.CONTEXT_KEY, activity);
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


    public void processRefresh() {
        // TODO
    }
}
