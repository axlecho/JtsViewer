package com.axlecho.jtsviewer.network;

import android.content.Context;

import com.axlecho.jtsviewer.action.parser.JtsParseTabDetailFunction;
import com.axlecho.jtsviewer.action.parser.JtsParseTabListFunction;
import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsConf;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class JtsServer {
    String TAG = JtsServer.class.getSimpleName();
    private volatile static JtsServer singleton;
    private JtsServerApi service;
    private Context context;

    private int searchKey;
    private String keyword;

    private JtsServer(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addNetworkInterceptor(logging);

        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
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
        return service.getDetail(id).map(new JtsParseTabDetailFunction(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<JtsTabInfoModel>> getNewTab(int page) {
        return service.getNewTab(page).map(new JtsParseTabListFunction(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<JtsTabInfoModel>> getHotTab(int page) {
        return service.getHotTab(page).map(new JtsParseTabListFunction(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<JtsTabInfoModel>> getArtist(int id) {
        return service.getArtist(id).map(new JtsParseTabListFunction(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<JtsTabInfoModel>> search(String keyword, int page) {
        if (this.keyword == null || !this.keyword.equals(keyword) || page == 1 || searchKey == 0) {
            this.keyword = keyword;
            return service.search(keyword).doOnNext(saveSearchKeyConsumer).map(new JtsParseTabListFunction(context))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            return service.searchById(searchKey, page).map(new JtsParseTabListFunction(context))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public int getSearchKey() {
        return searchKey;
    }

    private Consumer<ResponseBody> saveSearchKeyConsumer = new Consumer<ResponseBody>() {
        @Override
        public void accept(ResponseBody responseBody) throws Exception {
            long contentLength = responseBody.contentLength();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();

            if (contentLength != 0) {
                Charset charset = Charset.forName("Utf-8");
                String html = buffer.clone().readString(charset);
                JtsPageParser.getInstance(context).setContent(html);
                searchKey = JtsPageParser.getInstance(context).parserSearchId();
            }
        }
    };
}
