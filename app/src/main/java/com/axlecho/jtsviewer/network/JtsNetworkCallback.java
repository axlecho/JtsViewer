package com.axlecho.jtsviewer.network;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class JtsNetworkCallback implements Callback {
    private static final String TAG = JtsNetworkCallback.class.getSimpleName();
    private JtsBaseAction action;

    public JtsNetworkCallback(JtsBaseAction action) {
        this.action = action;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        JtsViewerLog.d(TAG, "network error");
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String ret = response.body().string();
        if (action == null) {
            JtsViewerLog.e(TAG, "action is null");
            return;
        }
        action.setKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY, ret);
        action.execute();
    }


}
