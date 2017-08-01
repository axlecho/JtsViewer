package com.axlecho.jtsviewer.network.download;

import android.content.Context;
import android.os.AsyncTask;

import com.axlecho.jtsviewer.cache.CacheManager;
import com.axlecho.jtsviewer.module.CacheModule;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadTask extends AsyncTask<Void, Long, String> {
    private static final String TAG = "DownloadTask";

    private String mPath;
    private String mUrl;
    private Context mContext;
    private long gid;

    private List<DownloadListener> mListeners = new ArrayList<>();

    public DownloadTask(Context context, String url, long gid) {
        mUrl = url;
        mContext = context;
        mPath = CacheManager.getInstance(context).getCachePath();
        this.gid = gid;
    }

    public void setDownloadListener(DownloadListener listener) {
        if (listener == null) {
            JtsViewerLog.e(TAG, "[setDownloadListener] listener is null");
            return;
        }
        mListeners.add(listener);
    }

    public void removeDownloadListener(DownloadListener listener) {
        mListeners.remove(listener);
    }

    public void cancel() {

    }

    @Override
    protected void onPreExecute() {
        for (DownloadListener listener : mListeners) {
            listener.onStart(-1);
        }

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            return JtsNetworkManager.getInstance(mContext).download(mUrl, mPath);
        } catch (Exception e) {
            e.printStackTrace();
            cancel(true);
            return e.getMessage();
        }
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        long progress = values[0];
        if (progress < 0) {
            for (DownloadListener listener : mListeners) {
                listener.onStart(-progress);
            }
        } else {
            for (DownloadListener listener : mListeners) {
                listener.onProgress(progress);
            }
        }
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        for (DownloadListener listener : mListeners) {
            listener.onFinish(mPath + File.separator + result);
        }
        this.cacheInfo(result);
        super.onPostExecute(result);
    }

    @Override
    protected void onCancelled(String msg) {
        for (DownloadListener listener : mListeners) {
            listener.onError(msg);
        }
        super.onCancelled(msg);
    }

    public void cacheInfo(String fileName) {
        CacheModule cacheInfo = new CacheModule();
        cacheInfo.path = mPath;
        cacheInfo.fileName = fileName;
        cacheInfo.gid = String.valueOf(gid);
        cacheInfo.type = "gtp";
        try {
            cacheInfo.writeToFile();
        } catch (IOException e) {
            JtsViewerLog.e(TAG, "cache info failed " + e.getMessage());
        }
    }
}
