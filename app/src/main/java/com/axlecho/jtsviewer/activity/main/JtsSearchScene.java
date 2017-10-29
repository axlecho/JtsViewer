package com.axlecho.jtsviewer.activity.main;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.tab.JtsParseTabListAction;
import com.axlecho.jtsviewer.action.ui.JtsLoadMoreAction;
import com.axlecho.jtsviewer.action.ui.JtsRefreshAction;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsConf;
import com.axlecho.jtsviewer.network.JtsNetworkManager;

import java.util.List;

/**
 * Created by Administrator on 2017/10/29.
 */

public class JtsSearchScene extends BaseScene {

    private int currentPage = 1;
    private Context context;
    private String keyword;

    public JtsSearchScene(Context context, String keyword) {
        this.context = context;
        this.keyword = keyword;
    }

    @Override
    public void loadMore() {
        // TODO
    }

    @Override
    public void refresh() {
        this.currentPage = 1;
        MainActivityController.getInstance().startHeaderRefreshing();
        JtsRefreshAction refreshAction = new JtsRefreshAction();
        JtsParseTabListAction action = new JtsParseTabListAction();
        action.setKey(JtsBaseAction.CONTEXT_KEY, context);
        action.setKey(JtsParseTabListAction.SRC_FROM_KEY, JtsParseTabListAction.SRC_FROM_SEARCH);
        action.setKey("after_action", refreshAction);
        JtsNetworkManager.getInstance(context).get(JtsConf.DESKTOP_SREACH_URL + keyword, action);
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
