package com.axlecho.jtsviewer.activity.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.user.JtsParseUserInfoAction;
import com.axlecho.jtsviewer.action.user.JtsShowLoginAction;
import com.axlecho.jtsviewer.activity.JtsDetailActivity;
import com.axlecho.jtsviewer.activity.JtsSettingsActivity;
import com.axlecho.jtsviewer.activity.cache.HistoryActivity;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.module.JtsUserModule;
import com.axlecho.jtsviewer.untils.JtsConf;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.bumptech.glide.Glide;

import java.util.List;

import io.reactivex.functions.Consumer;

public class MainActivityController {
    private static final String TAG = MainActivityController.class.getSimpleName();
    private static MainActivityController instance;
    private MainActivity activity;
    private JtsTabListAdapter adapter;
    private BaseScene currentScene;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private Consumer<Throwable> errorHandler = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            JtsViewerLog.e(TAG, throwable.getMessage());
            Snackbar.make(activity.getWindow().getDecorView(), throwable.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    };


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

    public void toHistory() {
        Intent intent = new Intent();
        intent.setClass(activity, HistoryActivity.class);
        activity.startActivity(intent);
    }

    public void toSettings() {
        Intent intent = new Intent();
        intent.setClass(activity, JtsSettingsActivity.class);
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

    public void stopHeaderRefreshing() {
        activity.refreshLayout.setHeaderRefreshing(false);
    }

    public void startHeaderRefreshing() {
        activity.refreshLayout.setHeaderRefreshing(true);
    }

    public void startFooterRefreshing() {
        activity.refreshLayout.setFooterRefreshing(true);
    }

    public void stopFooterRefreshing() {
        activity.refreshLayout.setFooterRefreshing(false);
    }

    public void loadUserInfo() {
        JtsParseUserInfoAction action = new JtsParseUserInfoAction();
        JtsNetworkManager.getInstance(activity).get(JtsConf.HOST_URL, action);
    }

    public void processLoadUserInfo(final JtsUserModule user) {
        JtsViewerLog.d(TAG, user.toString());

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View headerView = activity.navigationView.getHeaderView(0);
                ImageView drawerUserInfoImageView = (ImageView) headerView.findViewById(R.id.nav_user_imageView);
                TextView userNameTextView = (TextView) headerView.findViewById(R.id.nav_user_name);

                if (user.avatarUrl != null) {
                    Glide.with(activity).load(user.avatarUrl).into(drawerUserInfoImageView);
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
        action.processAction();
    }

    public void processShowLogin() {
        JtsShowLoginAction action = new JtsShowLoginAction();
        action.setKey(JtsBaseAction.CONTEXT_KEY, activity);
        Snackbar.make(activity.recyclerView, activity.getResources().getString(R.string.unlogin_tip_long), Snackbar.LENGTH_LONG)
                .setAction(activity.getResources().getString(R.string.login), action).show();
    }

    public void clearData() {
        if (adapter == null) {
            return;
        }
        adapter.clearData();
    }

    public void processShowHome(final List<JtsTabInfoModel> content) {
        if (adapter == null) {
            adapter = new JtsTabListAdapter(activity, content);
            activity.recyclerView.setAdapter(adapter);
        } else {
            adapter.addData(content);
        }

        adapter.notifyDataSetChanged();
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
                switchSenceToSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
    }

    public void loadDefaultScene() {
        this.currentScene = new JtsDailyScene(activity);
        this.currentScene.refresh();
    }

    public BaseScene getScene() {
        return currentScene;
    }

    public void switchSenceToDaily() {
        this.currentScene = new JtsDailyScene(activity);
        this.currentScene.refresh();
    }

    public void switchSenceToHot() {
        this.currentScene = new JtsHotScene(activity);
        this.currentScene.refresh();
    }

    public void switchSenceToSearch(String keyword) {
        this.currentScene = new JtsSearchScene(activity, keyword);
        this.currentScene.refresh();
    }

    public Consumer<Throwable> getErrorHandler() {
        return errorHandler;
    }

    public JtsTabInfoModel findTabInfoByGid(long gid) {
        return adapter.findTabInfoByGid(gid);
    }

    public void startDetailActivity(JtsTabInfoModel model, View shareView) {

        String transition_name = activity.getResources().getString(R.string.detail_transition);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, shareView, transition_name);

        Intent intent = new Intent();
        intent.putExtra("tabinfo", model);
        intent.setClass(activity, JtsDetailActivity.class);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public void detachToActivity() {
        JtsNetworkManager.getInstance(activity).cancelAll();
        activity = null;
    }

    public void stopLoadingProgressBar() {
        activity.findViewById(R.id.main_loading_progressbar).setVisibility(View.GONE);
    }
}
