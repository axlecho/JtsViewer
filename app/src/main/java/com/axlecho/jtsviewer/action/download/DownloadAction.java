package com.axlecho.jtsviewer.action.download;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.ui.JtsShowGtpTabAction;
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


    public DownloadAction(Context context, String url, long gid) {
        this.context = context;
        this.url = url;
        this.gid = gid;
    }

    @Override
    public void processAction() {

        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[download url] " + url);
        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[download gid] " + gid);
        CacheModule module = CacheManager.getInstance(context).getModule(gid);

        if (module != null) {
            JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "load from cache");
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
        JtsShowGtpTabAction action = new JtsShowGtpTabAction(context,
                module.path + File.separator + module.fileName);
        action.executeOnUiThread();
    }
}
