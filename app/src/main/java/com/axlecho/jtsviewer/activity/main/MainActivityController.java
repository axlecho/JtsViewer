package com.axlecho.jtsviewer.activity.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andryr.guitartuner.TunerActivity;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.user.JtsShowLoginAction;
import com.axlecho.jtsviewer.activity.base.JtsBaseController;
import com.axlecho.jtsviewer.activity.base.JtsBaseRecycleViewAdapter;
import com.axlecho.jtsviewer.activity.base.JtsCommonSingleTabInfoListActivity;
import com.axlecho.jtsviewer.activity.detail.JtsDetailActivity;
import com.axlecho.jtsviewer.activity.login.JtsLoginActivity;
import com.axlecho.jtsviewer.activity.my.JtsCollectionActivity;
import com.axlecho.jtsviewer.activity.my.JtsHistoryActivity;
import com.axlecho.jtsviewer.activity.my.JtsSettingsActivity;
import com.axlecho.jtsviewer.bookmark.JtsBookMarkConfigureActivity;
import com.axlecho.jtsviewer.bookmark.JtsBookMarkHelper;
import com.axlecho.jtsviewer.cache.CacheManager;
import com.axlecho.jtsviewer.module.CacheModule;
import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.module.JtsUserModule;
import com.axlecho.jtsviewer.module.JtsVersionInfoModule;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.network.JtsServer;
import com.axlecho.jtsviewer.untils.JtsDeviceUnitls;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsToolUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.bumptech.glide.Glide;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import io.reactivex.functions.Consumer;
import james.metronome.activities.MetronomeActivity;

