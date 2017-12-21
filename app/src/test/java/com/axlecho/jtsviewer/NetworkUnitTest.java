package com.axlecho.jtsviewer;

import android.content.Context;

import com.axlecho.jtsviewer.action.parser.JtsParseTabListFunction;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsServerApi;
import com.axlecho.jtsviewer.untils.JtsConf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by axlecho on 17-12-20.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)


public class NetworkUnitTest {
    private JtsServerApi service;
    private Context context;

    @Before
    public void setup() {
        // mMockClient = mock(Client.class);
        // ShadowLog.stream = System.out;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HttpLoggingInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(JtsConf.DESKTOP_HOST_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(JtsServerApi.class);
        context = RuntimeEnvironment.application.getApplicationContext();
    }

    @Test
    public void testNewTab() {
        service.getNewTab(String.valueOf(1)).map(new JtsParseTabListFunction(context))
                .subscribe(new Consumer<List<JtsTabInfoModel>>() {
                    @Override
                    public void accept(List<JtsTabInfoModel> list) throws Exception {
                        System.out.println(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // System.out.println(throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });
    }
}
