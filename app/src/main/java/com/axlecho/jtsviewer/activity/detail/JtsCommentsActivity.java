package com.axlecho.jtsviewer.activity.detail;

import android.animation.Animator;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.aesthetic.AestheticActivity;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.ui.JtsStopVideoAction;
import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.module.JtsThreadModule;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.network.JtsServer;
import com.axlecho.jtsviewer.untils.JtsConf;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsToolUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.axlecho.jtsviewer.widget.RecycleViewDivider;
import com.axlecho.sakura.SakuraPlayerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hippo.refreshlayout.RefreshLayout;
import com.hippo.yorozuya.AnimationUtils;
import com.hippo.yorozuya.SimpleAnimatorListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class JtsCommentsActivity extends AestheticActivity implements RefreshLayout.OnRefreshListener {
    private static final String TAG = "comments-scene";
    public RecyclerView recyclerView;
    public RefreshLayout refreshLayout;

    public EditText comment;
    public ImageView send;
    public View commentLayout;
    public FloatingActionButton replyButton;

    private int page = 1;
    private JtsThreadListAdapter adapter;
    private JtsTabInfoModel info;
    private JtsTabDetailModule detail;

    private List<Disposable> disposables = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.comment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));

        refreshLayout = (RefreshLayout) findViewById(R.id.main_swip_refresh_layout);
        refreshLayout.setFooterColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        refreshLayout.setHeaderColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(this);

        commentLayout = findViewById(R.id.comment_layout);
        send = findViewById(R.id.comment_send);
        replyButton = findViewById(R.id.fab);
        comment = findViewById(R.id.comment_edittext);

        initAction();
        loadComments();
    }

    public void showEditPanelWithAnimation() {
        if (null == replyButton || null == commentLayout) {
            return;
        }

        replyButton.setTranslationX(0.0f);
        replyButton.setTranslationY(0.0f);
        replyButton.setScaleX(1.0f);
        replyButton.setScaleY(1.0f);
        int fabEndX = commentLayout.getLeft() + (commentLayout.getWidth() / 2) - (replyButton.getWidth() / 2);
        int fabEndY = commentLayout.getTop() + (commentLayout.getHeight() / 2) - (replyButton.getHeight() / 2);
        replyButton.animate().x(fabEndX).y(fabEndY).scaleX(0.0f).scaleY(0.0f)
                .setInterpolator(AnimationUtils.SLOW_FAST_SLOW_INTERPOLATOR)
                .setDuration(300L).setListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (null == replyButton || null == commentLayout) {
                    return;
                }

                replyButton.setVisibility(View.INVISIBLE);
                commentLayout.setVisibility(View.VISIBLE);
                int halfW = commentLayout.getWidth() / 2;
                int halfH = commentLayout.getHeight() / 2;
                Animator animator = ViewAnimationUtils.createCircularReveal(commentLayout, halfW, halfH, 0,
                        (float) Math.hypot(halfW, halfH)).setDuration(300L);
                animator.addListener(new SimpleAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator a) {

                    }
                });
                animator.start();
            }
        }).start();
    }

    private void hideEditPanelWithAnimation() {
        if (null == replyButton || null == commentLayout) {
            return;
        }

        int halfW = commentLayout.getWidth() / 2;
        int halfH = commentLayout.getHeight() / 2;
        Animator animator = ViewAnimationUtils.createCircularReveal(commentLayout, halfW, halfH,
                (float) Math.hypot(halfW, halfH), 0.0f).setDuration(300L);
        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator a) {
                if (null == replyButton || null == commentLayout) {
                    return;
                }

                if (Looper.myLooper() != Looper.getMainLooper()) {
                    // Some devices may run this block in non-UI thread.
                    // It might be a bug of Android OS.
                    // Check it here to avoid crash.
                    return;
                }

                commentLayout.setVisibility(View.GONE);
                ((View) replyButton).setVisibility(View.VISIBLE);
                int fabStartX = commentLayout.getLeft() + (commentLayout.getWidth() / 2) - (replyButton.getWidth() / 2);
                int fabStartY = commentLayout.getTop() + (commentLayout.getHeight() / 2) - (replyButton.getHeight() / 2);
                replyButton.setX(fabStartX);
                replyButton.setY(fabStartY);
                replyButton.setScaleX(0.0f);
                replyButton.setScaleY(0.0f);
                replyButton.setRotation(-45.0f);
                replyButton.animate().translationX(0.0f).translationY(0.0f).scaleX(1.0f).scaleY(1.0f).rotation(0.0f)
                        .setInterpolator(AnimationUtils.SLOW_FAST_SLOW_INTERPOLATOR)
                        .setDuration(300L).setListener(new SimpleAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }
                }).start();
            }
        });
        animator.start();
    }


    public void initAction() {
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditPanelWithAnimation();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
    }

    public void loadComments() {
        this.info = (JtsTabInfoModel) getIntent().getSerializableExtra("info");
        this.detail = (JtsTabDetailModule) getIntent().getSerializableExtra("detail");
        processDetail();
    }

    @Override
    protected void onDestroy() {
        quit();
        super.onDestroy();
    }

    @Override
    public void onHeaderRefresh() {
        refreshLayout.setHeaderRefreshing(false);
    }

    @Override
    public void onFooterRefresh() {
        loadMoreThread();
    }

    public void processDetail() {
        if (adapter == null) {
            adapter = new JtsThreadListAdapter(this);
            this.recyclerView.setAdapter(adapter);
        }
        adapter.addData(detail.threadList);
        adapter.notifyDataSetChanged();
    }

    public void loadMoreThread() {
        page++;
        long tabKey = JtsTextUnitls.getTabKeyFromUrl(info.url);
        Disposable disposable = JtsServer.getSingleton(this).getThread(tabKey, page)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        refreshLayout.setFooterRefreshing(true);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        refreshLayout.setFooterRefreshing(false);
                    }
                })
                .subscribe(new Consumer<List<JtsThreadModule>>() {
                    @Override
                    public void accept(List<JtsThreadModule> jtsThreadModules) throws Exception {
                        processLoadMoreThread(jtsThreadModules);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showMessage(throwable.getMessage());
                    }
                });
        disposables.add(disposable);
    }

    public void sendComment() {
        String content = comment.getText().toString();
        if (TextUtils.isEmpty(content)) {
            JtsToolUnitls.hideSoftInput(this, comment);
            return;
        }

        Disposable disposable = JtsServer.getSingleton(this)
                .postComment(detail.fid, JtsTextUnitls.getTabKeyFromUrl(info.url), content, detail.formhash)
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        JtsToolUnitls.hideSoftInput(JtsCommentsActivity.this, comment);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        processPostComment(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        disposables.add(disposable);
    }

    public void processLoadMoreThread(List<JtsThreadModule> threads) {
        if (threads == null) {
            JtsViewerLog.e(TAG, "[processLoadMoreThread] null");
            return;
        }

        if (threads.size() == 0) {
            JtsViewerLog.e(TAG, "[processLoadMoreThread] empty");
            return;
        }

        if (threads.get(0) != null) {
            for (JtsThreadModule module : detail.threadList) {
                if (module.floor.equals(threads.get(0).floor)) {
                    JtsViewerLog.w(TAG, "[processLoadMoreThread] over flow");
                    return;
                }
            }

        }

        detail.threadList.addAll(threads);
        adapter.notifyDataSetChanged();
    }

    public void processPostComment(String result) {
        JtsToolUnitls.hideSoftInput(this, comment);

        if (result.equals(JtsConf.STATUS_SUCCESSED)) {
            comment.setText("");
            refresh();
            showMessage(R.string.comment_success);
        } else {
            showMessage(R.string.comment_failed);
        }


    }

    public void refresh() {

    }

    public void showMessage(int resId) {
        View rootView = findViewById(R.id.comment_layout);
        Snackbar.make(rootView, resId, Snackbar.LENGTH_LONG).show();
    }

    public void showMessage(String msg) {
        View rootView = findViewById(R.id.comment_layout);
        Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG).show();
    }

    public void quit() {
        this.stopVideoPlayer();
        JtsNetworkManager.getInstance(this).cancelAll();

        for (Disposable disposable : disposables) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        disposables.clear();
    }

    private void stopVideoPlayer() {
        JtsStopVideoAction action = new JtsStopVideoAction(this);
        action.execute();
    }

    public boolean processBackPressed() {
        if (null != commentLayout && commentLayout.getVisibility() == View.VISIBLE) {
            hideEditPanelWithAnimation();
            return false;
        }

        SakuraPlayerView player = this.findViewById(R.id.player);
        if (player != null) {
            if (player.isFullScreen) {
                player.toggleFullScreen();
                return false;
            }

            player.stop();
            ViewGroup root = this.findViewById(android.R.id.content);
            root.removeView(player);
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {


        if (this.processBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
