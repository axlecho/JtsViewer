package com.axlecho.jtsviewer.action.tab;

import android.content.Context;
import android.text.TextUtils;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.ui.JtsShowGtpTabAction;
import com.axlecho.jtsviewer.activity.main.MainActivityController;
import com.axlecho.jtsviewer.cache.CacheManager;
import com.axlecho.jtsviewer.module.CacheModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsServer;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;


public class JtsGtpTabAction extends JtsBaseAction {

    private static final String TAG = JtsGtpTabAction.class.getSimpleName();
    private Context context;
    private long gid;
    private String gtpUrl;

    public JtsGtpTabAction(Context context, long gid, String gtpUrl) {
        this.context = context;
        this.gid = gid;
        this.gtpUrl = gtpUrl;
    }

    @Override
    public void processAction() {
        Observable<String> cache = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                CacheModule module = CacheManager.getInstance(context).getModule(gid);
                JtsViewerLog.d(TAG, "find " + gid + " from cache" + module);
                if (module != null) {
                    e.onNext( module.fileName);
                    JtsViewerLog.d(TAG, "get " + gid + " from cache");
                }
                e.onComplete();
            }
        });

        final String path = CacheManager.getInstance(context).getCachePath() + File.separator + gid;
        Observable<String> network = JtsServer.getSingleton(context).download(gtpUrl, path).doOnNext(new Consumer<String>() {
            @Override
            public void accept(String result) throws Exception {
                JtsViewerLog.d(TAG,"save " + gid + " to cache");
                JtsTabInfoModel tabInfo = MainActivityController.getInstance().findTabInfoByGid(gid);
                CacheManager.getInstance(context).cacheInfo(gid, path, result, tabInfo);
            }
        });

        Observable.concat(cache, network).first("")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        if (TextUtils.isEmpty(result)) {
                            JtsViewerLog.e(TAG,"error getting gtp tab");
                            return;
                        }
                        JtsShowGtpTabAction action = new JtsShowGtpTabAction(context, path + File.separator + result);
                        action.processAction();
                    }
                });
    }


}
