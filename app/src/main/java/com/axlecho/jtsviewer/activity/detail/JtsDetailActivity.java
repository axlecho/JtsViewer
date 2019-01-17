package com.axlecho.jtsviewer.activity.detail;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.axlecho.jtsviewer.JtsApplication;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.widget.RecycleViewDivider;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.hippo.refreshlayout.RefreshLayout;

public class JtsDetailActivity extends AppCompatActivity implements RefreshLayout.OnRefreshListener {
    private static final String TAG = "detail-scene";
    public RecyclerView recyclerView;

    public ImageView avatar;
    public TextView title;
    public TextView author;
    public TextView type;

    public EditText comment;
    public ImageView send;
    public FloatingActionButton play;
    public View commentLayout;


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
        comment = (EditText) findViewById(R.id.comment_edittext);
        send = (ImageView) findViewById(R.id.comment_send);
        play = (FloatingActionButton) findViewById(R.id.tab_detail_play);
        commentLayout = findViewById(R.id.comment_layout);
        otherActions = findViewById(R.id.tab_detail_other_actions);

        this.controller = new JtsDetailActivityController();
        this.controller.attachToActivity(this);

        refreshLayout = (RefreshLayout) findViewById(R.id.main_swip_refresh_layout);
        refreshLayout.setFooterColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        refreshLayout.setHeaderColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(this);

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
    protected void onStop() {
        super.onStop();
        stopLoading();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.detachFromActivity();
    }

    @Override
    public void onBackPressed() {
        if (controller.processBackPressed()) {
            super.onBackPressed();
        }
    }

    public void startLoading() {
        findViewById(R.id.detail_loading_progressbar).setVisibility(View.VISIBLE);
    }

    public void stopLoading() {
        findViewById(R.id.detail_loading_progressbar).setVisibility(View.GONE);
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
        Snackbar.make(this.getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG).show();
    }

    public void hideError() {
        View errorLayout = findViewById(R.id.error_tip_layout);
        if (errorLayout != null) {
            errorLayout.setVisibility(View.GONE);
        }
    }

    public void showMessage(int resId) {
        View rootView = findViewById(R.id.comment_layout);
        Snackbar.make(rootView, resId, Snackbar.LENGTH_LONG).show();
    }

    public void showMessage(String msg) {
        View rootView = findViewById(R.id.comment_layout);
        Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG).show();
    }

    public void initPopMenu() {
        if (popupMenu != null) {
            return;
        }

        popupMenu = new PopupMenu(this, otherActions, Gravity.TOP);
        popupMenu.getMenuInflater().inflate(R.menu.scene_tab_detail, popupMenu.getMenu());
        controller.initPopMenuAction();
    }

    public void popMenu() {
        popupMenu.show();
    }

    public void showPlayBtn() {
        Animation animation = new ScaleAnimation(0.1f, Animation.RELATIVE_TO_SELF,
                0.1f, Animation.RELATIVE_TO_SELF,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        // AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);

        animation.setDuration(50);
        play.startAnimation(animation);
        play.setVisibility(View.VISIBLE);
    }

    public void showCommentEdittext() {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);

        // AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);

        animation.setDuration(50);
        commentLayout.startAnimation(animation);
        commentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHeaderRefresh() {
        refreshLayout.setHeaderRefreshing(false);
    }

    @Override
    public void onFooterRefresh() {
        controller.loadMoreThread();
    }
}
