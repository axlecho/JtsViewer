package com.axlecho.jtsviewer.activity.main;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.ui.JtsLoadMoreAction;
import com.axlecho.jtsviewer.action.ui.JtsRefreshAction;
import com.axlecho.jtsviewer.action.tab.JtsParseTabListAction;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsConf;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.util.List;


public class JtsDailyScene extends BaseScene {

    private static final String TAG = "daily-scene";
    private int currentPage = 1;
    private Context context;

    public JtsDailyScene(Context context) {
        this.context = context;
        MainActivityController.getInstance().getActivity().setTitle("最新");
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public void loadMore() {
        this.currentPage++;
        JtsViewerLog.d(TAG, "load page " + currentPage);
        MainActivityController.getInstance().startFooterRefreshing();
        JtsParseTabListAction action = new JtsParseTabListAction();
        JtsLoadMoreAction loadMoreAction = new JtsLoadMoreAction();
        action.setKey(JtsBaseAction.CONTEXT_KEY, context);
        action.setKey("after_action", loadMoreAction);
        JtsNetworkManager.getInstance(context).get(JtsConf.DESKTOP_NEW_URL + currentPage, action);
    }

    @Override
    public void refresh() {
        this.currentPage = 1;
        MainActivityController.getInstance().startHeaderRefreshing();
        JtsRefreshAction refreshAction = new JtsRefreshAction();
        JtsParseTabListAction action = new JtsParseTabListAction();
        action.setKey(JtsBaseAction.CONTEXT_KEY, context);
        action.setKey("after_action", refreshAction);
        JtsNetworkManager.getInstance(context).get(JtsConf.DESKTOP_NEW_URL + currentPage, action);
    }

    @Override
    public void processLoadMore(List<JtsTabInfoModel> data) {
        MainActivityController.getInstance().processShowHome(data);
        MainActivityController.getInstance().stopFooterRefreshing();
    }

    @Override
    public void processRefreah(List<JtsTabInfoModel> data) {
        MainActivityController.getInstance().clearData();
        MainActivityController.getInstance().processShowHome(data);
        MainActivityController.getInstance().stopHeaderRefreshing();
    }
}
