package com.axlecho.jtsviewer.action.tab;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.download.DownloadAction;
import com.axlecho.jtsviewer.activity.main.MainActivityController;
import com.axlecho.jtsviewer.network.JtsConf;
import com.axlecho.jtsviewer.network.JtsCookieManager;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.network.download.DownloadListener;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.util.List;


public class JtsGetGtpTabAction extends JtsBaseAction implements DownloadListener {

    private static final String GTP_PATTERN = "dlink=\"/forum.php\\?mod=attachment.*?\"";
    private static final String TAG = JtsGetGtpTabAction.class.getSimpleName();


    @Override
    public void execute() {
        String webpageContent = (String) getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY);
        Context context = (Context) getKey(CONTEXT_KEY);
        long gid = (long) getKey(JtsParseTabTypeAction.GID_KEY);

        JtsViewerLog.appendToFile(context, webpageContent);
        JtsViewerLog.d(TAG, "gid " + gid);

        if (webpageContent == null) {
            JtsViewerLog.e(TAG, "execute failed - webpage content is null");
            return;
        }

        if (context == null) {
            JtsViewerLog.e(TAG, "execute failed - context is null");
            return;
        }
//        if (JtsCookieManager.getInstance(context).getCookie().equals("") ||
//                JtsCookieManager.getInstance(context).getCookie() == null) {
//            MainActivityController.getInstance().processShowLogin();
//            return;
//        }

        List<String> gtpUrls = JtsTextUnitls.findByPattern(webpageContent, GTP_PATTERN);
        JtsViewerLog.i(TAG, gtpUrls.toString());

        String gtpUrl = gtpUrls.get(0);
        gtpUrl = gtpUrl.replaceAll("dlink=\"", "");
        gtpUrl = gtpUrl.replaceAll("\"$", "");
        gtpUrl = gtpUrl.replaceAll("amp;", "");
        gtpUrl = JtsConf.HOST_URL + gtpUrl;

        download(gtpUrl);
    }

    private void download(String url) {
        JtsViewerLog.d(TAG, url);

        DownloadAction action = new DownloadAction();
        action.setKey(CONTEXT_KEY, getKey(CONTEXT_KEY));
        action.setKey(DownloadAction.URL_KEY, url);
        action.setKey(JtsParseTabTypeAction.GID_KEY, getKey(JtsParseTabTypeAction.GID_KEY));
        action.setDownloadHandler(this);
        action.execute();
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
        JtsShowGtpTabAction action = new JtsShowGtpTabAction();
        action.setKey(CONTEXT_KEY, getKey(CONTEXT_KEY));
        action.setKey(JtsShowGtpTabAction.GTP_FILE_PATH, result);
        action.execute();
    }
}
