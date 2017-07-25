package com.axlecho.jtsviewer.network;

import com.axlecho.jtsviewer.action.JtsBaseAction;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class JtsNetworkManager {

    public static final String WEBPAGE_CONTENT_KEY = "webpage_content";
    private static JtsNetworkManager jtsNetworkManager;
    private OkHttpClient client;

    private JtsNetworkManager() {
        client = new OkHttpClient();
    }

    static public JtsNetworkManager getInstance() {
        if (jtsNetworkManager == null) {
            jtsNetworkManager = new JtsNetworkManager();
        }
        return jtsNetworkManager;
    }

    public void get(String url, JtsBaseAction action) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new JtsNetworkCallback(action));
    }


}
