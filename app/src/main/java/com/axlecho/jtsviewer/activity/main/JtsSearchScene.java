package com.axlecho.jtsviewer.activity.main;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.tab.JtsParseTabListAction;
import com.axlecho.jtsviewer.action.ui.JtsLoadMoreAction;
import com.axlecho.jtsviewer.action.ui.JtsRefreshAction;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsConf;
import com.axlecho.jtsviewer.network.JtsNetworkManager;

import java.util.List;
import java.util.Locale;

import static com.axlecho.jtsviewer.untils.JtsConf.DESKTOP_SEEACH_MORE_URL;

/**
 * Created by Administrator on 2017/10/29.
 */

public class JtsSearchScene extends BaseScene {
    private static final String TAG = "serch-scene";

    private int currentPage = 1;
    private Context context;
    private String keyword;


    public JtsSearchScene(Context context, String keyword) {
        this.context = context;
        this.keyword = keyword;
        MainActivityController.getInstance().getActivity().setTitle("搜索:" + keyword);
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public void loadMore() {
        this.currentPage++;
        MainActivityController.getInstance().startFooterRefreshing();
        JtsParseTabListAction action = new JtsParseTabListAction();
        JtsLoadMoreAction loadMoreAction = new JtsLoadMoreAction();
        action.setKey(JtsBaseAction.CONTEXT_KEY, context);
        action.setKey("after_action", loadMoreAction);
        JtsNetworkManager.getInstance(context).get(String.format(Locale.CHINA, DESKTOP_SEEACH_MORE_URL, getSearchKey(), currentPage), action);
    }

    @Override
    public void refresh() {
        this.currentPage = 1;
        MainActivityController.getInstance().startHeaderRefreshing();
        JtsRefreshAction refreshAction = new JtsRefreshAction();
        JtsParseTabListAction action = new JtsParseTabListAction();
        action.setKey(JtsBaseAction.CONTEXT_KEY, context);
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
