package com.axlecho.jtsviewer.activity.my;

import android.content.Context;

import com.axlecho.jtsviewer.activity.base.JtsBaseController;
import com.axlecho.jtsviewer.activity.main.BaseScene;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsServer;

import java.util.List;

import io.reactivex.functions.Consumer;


public class JtsCollectionScene extends BaseScene {


    private static final String TAG = "daily-scene";
    private long collectionId;

    public JtsCollectionScene(Context context, long collectionId, JtsBaseController controller) {
        super(context, controller);
        setTitle("收藏");
        this.collectionId = collectionId;
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public void loadMore() {
        super.loadMore();
        JtsServer.getSingleton(context).getCollectionDetail(collectionId, currentPage).subscribe(new Consumer<List<JtsTabInfoModel>>() {
            @Override
            public void accept(List<JtsTabInfoModel> jtsTabInfoModels) throws Exception {
                processLoadMore(jtsTabInfoModels);
            }
        }, controller.getErrorHandler());
    }

    @Override
    public void refresh() {
        super.refresh();
        JtsServer.getSingleton(context).getCollectionDetail(collectionId, currentPage).subscribe(new Consumer<List<JtsTabInfoModel>>() {
            @Override
            public void accept(List<JtsTabInfoModel> jtsTabInfoModels) throws Exception {
                processRefreah(jtsTabInfoModels);
            }
        }, controller.getErrorHandler());
    }
}
