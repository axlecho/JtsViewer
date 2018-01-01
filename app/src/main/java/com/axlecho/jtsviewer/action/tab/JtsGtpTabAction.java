package com.axlecho.jtsviewer.action.tab;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.download.DownloadAction;
import com.axlecho.jtsviewer.action.ui.JtsShowGtpTabAction;
import com.axlecho.jtsviewer.network.download.DownloadListener;
import com.axlecho.jtsviewer.untils.JtsViewerLog;


public class JtsGtpTabAction extends JtsBaseAction implements DownloadListener {

    private static final String TAG = JtsGtpTabAction.class.getSimpleName();
    private Context context;
    private long gid;
    private String gtpUrl;

    public JtsGtpTabAction(Context context, long gid, String gtpUrl) {
        this.context = context;
        this.gid = gid;
        this.gtpUrl = gtpUrl;
    }

    @Override
    public void processAction() {
        DownloadAction action = new DownloadAction(context, gtpUrl, gid);
        action.setDownloadHandler(this);
        action.processAction();
    }


    @Override
    public void onStart(long size) {
        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[download] size " + size);
    }

    @Override
    public void onFinish(String result) {
        this.processDownloadFinish(result);
    }

    @Override
    public void onError(String msg) {
        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[download] error " + msg);
    }

    @Override
    public void onProgress(long progress) {
        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[download] progress " + progress);
    }

    public void processDownloadFinish(String result) {
        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[download] result " + result);
        JtsShowGtpTabAction action = new JtsShowGtpTabAction(context, result);
        action.processAction();
    }
}
