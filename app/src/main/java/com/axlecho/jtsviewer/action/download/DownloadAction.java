package com.axlecho.jtsviewer.action.download;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.tab.JtsParseTabAction;
import com.axlecho.jtsviewer.action.tab.JtsShowGtpTabAction;
import com.axlecho.jtsviewer.cache.CacheManager;
import com.axlecho.jtsviewer.module.CacheModule;
import com.axlecho.jtsviewer.network.download.DownloadListener;
import com.axlecho.jtsviewer.network.download.DownloadManager;
import com.axlecho.jtsviewer.network.download.DownloadTask;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.io.File;


public class DownloadAction extends JtsBaseAction {
    private static final String TAG = DownloadAction.class.getSimpleName();
    public static final String URL_KEY = "download_action_webpage_content";

    private Context context;
    private String url;
    private long gid;

    private DownloadTask downloadTask;


    public DownloadAction() {

    }

    @Override
    public void execute() {
        this.context = (Context) getKey(CONTEXT_KEY);
        this.url = (String) getKey(URL_KEY);
        this.gid = (long) getKey(JtsParseTabAction.GID_KEY);

        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[download url] " + url);
        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[download gid] " + gid);
        CacheModule module = CacheManager.getInstance(context).getModule(gid);

        if (module != null) {
            JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE,TAG,"load from cache");
            processShowGtpFromCache(module);
            return;
        }

        downloadTask = new DownloadTask(context, url, gid);
        downloadTask.setDownloadListener(handler);
        DownloadManager.getInstance(context).executeTask(downloadTask);
    }

    private DownloadListener handler;

    public void setDownloadHandler(DownloadListener handler) {
        this.handler = handler;
    }

    private void processShowGtpFromCache(CacheModule module) {
        JtsShowGtpTabAction action = new JtsShowGtpTabAction();
        action.setKey(JtsBaseAction.CONTEXT_KEY, getKey(JtsBaseAction.CONTEXT_KEY));
        action.setKey(JtsShowGtpTabAction.GTP_FILE_PATH, module.path + File.separator + module.fileName);
        action.execute();
    }
}
