package com.axlecho.jtsviewer.activity.detail;

import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.tab.JtsGtpTabAction;
import com.axlecho.jtsviewer.action.tab.JtsImgTabAction;
import com.axlecho.jtsviewer.action.ui.JtsStopVideoAction;
import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.module.JtsThreadModule;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.network.JtsPageParser;
import com.axlecho.jtsviewer.network.JtsServer;
import com.axlecho.jtsviewer.untils.JtsConf;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsToolUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.axlecho.sakura.SakuraPlayerView;
import com.bumptech.glide.Glide;
import com.hippo.refreshlayout.RefreshLayout;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/7.
 */

public class JtsDetailActivityController implements RefreshLayout.OnRefreshListener {
    private static final String TAG = "detail";
    private JtsDetailActivity activity;
    private JtsThreadListAdapter adapter;
    private JtsTabDetailModule detail;
    private JtsTabInfoModel info;
    private int page = 1;

    private List<Disposable> disposables = new ArrayList<>();

    private static JtsDetailActivityController instance;

    private Consumer<Throwable> errorHandler = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            JtsToolUnitls.hideSoftInput(activity, activity.comment);
            activity.showError(throwable.getMessage());
        }
    };

    public static JtsDetailActivityController getInstance() {
        if (instance == null) {
            synchronized (JtsDetailActivity.class) {
                if (instance == null) {
                    instance = new JtsDetailActivityController();
                }
            }
        }
        return instance;
    }


    public void attachToActivity(JtsDetailActivity activity) {
        this.activity = activity;
        this.bindTabInfo();
    }

    public void getTabDetail() {
        this.info = (JtsTabInfoModel) activity.getIntent().getSerializableExtra("tabinfo");
        long tabKey = JtsTextUnitls.getTabKeyFromUrl(info.url);
        Disposable disposable = JtsServer.getSingleton(activity).getDetail(tabKey).subscribe(new Consumer<JtsTabDetailModule>() {
            @Override
            public void accept(JtsTabDetailModule jtsTabDetailModule) throws Exception {
                processDetail(jtsTabDetailModule);
            }
        }, errorHandler);
        disposables.add(disposable);
    }

    public void processDetail(JtsTabDetailModule detail) {
        this.detail = detail;

        this.registerListener();
        if (adapter == null) {
            adapter = new JtsThreadListAdapter(activity);
            activity.recyclerView.setAdapter(adapter);
        }

        adapter.addData(detail.threadList);
        activity.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendComment();
            }
        });
        this.stopLoadingProgressBar();
    }

    public void bindTabInfo() {
        this.page = 1;
        JtsTabInfoModel model = (JtsTabInfoModel) activity.getIntent().getSerializableExtra("tabinfo");
        JtsViewerLog.d(TAG, "[bindTabInfo] " + model);

        TextDrawable defaultDrawable = TextDrawable.builder()
                .beginConfig().height(200).width(300).bold().endConfig()
                .buildRect(model.title.substring(0, 1), activity.getResources().getColor(R.color.colorPrimary));
        Glide.with(activity).load(JtsTextUnitls.getResizePicUrl(model.avatar, 200, 300))
                .error(defaultDrawable).into(activity.avatar);

        activity.title.setText(model.title);
        activity.author.setText(model.author);
        activity.type.setText(model.type);

        activity.otherActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.initPopMenu();
                initPopMenuAction();
                activity.popMenu();
            }
        });


    }

    public void detachFromActivity() {
        this.stopVideoPlayer();
        JtsNetworkManager.getInstance(activity).cancelAll();

        for (Disposable disposable : disposables) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        disposables.clear();

        this.activity = null;
        this.adapter = null;
    }

    public void registerListener() {
        activity.findViewById(R.id.tab_detail_play).setOnClickListener(createPlayProcessor());
    }

    public void initPopMenuAction() {
        activity.popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_open_in_other_app:
                        openInOtherApp();
                        break;
                    case R.id.action_refresh:
                        getTabDetail();
                        break;
                }
                return true;
            }
        });
    }

    public JtsBaseAction createPlayProcessor() {
        // JtsViewerLog.appendToFile(activity, detail.raw);

        JtsBaseAction action;

        long gid = JtsTextUnitls.getTabKeyFromUrl(info.url);
        if (detail.gtpUrl != null) {
            action = new JtsGtpTabAction(activity, gid, detail.gtpUrl);
        } else if (detail.imgUrls != null && detail.imgUrls.size() != 0) {
            action = new JtsImgTabAction(activity, gid, detail.imgUrls);
        } else {
            action = new JtsBaseAction() {
                @Override
                public void processAction() {
                    activity.showError(R.string.error_comment_null);
                }
            };
        }

        return action;
    }

    public void sendComment() {
        String comment = activity.comment.getText().toString();
        if (TextUtils.isEmpty(comment)) {
            JtsToolUnitls.hideSoftInput(activity, activity.comment);
            activity.showError(R.string.error_comment_null);
            return;
        }

        Disposable disposable = JtsServer.getSingleton(activity).postComment(detail.fid, JtsTextUnitls.getTabKeyFromUrl(info.url), comment, detail.formhash)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        processPostComment(s);
                    }
                }, errorHandler);
        disposables.add(disposable);
    }

    private void loadMoreThread() {
        page++;


        this.info = (JtsTabInfoModel) activity.getIntent().getSerializableExtra("tabinfo");
        long tabKey = JtsTextUnitls.getTabKeyFromUrl(info.url);
        Disposable disposable = JtsServer.getSingleton(activity).getThread(tabKey, page)
                .subscribe(new Consumer<List<JtsThreadModule>>() {
                    @Override
                    public void accept(List<JtsThreadModule> jtsThreadModules) throws Exception {
                        processLoadMoreThread(jtsThreadModules);
                    }

                }, errorHandler);

        disposables.add(disposable);
    }

    public void processLoadMoreThread(List<JtsThreadModule> threads) {
        activity.refreshLayout.setFooterRefreshing(false);
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

    public boolean processBackPressed() {
        SakuraPlayerView player = (SakuraPlayerView) activity.findViewById(R.id.player);
        if (player != null) {
            if (player.isFullScreen) {
                player.toggleFullScreen();
                return false;
            }

            player.stop();
            ViewGroup root = (ViewGroup) activity.findViewById(android.R.id.content);
            root.removeView(player);
            return false;
        }
        return true;
    }

    public void processPostComment(String result) {
        JtsToolUnitls.hideSoftInput(activity, activity.comment);

        if (result.equals(JtsConf.STATUS_SUCCESSED)) {
            activity.comment.setText("");
            getTabDetail();
            activity.showError(R.string.comment_success);
        } else {
            activity.showError(R.string.comment_failed);
        }


    }

    @Override
    public void onHeaderRefresh() {
        activity.refreshLayout.setHeaderRefreshing(false);
    }

    @Override
    public void onFooterRefresh() {
        activity.refreshLayout.setFooterRefreshing(true);
        loadMoreThread();
    }

    private void stopVideoPlayer() {
        JtsStopVideoAction action = new JtsStopVideoAction(activity);
        action.execute();
    }

    private void openInOtherApp() {
        JtsToolUnitls.openUrl(activity, JtsConf.HOST_URL + info.url);
    }

    public void stopLoadingProgressBar() {
        activity.findViewById(R.id.detail_loading_progressbar).setVisibility(View.INVISIBLE);
    }
}
