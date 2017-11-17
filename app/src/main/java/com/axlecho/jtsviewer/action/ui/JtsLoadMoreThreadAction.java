package com.axlecho.jtsviewer.action.ui;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.activity.JtsDetailActivityController;
import com.axlecho.jtsviewer.module.JtsThreadModule;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.network.JtsPageParser;

import java.util.List;

/**
 * Created by Administrator on 2017/11/17.
 */

public class JtsLoadMoreThreadAction extends JtsBaseAction {


    @Override
    public void processAction() {
        Context context = (Context) getKey(JtsBaseAction.CONTEXT_KEY);
        String html = (String) getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY);
        JtsPageParser.getInstance(context).setContent(html);
        List<JtsThreadModule> data = JtsPageParser.getInstance(context).parserThread();
        JtsDetailActivityController.getInstance().processLoadMoreThread(data);
    }
}
