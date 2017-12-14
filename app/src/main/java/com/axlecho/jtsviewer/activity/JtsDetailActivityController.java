package com.axlecho.jtsviewer.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.tab.JtsParseTabTypeAction;
import com.axlecho.jtsviewer.action.tab.JtsParseThreadAction;
import com.axlecho.jtsviewer.action.ui.JtsLoadMoreThreadAction;
import com.axlecho.jtsviewer.action.ui.JtsStopVideoAction;
import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.module.JtsThreadModule;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsConf;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.bumptech.glide.Glide;
import com.hippo.refreshlayout.RefreshLayout;

import java.util.List;

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

    private static JtsDetailActivityController instance;

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
        JtsNetworkManager.getInstance(activity).get(JtsConf.DESKTOP_HOST_URL + info.url, createDetailInfoProcessor());
    }

    public void processDetail(JtsTabDetailModule detail) {
        this.detail = detail;

        this.registerListener();
        if (adapter == null) {
            adapter = new JtsThreadListAdapter(activity);
            activity.recyclerView.setAdapter(adapter);
        }

        adapter.addData(detail.threadList);
    }

    public void bindTabInfo() {
        this.page = 1;
        JtsTabInfoModel model = (JtsTabInfoModel) activity.getIntent().getSerializableExtra("tabinfo");
        JtsViewerLog.d(TAG, "[bindTabInfo] " + model);
        Glide.with(activity).load(JtsTextUnitls.getResizePicUrl(model.avatar, 200, 300))
                .error(R.drawable.ic_launcher)
                .into(activity.avatar);
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

    public void detachToActivity() {
        this.stopVideoPlayer();
        JtsNetworkManager.getInstance(activity).cancelAll();
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
        JtsViewerLog.appendToFile(activity, detail.raw);
        JtsParseTabTypeAction action = new JtsParseTabTypeAction();
        action.setKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY, detail.raw);
        action.setKey(JtsBaseAction.CONTEXT_KEY, activity);
        action.setKey(JtsParseTabTypeAction.GID_KEY, JtsTextUnitls.getTabKeyFromUrl(info.url));
        return action;
    }

    public JtsBaseAction createDetailInfoProcessor() {
        JtsParseThreadAction action = new JtsParseThreadAction(activity);
        action.setKey(JtsBaseAction.CONTEXT_KEY, activity);
        return action;
    }

    public JtsBaseAction createLoadMoreThreadProcessor() {
        JtsLoadMoreThreadAction action = new JtsLoadMoreThreadAction();
        action.setKey(JtsBaseAction.CONTEXT_KEY, activity);
        return action;
    }

    private void loadMoreThread() {
        page++;
        String url = JtsConf.DESKTOP_HOST_URL + info.url + "/" + page;
        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, url);
        JtsNetworkManager.getInstance(activity).get(url, createLoadMoreThreadProcessor());
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
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(JtsConf.DESKTOP_HOST_URL + info.url);
        intent.setData(content_url);
        activity.startActivity(intent);

    }
}
