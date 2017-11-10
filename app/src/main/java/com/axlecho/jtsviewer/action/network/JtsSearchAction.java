package com.axlecho.jtsviewer.action.network;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.tab.JtsParseTabListAction;
import com.axlecho.jtsviewer.activity.main.MainActivityController;
import com.axlecho.jtsviewer.network.JtsConf;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

public class JtsSearchAction extends JtsBaseAction {
    private static final String TAG = JtsSearchAction.class.getSimpleName();
    public static final String SEARCH_CONTENT_KEY = "jts-search-content";

    @Override
    public void processAction() {
        String keyword = (String) getKey(SEARCH_CONTENT_KEY);
        Context context = (Context) getKey(JtsBaseAction.CONTEXT_KEY);

        JtsViewerLog.d(TAG, "search key:" + keyword);
        JtsParseTabListAction action = new JtsParseTabListAction();
        action.setKey(JtsBaseAction.CONTEXT_KEY, context);
        JtsNetworkManager.getInstance(context).get(JtsConf.DESKTOP_SREACH_URL + keyword, action);
    }
}
