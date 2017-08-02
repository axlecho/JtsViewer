package com.axlecho.jtsviewer.network;

import android.content.Context;
import android.net.Uri;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.activity.main.MainActivityController;
import com.axlecho.jtsviewer.action.tab.JtsGetTabAction;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

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

    public void processUrlChange(final String url) {
        if (url == null) {
            JtsViewerLog.e(TAG, "process url change failed -- url is null");
            return;
        }

        if (Uri.parse(url).getPath().matches(TAB_URL_PATTERN)) {
            JtsGetTabAction action = new JtsGetTabAction();
            action.setKey(JtsBaseAction.CONTEXT_KEY, context);
            action.setKey(JtsGetTabAction.URL_KEY, url);
            MainActivityController.getInstance().enableFloatingActionButton(action);
        } else {
            MainActivityController.getInstance().disableFloatingActionButton();
        }
    }
}
