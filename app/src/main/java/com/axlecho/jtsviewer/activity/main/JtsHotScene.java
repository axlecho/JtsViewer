package com.axlecho.jtsviewer.activity.main;

import android.content.Context;

import com.axlecho.jtsviewer.activity.base.JtsBaseController;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsServer;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/10/29.
 */

public class JtsHotScene extends BaseScene {
    private static final String TAG = "hot-scene";

    public JtsHotScene(Context context, JtsBaseController controller) {
        super(context, controller);
        setTitle("热门");
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public void loadMore() {
        super.loadMore();
        JtsServer.getSingleton(context).getHotTab(currentPage).subscribe(new Consumer<List<JtsTabInfoModel>>() {
            @Override
            public void accept(List<JtsTabInfoModel> jtsTabInfoModels) throws Exception {
                processLoadMore(jtsTabInfoModels);
            }
        }, controller.getErrorHandler());
    }

    @Override
    public void refresh() {
        super.refresh();
        JtsServer.getSingleton(context).getHotTab(currentPage).subscribe(new Consumer<List<JtsTabInfoModel>>() {
            @Override
            public void accept(List<JtsTabInfoModel> jtsTabInfoModels) throws Exception {
                processRefreah(jtsTabInfoModels);
            }
        }, controller.getErrorHandler());
    }


}
