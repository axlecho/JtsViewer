package com.axlecho.jtsviewer.action;

import android.content.Context;

import com.axlecho.jtsviewer.activity.main.MainActivityController;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.network.JtsPageParser;

import java.util.List;

public class JtsParseHomePageAction extends JtsBaseAction {

    private static final String TAG = JtsParseHomePageAction.class.getSimpleName();

    @Override
    public void execute() {

        Context context = (Context) getKey(JtsBaseAction.CONTEXT_KEY);
        String html = (String) getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY);

        JtsPageParser.getInstance().setContent(html);
        List<JtsTabInfoModel> content = JtsPageParser.getInstance().parserTabList();
        MainActivityController.getInstance().processShowHome(content);
    }
}
