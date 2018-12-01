package com.axlecho.jtsviewer.activity.main;

import android.content.Context;

import com.axlecho.jtsviewer.activity.base.JtsBaseController;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.util.List;

/**
 * Created by Administrator on 2017/10/28.
 */

public abstract class BaseScene {

    protected JtsBaseController controller;
    protected Context context;
    protected int currentPage = 1;

    public BaseScene(Context context, JtsBaseController controller) {
        this.context = context;
        this.controller = controller;
    }

    public void setTitle(String title) {
        controller.setTitle(title);
    }

    public void loadMore() {
        this.currentPage++;
        JtsViewerLog.d(getName(), "load page " + currentPage);
        controller.startFooterRefreshing();
    }

    public void refresh() {
        this.currentPage = 1;
        controller.startHeaderRefreshing();
    }

    public void processLoadMore(List<JtsTabInfoModel> data) {
        controller.processDataNotify(data);
        controller.stopFooterRefreshing();
    }

    public void processRefreah(List<JtsTabInfoModel> data) {
        controller.clearData();
        controller.processDataNotify(data);
        controller.stopHeaderRefreshing();
        controller.stopLoadingProgressBar();
    }

    public abstract String getName();
}
