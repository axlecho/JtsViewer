package com.axlecho.jtsviewer.network;

import android.content.Context;

import com.axlecho.jtsviewer.action.tab.JtsImageTabAction;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JtsServerApi {
    private static final String TAG = JtsServerApi.class.getSimpleName();


    public static void getTabImageUrl(final Context context, String url) {
        JtsImageTabAction action = new JtsImageTabAction();
        action.setKey(JtsImageTabAction.CONTEXT_KEY, context);
        action.setKey(JtsImageTabAction.GID_KEY, JtsTextUnitls.getTabKeyFromUrl(url));
        JtsNetworkManager.getInstance().get(url, action);
    }

    public static void checkLogin() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(JtsConf.HOST_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ret = response.body().string();

                JtsViewerLog.i(TAG, ret);
            }
        });
    }


}
