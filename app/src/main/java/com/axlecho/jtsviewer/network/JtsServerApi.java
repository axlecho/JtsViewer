package com.axlecho.jtsviewer.network;

import android.content.Context;
import android.net.Uri;

import com.axlecho.jtsviewer.activity.MainActivityController;
import com.axlecho.jtsviewer.action.tab.JtsGetTabAction;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

public class JtsServerApi {

    private static final String TAG = JtsServerApi.class.getSimpleName();

    private static JtsServerApi instace;
    private Context context;

    private JtsServerApi(Context context) {
        this.context = context;
    }

    public static JtsServerApi getInstance(Context context) {
        if (instace == null) {
            instace = new JtsServerApi(context);
        }
        return instace;
    }

    public void checkLogin() {
    }

    public void processUrlChange(final String url) {
        if (url == null) {
            JtsViewerLog.e(TAG, "process url change failed -- url is null");
            return;
        }

        if (Uri.parse(url).getPath().contains("/tab/")) {
            JtsGetTabAction action = new JtsGetTabAction();
            action.setKey(JtsGetTabAction.CONTEXT_KEY, context);
            action.setKey(JtsGetTabAction.URL_KEY, url);
            MainActivityController.getInstance().enableFloatingActionButton(action);
        } else {
            MainActivityController.getInstance().disableFloatingActionButton();
        }
    }
}
