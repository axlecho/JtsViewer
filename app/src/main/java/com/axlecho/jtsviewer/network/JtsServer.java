package com.axlecho.jtsviewer.network;

import android.content.Context;

import com.axlecho.jtsviewer.action.parser.JtsParseTabDetailFunction;
import com.axlecho.jtsviewer.action.parser.JtsParseTabListFunction;
import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsConf;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class JtsServer {
    String TAG = JtsServer.class.getSimpleName();
    private volatile static JtsServer singleton;
    private JtsServerApi service;
    private Context context;

    private JtsServer(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addNetworkInterceptor(logging);


        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(JtsConf.DESKTOP_HOST_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(JtsServerApi.class);
        this.context = context.getApplicationContext();
    }

    public static JtsServer getSingleton(Context context) {
        if (singleton == null) {
            synchronized (JtsServer.class) {
                if (singleton == null) {
                    singleton = new JtsServer(context);
                }
            }
        }
        return singleton;
    }

    public Observable<JtsTabDetailModule> getDetail(int id) {
        return service.getDetail(id).map(new JtsParseTabDetailFunction(context));
    }

    public Observable<List<JtsTabInfoModel>> getNewTab(int page) {
        return service.getNewTab(page).map(new JtsParseTabListFunction(context));
    }

    public Observable<List<JtsTabInfoModel>> getHotTab(int page) {
        return service.getHotTab(page).map(new JtsParseTabListFunction(context));
    }

    public Observable<List<JtsTabInfoModel>> getArtist(int id) {
        return service.getArtist(id).map(new JtsParseTabListFunction(context));
    }

}
