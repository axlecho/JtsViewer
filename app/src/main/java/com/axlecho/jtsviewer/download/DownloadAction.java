package com.axlecho.jtsviewer.download;

import android.content.Context;
import android.util.Log;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.network.download.DownloadListener;
import com.axlecho.jtsviewer.network.download.DownloadManager;
import com.axlecho.jtsviewer.network.download.DownloadTask;
import com.axlecho.jtsviewer.untils.JtsViewerLog;


public class DownloadAction extends JtsBaseAction {
    private static final String TAG = DownloadAction.class.getSimpleName();
    public static final String CONTEXT_KEY = "gtp_tab_action_context";
    public static final String URL_KEY = "gtp_tab_action_webpage_content";

    private Context mContext;
    private String mUrl;

    private DownloadTask mDownloadTask;


    public DownloadAction() {

    }

    @Override
    public void execute() {
        this.mContext = (Context) getKey(CONTEXT_KEY);
        this.mUrl = (String) getKey(URL_KEY);
        JtsViewerLog.d(TAG, "[download url] " + mUrl);
        mDownloadTask = new DownloadTask(mContext,mUrl);
        mDownloadTask.setDownloadListener(handler);
        DownloadManager.getInstance(mContext).executeTask(mDownloadTask);
    }

    private DownloadListener handler;

    public void setDownloadHandler(DownloadListener handler) {
        this.handler = handler;
    }
}
