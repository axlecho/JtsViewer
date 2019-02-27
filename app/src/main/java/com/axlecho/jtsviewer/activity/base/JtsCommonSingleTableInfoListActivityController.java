package com.axlecho.jtsviewer.activity.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.activity.detail.JtsDetailActivity;
import com.axlecho.jtsviewer.activity.main.BaseScene;
import com.axlecho.jtsviewer.activity.main.JtsSearchScene;
import com.axlecho.jtsviewer.activity.main.JtsTabListAdapter;
import com.axlecho.jtsviewer.activity.my.JtsCollectionScene;
import com.axlecho.jtsviewer.bookmark.JtsBookMarkHelper;
import com.axlecho.jtsviewer.cache.CacheManager;
import com.axlecho.jtsviewer.module.CacheModule;
import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.network.JtsServer;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class JtsCommonSingleTableInfoListActivityController implements JtsBaseController {
    private static final String TAG = "common";
    private JtsTabListAdapter adapter;
    private BaseScene scene;
    private JtsCommonSingleTabInfoListActivity activity;
    private List<Disposable> disposables = new ArrayList<>();

    private Consumer<Throwable> errorHandler = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            throwable.printStackTrace();
            stopLoadingProgressBar();
            activity.showError(JtsTextUnitls.getErrorMessageFromException(activity, throwable));
        }
    };

    public void setActivity(final JtsCommonSingleTabInfoListActivity activity) {
        this.activity = activity;
        this.adapter = new JtsTabListAdapter(activity);
        this.activity.recyclerView.setAdapter(adapter);

        adapter.addOnItemClickListener(new JtsBaseRecycleViewAdapter.OnItemClickListener<JtsTabInfoModel>() {
            @Override
            public void onItemClick(JtsTabInfoModel module, View shareView) {
                JtsViewerLog.d(TAG, module.url);
                startDetailActivity(module, shareView);
            }

            @Override
            public void onItemAvatarClick(JtsTabInfoModel module, View shareView) {

            }
        });

        adapter.addOnItemLongClickListener(new JtsBaseRecycleViewAdapter.OnItemLongClickListener<JtsTabInfoModel>() {
            @Override
            public void onItemLongClick(JtsTabInfoModel module) {
                JtsBookMarkHelper.getSingleton(activity).add(module);
                JtsBookMarkHelper.getSingleton(activity).notifyDataChange();
            }
        });
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
        } else if (sceneType.equals("collection")) {
            long collectionId = activity.getIntent().getLongExtra("collection-id", -1);
            JtsViewerLog.d(TAG, "collection id is " + collectionId);
            this.scene = new JtsCollectionScene(activity, collectionId, this);
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
        JtsViewerLog.i(JtsViewerLog.DEFAULT_MODULE, TAG, content.toString());
        adapter.addData(content);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void generateShortcut(final JtsTabInfoModel model) {
        final long tabKey = JtsTextUnitls.getTabKeyFromUrl(model.url);
        Disposable disposable = JtsServer.getSingleton(activity).getDetail(tabKey).subscribe(new Consumer<JtsTabDetailModule>() {
            @Override
            public void accept(JtsTabDetailModule detail) throws Exception {
                if (detail.gtpUrl != null) {
                    JtsServer.getSingleton(activity).downloadWithCache(tabKey, detail.gtpUrl, model)
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    CacheModule cache = CacheManager.getInstance(activity).getModule(tabKey);
                                    CacheManager.getInstance(activity).generateShortcutFromCache(cache).subscribe(new Consumer<Bitmap>() {
                                        @Override
                                        public void accept(Bitmap bitmap) throws Exception {
                                            activity.showMessage(activity.getResources().getString(R.string.add_short_cut));
                                        }
                                    });
                                }
                            });
                }

            }
        });
        disposables.add(disposable);
    }

    @Override
    public void startDetailActivity(JtsTabInfoModel model, View shareView) {
        String transition_name = activity.getResources().getString(R.string.detail_transition);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, shareView, transition_name);

        Intent intent = new Intent();
        intent.putExtra("tabinfo", model);
        intent.setClass(activity, JtsDetailActivity.class);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public void detachFromActivity() {
        JtsNetworkManager.getInstance(activity).cancelAll();

        for (Disposable disposable : disposables) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        disposables.clear();

        // this.activity = null;
        // this.adapter = null;
    }
}
