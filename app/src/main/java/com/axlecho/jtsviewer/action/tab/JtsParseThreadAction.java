package com.axlecho.jtsviewer.action.tab;

import android.content.Context;
import android.content.Intent;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.module.JtsThreadModule;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.network.JtsPageParser;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.util.List;

/**
 * Created by Administrator on 2017/11/1.
 */

public class JtsParseThreadAction extends JtsBaseAction {
    private static final String TAG = "parse-thread-action";
    private Context context;
    private String html;

    @Override
    public void execute() {
        JtsViewerLog.d(JtsViewerLog.TRACE_MODULE, TAG, TAG);
        this.context = (Context) getKey(CONTEXT_KEY);
        this.html = (String) getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY);

        JtsPageParser.getInstance(context).setContent(html);
        List<JtsThreadModule> threadList = JtsPageParser.getInstance(context).parserThread();
    }
}
