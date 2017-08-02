package com.axlecho.jtsviewer.network.download;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class DownloadManager {
    private List<DownloadTask> downloadQueue = new ArrayList<>();

    private static DownloadManager instance;

    private DownloadManager(Context context) {
    }

    public static DownloadManager getInstance(Context context) {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager(context);
                }
            }
        }
        return instance;
    }

    public int executeTask(DownloadTask task) {
        downloadQueue.add(task);
        task.execute();
        return 0;
    }

}
