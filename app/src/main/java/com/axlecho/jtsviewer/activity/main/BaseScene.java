package com.axlecho.jtsviewer.activity.main;

import com.axlecho.jtsviewer.module.JtsTabInfoModel;

import java.util.List;

/**
 * Created by Administrator on 2017/10/28.
 */

public abstract class BaseScene {

    private int searchKey = -1;

    public abstract void loadMore();

    public abstract void refresh();

    public abstract void processLoadMore(List<JtsTabInfoModel> data);

    public abstract void processRefreah(List<JtsTabInfoModel> data);

    public void setSearchKey(int searchKey) {
        this.searchKey = searchKey;
    }

    public int getSearchKey() {
        return searchKey;
    }
}
