package com.axlecho.jtsviewer.activity.main;

import android.content.Context;

import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsServer;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/10/29.
 */

public class JtsSearchScene extends BaseScene {
    private static final String TAG = "serch-scene";

    private int currentPage = 1;
    private Context context;
    private String keyword;
    private MainActivityController controller;

    public JtsSearchScene(Context context, String keyword) {
        this.context = context;
        this.keyword = keyword;
        controller = MainActivityController.getInstance();
        controller.getActivity().setTitle("搜索:" + keyword);
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public void loadMore() {
        this.currentPage++;
        controller.startFooterRefreshing();
        JtsServer.getSingleton(context).search(keyword, currentPage).subscribe(new Consumer<List<JtsTabInfoModel>>() {
            @Override
            public void accept(List<JtsTabInfoModel> jtsTabInfoModels) throws Exception {
                processLoadMore(jtsTabInfoModels);
            }
        }, controller.getErrorHandler());
    }

    @Override
    public void refresh() {
        this.currentPage = 1;


        JtsServer.getSingleton(context).search(keyword, currentPage).subscribe(new Consumer<List<JtsTabInfoModel>>() {
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
    }
}
