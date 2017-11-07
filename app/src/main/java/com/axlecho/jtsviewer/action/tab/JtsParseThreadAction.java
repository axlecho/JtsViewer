package com.axlecho.jtsviewer.action.tab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.activity.JtsDetailActivity;
import com.axlecho.jtsviewer.activity.JtsDetailActivityController;
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

    public JtsParseThreadAction(Context context) {
        this.context = context;
    }

    @Override
    public void execute() {
        JtsViewerLog.d(JtsViewerLog.TRACE_MODULE, TAG, TAG);
        this.html = (String) getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY);

        JtsPageParser.getInstance(context).setContent(html);

        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<JtsThreadModule> threadList = JtsPageParser.getInstance(context).parserThread();
                    JtsDetailActivityController.getInstance().processThreadData(threadList);
                }
            });
        }

    }
}
