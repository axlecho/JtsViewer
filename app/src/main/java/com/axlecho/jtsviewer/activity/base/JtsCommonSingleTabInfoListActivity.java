package com.axlecho.jtsviewer.activity.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.aesthetic.AestheticActivity;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.activity.my.JtsHistoryActivity;
import com.axlecho.jtsviewer.widget.RecycleViewDivider;
import com.google.android.material.snackbar.Snackbar;
import com.hippo.refreshlayout.RefreshLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class JtsCommonSingleTabInfoListActivity extends AestheticActivity
        implements RefreshLayout.OnRefreshListener {
    private static final String TAG = JtsHistoryActivity.class.getSimpleName();
    public RecyclerView recyclerView;
    public RefreshLayout refreshLayout;
    public Toolbar toolbar;
    private JtsCommonSingleTableInfoListActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.common_content_recyclerview);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        refreshLayout = (RefreshLayout) findViewById(R.id.common_swip_refresh_layout);
        refreshLayout.setFooterColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        refreshLayout.setHeaderColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(this);

        this.controller = new JtsCommonSingleTableInfoListActivityController();
        this.controller.setActivity(this);
        this.controller.loadScene();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.detachFromActivity();
    }

    @Override
    public void onHeaderRefresh() {
        this.controller.getScene().refresh();
    }

    @Override
    public void onFooterRefresh() {
        controller.getScene().loadMore();
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void showError(String msg) {
        ViewGroup rootView = (ViewGroup) findViewById(R.id.activity_content_layout);
        View errorLayout = findViewById(R.id.error_tip_layout);
        if (errorLayout == null) {
            errorLayout = LayoutInflater.from(this).inflate(R.layout.view_error_tip, rootView);
        }
        ImageView errorImageView = (ImageView) errorLayout.findViewById(R.id.error_tip_imageview);
        TextView errorTextView = (TextView) errorLayout.findViewById(R.id.error_tip_textview);
        errorTextView.setText(msg);

        errorLayout.setVisibility(View.VISIBLE);
    }

    public void showMessage(String msg) {
        Snackbar.make(this.getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG).show();
    }

    public void hideError() {
        View errorLayout = findViewById(R.id.error_tip_layout);
        if (errorLayout != null) {
            errorLayout.setVisibility(View.GONE);
        }
    }
}