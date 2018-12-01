package com.axlecho.jtsviewer.activity.base;

import android.view.View;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.activity.main.BaseScene;
import com.axlecho.jtsviewer.activity.main.JtsSearchScene;
import com.axlecho.jtsviewer.activity.main.JtsTabListAdapter;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.util.List;

import io.reactivex.functions.Consumer;

public class JtsCommonSingleTableInfoListActivityController implements JtsBaseController {
    private static final String TAG = "common";
    private JtsTabListAdapter adapter;
    private BaseScene scene;
    private static JtsCommonSingleTableInfoListActivityController instance;
    private JtsCommonSingleTabInfoListActivity activity;

    private Consumer<Throwable> errorHandler = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            throwable.printStackTrace();
            stopLoadingProgressBar();
            activity.showError(JtsTextUnitls.getErrorMessageFromException(activity, throwable));
        }
    };

    public static JtsCommonSingleTableInfoListActivityController getInstance() {
        if (instance == null) {
            synchronized (JtsCommonSingleTabInfoListActivity.class) {
                if (instance == null) {
                    instance = new JtsCommonSingleTableInfoListActivityController();
                }
            }
        }
        return instance;
    }

    public void setActivity(JtsCommonSingleTabInfoListActivity activity) {
        this.activity = activity;
    }

    public BaseScene getScene() {
        return scene;
    }

    public void loadScene() {
        String sceneType = activity.getIntent().getStringExtra("scene-type");
        if (sceneType == null) {
            activity.showError("scene is null");
            return;
        }

        if (sceneType.equals("search")) {
            String keyword = activity.getIntent().getStringExtra("keyword");
            this.scene = new JtsSearchScene(activity, keyword, this);
            this.scene.refresh();
        } else {
            activity.showError("scene not defind");
        }
    }

    @Override
    public void setTitle(String title) {
        activity.setTitle(title);
    }

    @Override
    public void stopLoadingProgressBar() {
        activity.findViewById(R.id.common_loading_progressbar).setVisibility(View.GONE);
    }

    @Override
    public void stopHeaderRefreshing() {
        activity.refreshLayout.setHeaderRefreshing(false);
    }

    @Override
    public void startHeaderRefreshing() {
        activity.refreshLayout.setHeaderRefreshing(true);
        activity.hideError();
    }

    @Override
    public void startFooterRefreshing() {
        activity.refreshLayout.setFooterRefreshing(true);
    }

    @Override
    public void stopFooterRefreshing() {
        activity.refreshLayout.setFooterRefreshing(false);
    }

    @Override
    public Consumer<Throwable> getErrorHandler() {
        return errorHandler;
    }

    public void clearData() {
        if (adapter == null) {
            return;
        }
        adapter.clearData();
    }

    public void processDataNotify(final List<JtsTabInfoModel> content) {
        JtsViewerLog.i(JtsViewerLog.DEFAULT_MODULE,TAG,content.toString());
        if (adapter == null) {
            adapter = new JtsTabListAdapter(activity, content);
            activity.recyclerView.setAdapter(adapter);
        } else {
            adapter.addData(content);
        }

        adapter.notifyDataSetChanged();
    }

}
