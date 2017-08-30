package com.axlecho.jtsviewer.action;

import android.content.Context;

import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.network.JtsPageParser;

public class JtsParseHomePageAction extends JtsBaseAction {

    private static final String TAG = JtsParseHomePageAction.class.getSimpleName();

    @Override
    public void execute() {

        Context context = (Context) getKey(JtsBaseAction.CONTEXT_KEY);
        String html = (String) getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY);

        JtsPageParser.getInstance().setContent(html);
        JtsPageParser.getInstance().parserTabList();
    }
}
