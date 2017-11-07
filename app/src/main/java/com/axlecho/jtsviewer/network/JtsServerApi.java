package com.axlecho.jtsviewer.network;

import android.content.Context;

public class JtsServerApi {

    private static final String TAG = JtsServerApi.class.getSimpleName();
    private static final String TAB_URL_PATTERN = "/tab/\\d+/";
    private static JtsServerApi instance;
    private Context context;

    private JtsServerApi(Context context) {
        this.context = context;
    }

    public static JtsServerApi getInstance(Context context) {
        if (instance == null) {
            synchronized (JtsServerApi.class) {
                if (instance == null) {
                    instance = new JtsServerApi(context);
                }
            }
        }
        return instance;
    }

    public void checkLogin() {
    }
}
