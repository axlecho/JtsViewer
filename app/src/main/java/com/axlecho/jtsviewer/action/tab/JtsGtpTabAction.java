package com.axlecho.jtsviewer.action.tab;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.ui.JtsShowGtpTabAction;
import com.axlecho.jtsviewer.cache.CacheManager;
import com.axlecho.jtsviewer.module.CacheModule;
import com.axlecho.jtsviewer.network.JtsServer;

import java.io.File;

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
                if (module != null) {
                    e.onNext(module.path + File.separator + module.fileName);
                }
                e.onComplete();
            }
        });

        String path = CacheManager.getInstance(context).getCachePath() + File.separator + gid;
        Observable<String> network = JtsServer.getSingleton(context).download(gtpUrl, path);

        Observable.concat(cache, network).take(1)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        JtsShowGtpTabAction action = new JtsShowGtpTabAction(context, result);
                        action.processAction();
                    }
                });
    }
}
