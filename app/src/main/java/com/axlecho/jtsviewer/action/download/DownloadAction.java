package com.axlecho.jtsviewer.action.download;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.tab.JtsTabAction;
import com.axlecho.jtsviewer.network.download.DownloadListener;
import com.axlecho.jtsviewer.network.download.DownloadManager;
import com.axlecho.jtsviewer.network.download.DownloadTask;
import com.axlecho.jtsviewer.untils.JtsViewerLog;


public class DownloadAction extends JtsBaseAction {
    private static final String TAG = DownloadAction.class.getSimpleName();
    public static final String CONTEXT_KEY = "download_action_context";
    public static final String URL_KEY = "download_action_webpage_content";

    private Context context;
    private String url;
    private long gid;

    private DownloadTask mDownloadTask;


    public DownloadAction() {

    }

    @Override
    public void execute() {
        this.context = (Context) getKey(CONTEXT_KEY);
        this.url = (String) getKey(URL_KEY);
        this.gid = (long) getKey(JtsTabAction.GID_KEY);

        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[download url] " + url);
        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[download gid] " + gid);
        mDownloadTask = new DownloadTask(context, url, gid);
        mDownloadTask.setDownloadListener(handler);
        DownloadManager.getInstance(context).executeTask(mDownloadTask);
    }

    private DownloadListener handler;

    public void setDownloadHandler(DownloadListener handler) {
        this.handler = handler;
    }
}
