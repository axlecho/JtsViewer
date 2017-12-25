package com.axlecho.jtsviewer.activity.main;

import android.content.Context;

import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsServer;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.util.List;

import io.reactivex.functions.Consumer;


public class JtsDailyScene extends BaseScene {

    private static final String TAG = "daily-scene";
    private int currentPage = 1;
    private Context context;
    private MainActivityController controller;

    public JtsDailyScene(Context context) {
        this.context = context;
        controller = MainActivityController.getInstance();
        controller.getActivity().setTitle("最新");

    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public void loadMore() {
        this.currentPage++;
        JtsViewerLog.d(TAG, "load page " + currentPage);
        controller.startFooterRefreshing();

        JtsServer.getSingleton(context).getNewTab(currentPage).subscribe(new Consumer<List<JtsTabInfoModel>>() {
            @Override
            public void accept(List<JtsTabInfoModel> jtsTabInfoModels) throws Exception {
                processLoadMore(jtsTabInfoModels);
            }
        }, controller.getErrorHandler());
    }

    @Override
    public void refresh() {
        this.currentPage = 1;
        controller.startHeaderRefreshing();

        JtsServer.getSingleton(context).getNewTab(currentPage).subscribe(new Consumer<List<JtsTabInfoModel>>() {
            @Override
            public void accept(List<JtsTabInfoModel> jtsTabInfoModels) throws Exception {
                processRefreah(jtsTabInfoModels);
            }
        }, controller.getErrorHandler());

    }

    @Override
    public void processLoadMore(List<JtsTabInfoModel> data) {
        controller.processShowHome(data);
        controller.stopFooterRefreshing();
    }

    @Override
    public void processRefreah(List<JtsTabInfoModel> data) {
        controller.clearData();
        controller.processShowHome(data);
        controller.stopHeaderRefreshing();
        controller.stopLoadingProgressBar();
    }
}
