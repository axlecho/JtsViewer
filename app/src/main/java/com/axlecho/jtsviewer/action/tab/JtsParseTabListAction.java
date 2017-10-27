package com.axlecho.jtsviewer.action.tab;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.activity.main.MainActivityController;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.network.JtsPageParser;

import java.util.List;

public class JtsParseTabListAction extends JtsBaseAction {

    public static final String SRC_FROM_KEY = "tablist_form";
    public static final String SRC_FROM_SEARCH = "tablist_from_search";
    public static final String SRC_FROM_DIALY = "tablist_from_daily";
    public static final String CLEAR_FLAG = "clear-flag";

    private static final String TAG = JtsParseTabListAction.class.getSimpleName();

    @Override
    public void execute() {
        Context context = (Context) getKey(JtsBaseAction.CONTEXT_KEY);
        String srcFrom = (String) getKey(SRC_FROM_KEY);
        String html = (String) getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY);
        Boolean clearFlag = (Boolean) getKey(CLEAR_FLAG);
        if(clearFlag == null) {
            clearFlag = true;
        }
        JtsPageParser.getInstance(context).setContent(html);
        List<JtsTabInfoModel> content = JtsPageParser.getInstance(context).parserTabList(srcFrom);
        MainActivityController.getInstance().processShowHome(content, clearFlag);
    }
}
