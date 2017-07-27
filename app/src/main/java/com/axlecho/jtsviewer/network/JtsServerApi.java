package com.axlecho.jtsviewer.network;

import android.content.Context;

import com.axlecho.jtsviewer.action.tab.JtsGtpTabAction;
import com.axlecho.jtsviewer.action.tab.JtsImageTabAction;
import com.axlecho.jtsviewer.action.tab.JtsTabAction;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;

public class JtsServerApi {
    private static final String TAG = JtsServerApi.class.getSimpleName();

    public static void getTab(final Context context, String url) {
        JtsTabAction action = new JtsTabAction();
        action.setKey(JtsTabAction.CONTEXT_KEY, context);
        action.setKey(JtsTabAction.GID_KEY, JtsTextUnitls.getTabKeyFromUrl(url));
        JtsNetworkManager.getInstance(context).get(url, action);
    }

    public static void checkLogin() {
    }

}
