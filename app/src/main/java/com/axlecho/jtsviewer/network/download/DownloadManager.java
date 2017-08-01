package com.axlecho.jtsviewer.network.download;

import android.content.Context;

import com.axlecho.jtsviewer.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;

public class DownloadManager {
    private final String cachePath;
    private List<DownloadTask> mDownloadQueue = new ArrayList<>();

    private static DownloadManager mInstance;

    private DownloadManager(Context context) {
        cachePath = CacheManager.getInstance(context).getCachePath();
    }

    public static DownloadManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DownloadManager.class) {
                if (mInstance == null) {
                    mInstance = new DownloadManager(context);
                }
            }
        }
        return mInstance;
    }

    public int executeTask(DownloadTask task) {
        mDownloadQueue.add(task);
        task.execute();
        return 0;
    }

}
