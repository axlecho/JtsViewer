package com.axlecho.jtsviewer.action.network;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.activity.main.MainActivityController;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

public class JtsSearchAction extends JtsBaseAction {
    private static final String TAG = JtsSearchAction.class.getSimpleName();
    public static final String SEARCH_CONTENT_KEY = "jts-search-content";

    @Override
    public void execute() {
        String searchKey = (String) getKey(SEARCH_CONTENT_KEY);
        JtsViewerLog.d(TAG, "search key:" + searchKey);
        MainActivityController.getInstance().processSearch(searchKey);
    }
}
