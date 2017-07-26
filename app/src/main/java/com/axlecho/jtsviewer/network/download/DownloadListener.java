package com.axlecho.jtsviewer.network.download;

public interface DownloadListener {
    void onStart(long size);

    void onFinish(String result);

    void onError(String msg);

    void onProgress(long progress);
}
