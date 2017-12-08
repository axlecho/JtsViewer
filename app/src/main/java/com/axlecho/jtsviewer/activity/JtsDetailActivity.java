package com.axlecho.jtsviewer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axlecho.jtsviewer.JtsApplication;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.activity.main.MainActivityController;
import com.axlecho.jtsviewer.widget.RecycleViewDivider;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.hippo.refreshlayout.RefreshLayout;

public class JtsDetailActivity extends AppCompatActivity {
    private static final String TAG = "detail-scene";
    public RecyclerView recyclerView;

    public ImageView avatar;
    public TextView title;
    public TextView author;
    public TextView type;

    public PopupMenu popupMenu;
    public View otherActions;

    public RefreshLayout refreshLayout;

    private JtsDetailActivityController controller;

    private Tracker mTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        recyclerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));

        avatar = (ImageView) findViewById(R.id.tab_detail_avatar);
        title = (TextView) findViewById(R.id.tab_detail_title);
        author = (TextView) findViewById(R.id.tab_detail_author);
        type = (TextView) findViewById(R.id.tab_detail_type);
        otherActions = findViewById(R.id.tab_detail_other_actions);

        this.controller = JtsDetailActivityController.getInstance();
        this.controller.attachToActivity(this);

        refreshLayout = (RefreshLayout) findViewById(R.id.main_swip_refresh_layout);
        refreshLayout.setFooterColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        refreshLayout.setHeaderColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(controller);

        this.controller.getTabDetail();

        JtsApplication application = (JtsApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume() {
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JtsDetailActivityController.getInstance().detachToActivity();
    }


    public void initPopMenu() {
        if (popupMenu != null) {
            return;
        }

        popupMenu = new PopupMenu(this, otherActions, Gravity.TOP);
        popupMenu.getMenuInflater().inflate(R.menu.scene_tab_detail, popupMenu.getMenu());
        JtsDetailActivityController.getInstance().initPopMenuAction();
    }

    public void popMenu() {
        popupMenu.show();
    }
}
