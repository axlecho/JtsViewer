package com.axlecho.jtsviewer.network;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JtsNetworkManager {
    public static final String WEBPAGE_CONTENT_KEY = "webpage_content";
    private static final String TAG = JtsNetworkManager.class.getSimpleName();
    private static JtsNetworkManager jtsNetworkManager;
    private OkHttpClient client;
    private Context context;

    private JtsNetworkManager(Context context) {
        this.context = context;
        client = new OkHttpClient.Builder()
                // .cookieJar(new JtsCookieJar(this.context))
                .addInterceptor(new LoggingInterceptor())
                .build();

    }

    public static JtsNetworkManager getInstance(Context context) {
        if (jtsNetworkManager == null) {
            jtsNetworkManager = new JtsNetworkManager(context);
        }
        return jtsNetworkManager;
    }

    public void get(String url, JtsBaseAction action) {
        Request request = new Request.Builder()
                .addHeader("Cookie", JtsCookieManager.getInstance(context).getCookie())
                .url(url)
                .build();

        client.newCall(request).enqueue(new JtsNetworkCallback(action));
    }


    private class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "Sending request " +
                    request.url() + " on " + request.headers());
            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }
}
