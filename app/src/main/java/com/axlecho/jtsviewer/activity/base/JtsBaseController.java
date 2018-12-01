package com.axlecho.jtsviewer.activity.base;

import com.axlecho.jtsviewer.module.JtsTabInfoModel;

import java.util.List;

import io.reactivex.functions.Consumer;

public interface JtsBaseController {
    void setTitle(String title);

    void startFooterRefreshing();

    void stopFooterRefreshing();

    void startHeaderRefreshing();

    void stopHeaderRefreshing();

    void stopLoadingProgressBar();

    void clearData();

    void processDataNotify(List<JtsTabInfoModel> data);

    Consumer<Throwable> getErrorHandler();
}
