package com.axlecho.jtsviewer.network;

import android.content.Context;

import com.axlecho.jtsviewer.action.tab.JtsGtpTabAction;
import com.axlecho.jtsviewer.action.tab.JtsImageTabAction;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;

public class JtsServerApi {
    private static final String TAG = JtsServerApi.class.getSimpleName();


    public static void getTabImageUrl(final Context context, String url) {
        JtsImageTabAction action = new JtsImageTabAction();
        action.setKey(JtsImageTabAction.CONTEXT_KEY, context);
        action.setKey(JtsImageTabAction.GID_KEY, JtsTextUnitls.getTabKeyFromUrl(url));
        JtsNetworkManager.getInstance(context).get(url, action);
    }

    public static void getGtp(final Context context, String url) {
        JtsGtpTabAction action = new JtsGtpTabAction();
        action.setKey(JtsGtpTabAction.CONTEXT_KEY, context);
        action.setKey(JtsGtpTabAction.GID_KEY, JtsTextUnitls.getTabKeyFromUrl(url));
        JtsNetworkManager.getInstance(context).get(url, action);
    }

    public static void checkLogin() {
    }


}
