package com.axlecho.jtsviewer.network;

import android.content.Context;

import com.axlecho.jtsviewer.action.parser.JtsParseCommentFunction;
import com.axlecho.jtsviewer.action.parser.JtsParseLoginFunction;
import com.axlecho.jtsviewer.action.parser.JtsParseSearchKeyConsumer;
import com.axlecho.jtsviewer.action.parser.JtsParseTabDetailFunction;
import com.axlecho.jtsviewer.action.parser.JtsParseTabListFunction;
import com.axlecho.jtsviewer.action.parser.JtsParseThreadFunction;
import com.axlecho.jtsviewer.action.parser.JtsParseUserInfoFunction;
import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.module.JtsThreadModule;
import com.axlecho.jtsviewer.module.JtsUserModule;
import com.axlecho.jtsviewer.module.JtsVersionInfoModule;
import com.axlecho.jtsviewer.untils.JtsConf;
import com.axlecho.jtsviewer.untils.JtsToolUnitls;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class JtsServer {
    private static final String TAG = JtsServer.class.getSimpleName();
    private static final int TIME_OUT = 100;
    private volatile static JtsServer singleton;
    private JtsServerApi service;
    private JtsGithubApi github;
    private Context context;
    private JtsSchedulers schedulers;


    private JtsServer(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addNetworkInterceptor(logging);
        builder.cookieJar(new JtsCookieJar(context));
        builder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("user-agent", JtsServerApi.USER_AGENT)
                        .build();
                return chain.proceed(newRequest);
            }
        };
        builder.addInterceptor(headerInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(JtsConf.DESKTOP_HOST_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        this.service = retrofit.create(JtsServerApi.class);

        Retrofit githubRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(JtsConf.GITHUB_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.github = githubRetrofit.create(JtsGithubApi.class);

        this.context = context.getApplicationContext();
        this.schedulers = new JtsSchedulers();
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

    public Observable<JtsTabDetailModule> getDetail(long id) {
        Observable<JtsTabDetailModule> o = service.getDetail(id, 1).map(new JtsParseTabDetailFunction(context));
        return schedulers.switchSchedulers(o);
    }

    public Observable<List<JtsThreadModule>> getThread(long id, int page) {
        Observable<List<JtsThreadModule>> o = service.getDetail(id, page).map(new JtsParseThreadFunction(context));
        return schedulers.switchSchedulers(o);
    }

    public Observable<List<JtsTabInfoModel>> getNewTab(int page) {
        Observable<List<JtsTabInfoModel>> o = service.getNewTab(page).map(new JtsParseTabListFunction(context));
        return schedulers.switchSchedulers(o);
    }

    public Observable<List<JtsTabInfoModel>> getHotTab(int page) {
        Observable<List<JtsTabInfoModel>> o = service.getHotTab(page).map(new JtsParseTabListFunction(context));
        return schedulers.switchSchedulers(o);
    }

    public Observable<List<JtsTabInfoModel>> getArtist(int id, int page) {
        Observable<List<JtsTabInfoModel>> o = service.getArtist(id, page).map(new JtsParseTabListFunction(context));
        return schedulers.switchSchedulers(o);
    }

    public Observable<List<JtsTabInfoModel>> search(String keyword, int page) {
        Observable<List<JtsTabInfoModel>> o;
        int searchKey = JtsSearchHelper.getSingleton().getSearchKey(keyword);
        if (searchKey == -1) {
            o = service.search(keyword).doOnNext(new JtsParseSearchKeyConsumer(context, keyword)).map(new JtsParseTabListFunction(context));
        } else {
            o = service.searchById(searchKey, page).map(new JtsParseTabListFunction(context));
        }
        return schedulers.switchSchedulers(o);
    }

    public Observable<String> login(String username, String password) {
        String hash = "6b3db232";
        String referer = JtsConf.DESKTOP_HOST_URL;
        long cookietime = 2592000;
        Observable<String> o = service.login(hash, referer, username, password, cookietime).map(new JtsParseLoginFunction());
        return schedulers.switchSchedulers(o);
    }

    public Observable<JtsUserModule> getUserInfo() {
        Observable<JtsUserModule> o = service.index().map(new JtsParseUserInfoFunction(context));
        return schedulers.switchSchedulers(o);
    }

    public Observable<String> postComment(int fid, long tid, String message, String formhash) {
        Observable<String> o = service.postComment(fid, tid, message, System.currentTimeMillis(), formhash, 1, "")
                .map(new JtsParseCommentFunction());

        return schedulers.switchSchedulers(o);
    }

    public Observable<JtsVersionInfoModule> getLastVersionInfo() {
        Observable<JtsVersionInfoModule> o = github.getLastVersion();
        return schedulers.switchSchedulers(o);
    }

    public void setSchedulers(JtsSchedulers schedulers) {
        this.schedulers = schedulers;
    }
}
