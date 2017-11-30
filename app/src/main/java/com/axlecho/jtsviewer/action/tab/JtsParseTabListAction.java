package com.axlecho.jtsviewer.action.tab;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.ui.JtsRefreshAction;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.network.JtsPageParser;

import java.util.List;

public class JtsParseTabListAction extends JtsBaseAction {
    private static final String TAG = JtsParseTabListAction.class.getSimpleName();

    @Override
    public void processAction() {
        Context context = (Context) getKey(JtsBaseAction.CONTEXT_KEY);
        String html = (String) getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY);
        JtsPageParser.getInstance(context).setContent(html);
        List<JtsTabInfoModel> data = JtsPageParser.getInstance(context).parserTabList();
        int searchKey = JtsPageParser.getInstance(context).parserSearchId();
        this.processAfterAction(data,searchKey);

    }

    public void processAfterAction(List<JtsTabInfoModel> data,int search) {
        JtsBaseAction action = (JtsBaseAction) getKey("after_action");
        action.setKey(JtsRefreshAction.DATA_KEY, data);
        action.setKey(JtsRefreshAction.SEARCH_KEY,search);
        action.execute();
    }
}