public class MainActivityController implements JtsBaseController {
    private static final String TAG = MainActivityController.class.getSimpleName();
    private MainActivity activity;
    private JtsTabListAdapter adapter;
    private BaseScene currentScene;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private Consumer<Throwable> errorHandler = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            throwable.printStackTrace();
            stopLoadingProgressBar();
            activity.showError(JtsTextUnitls.getErrorMessageFromException(activity, throwable));
        }
    };

    public void setActivity(final MainActivity activity) {
        this.activity = activity;
        adapter = new JtsTabListAdapter(activity);
        activity.recyclerView.setAdapter(adapter);

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

    public MainActivity getActivity() {
        return this.activity;
    }


    public void verifyStoragePermissions() {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadUserInfo() {
        JtsServer.getSingleton(activity).getUserInfo().subscribe(new Consumer<JtsUserModule>() {
            @Override
            public void accept(JtsUserModule user) throws Exception {
                if (user.uid == 0) {
                    // processShowError(activity.getResources().getString(R.string.unlogin_tip));
                    bindUserNotLoadAction();
                    return;
                }

                processLoadUserInfo(user);
                bindUserNotLoadAction();
            }
        }, errorHandler);
    }

    public void processLoadUserInfo(final JtsUserModule user) {
        JtsViewerLog.d(TAG, user.toString());
        View headerView = activity.navigationView.getHeaderView(0);
        ImageView drawerUserInfoImageView = (ImageView) headerView.findViewById(R.id.nav_user_imageView);
        TextView userNameTextView = (TextView) headerView.findViewById(R.id.nav_user_name);

        if (user.avatarUrl != null) {
            user.avatarUrl = user.avatarUrl.replace("small", "big");
            Glide.with(activity).load(user.avatarUrl).into(drawerUserInfoImageView);
        }

        if (user.userName != null) {
            userNameTextView.setText(user.userName);
        }

        if (user.uid <= 0) {
            JtsShowLoginAction action = new JtsShowLoginAction();
            action.setKey(JtsBaseAction.CONTEXT_KEY, activity);
            drawerUserInfoImageView.setOnClickListener(action);
        }

    }

    public void bindUserNotLoadAction() {
        View headerView = activity.navigationView.getHeaderView(0);
        ImageView drawerUserInfoImageView = (ImageView) headerView.findViewById(R.id.nav_user_imageView);
        drawerUserInfoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLogin();
            }
        });
    }

    public void processLogin() {
        activity.closeDrawer();
        Intent intent = new Intent();
        intent.setClass(activity, JtsLoginActivity.class);
        activity.startActivity(intent);
    }

    public void processSearch() {
        SearchFragment searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                toSearch(keyword);
            }
        });
        searchFragment.showFragment(activity.getSupportFragmentManager(), SearchFragment.TAG);
    }

    public void loadDefaultScene() {
        this.currentScene = new JtsDailyScene(activity, this);
        this.currentScene.refresh();
    }

    public BaseScene getScene() {
        return currentScene;
    }

    public void toDaily() {
        this.currentScene = new JtsDailyScene(activity, this);
        this.currentScene.refresh();
    }

    public void toHot() {
        this.currentScene = new JtsHotScene(activity, this);
        this.currentScene.refresh();
    }

    public void toSearch(String keyword) {
        Intent intent = new Intent();
        intent.putExtra("scene-type", "search");
        intent.putExtra("keyword", keyword);
        intent.setClass(activity, JtsCommonSingleTabInfoListActivity.class);
        activity.startActivity(intent);
    }

    public void toAcg() {
        this.currentScene = new JtsArtistScene(activity, 19301, "Acg", this);
        this.currentScene.refresh();
    }

    public void toLearn() {
        this.currentScene = new JtsArtistScene(activity, 101941, "Learn", this);
        this.currentScene.refresh();
    }

    public void toHistory() {
        Intent intent = new Intent();
        intent.setClass(activity, JtsHistoryActivity.class);
        activity.startActivity(intent);
    }

    public void toCollection() {
        Intent intent = new Intent();
        intent.setClass(activity, JtsCollectionActivity.class);
        activity.startActivity(intent);
    }

    public void toSettings() {
        Intent intent = new Intent();
        intent.setClass(activity, JtsSettingsActivity.class);
        activity.startActivity(intent);
    }

    public void toBookMarkSettings() {
        Intent intent = new Intent();
        intent.setClass(activity, JtsBookMarkConfigureActivity.class);
        activity.startActivity(intent);
    }

    public void toTuner() {
        Intent intent = new Intent();
        intent.setClass(activity, TunerActivity.class);
        activity.startActivity(intent);
    }

    public void toMetronome() {
        Intent intent = new Intent();
        intent.setClass(activity, MetronomeActivity.class);
        activity.startActivity(intent);
    }

    public void detachFromActivity() {
        JtsNetworkManager.getInstance(activity).cancelAll();
        activity = null;
        adapter = null;
        currentScene = null;
        // instance = null;
    }

    public void checkForUpdate() {
        JtsServer.getSingleton(activity).getLastVersionInfo().subscribe(new Consumer<JtsVersionInfoModule>() {
            @Override
            public void accept(final JtsVersionInfoModule version) throws Exception {
                JtsViewerLog.d(TAG, version.toString());
                if (JtsTextUnitls.compareVersion(JtsDeviceUnitls.getVersionName(activity),
                        version.getTag_name()) <= 0) {
                    return;
                }

                int size = 0;
                String download_url = version.getHtml_url();

                for (JtsVersionInfoModule.Assets asset : version.getAssets()) {
                    if (asset.getName().endsWith(".apk")) {
                        size = asset.getSize();
                        download_url = asset.getBrowser_download_url();
                        break;
                    }
                }
                StringBuilder messageBuilder = new StringBuilder();
                messageBuilder.append(String.format(activity.getResources().getString(R.string.tip_update_version), version.getTag_name()));
                messageBuilder.append("\n");
                messageBuilder.append(String.format(activity.getResources().getString(R.string.tip_update_size), JtsTextUnitls.sizeFormat(size)));
                messageBuilder.append("\n\n");
                messageBuilder.append(version.getBody());
                String message = messageBuilder.toString();
                buildUpdateDialog(message, download_url);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                // do nothing if update error
            }
        });
    }

    public void buildUpdateDialog(String msg, final String downloadUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.title_update);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.tip_update_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                JtsToolUnitls.openUrl(activity, downloadUrl);
            }
        });
        builder.show();
    }

    @Override
    public void generateShortcut(final JtsTabInfoModel model) {
        final long tabKey = JtsTextUnitls.getTabKeyFromUrl(model.url);
        JtsServer.getSingleton(activity).getDetail(tabKey).subscribe(new Consumer<JtsTabDetailModule>() {
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

    @Override
    public void setTitle(String title) {
        activity.setTitle(title);
    }

    @Override
    public void stopLoadingProgressBar() {
        activity.findViewById(R.id.main_loading_progressbar).setVisibility(View.GONE);
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
        adapter.addData(content);
        adapter.notifyDataSetChanged();
    }

}
