package com.axlecho.jtsviewer.action.tab;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.download.DownloadAction;
import com.axlecho.jtsviewer.network.JtsConf;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.network.download.DownloadListener;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import org.herac.tuxguitar.android.activity.TGActivity;

import java.util.List;

public class JtsGtpTabAction extends JtsBaseAction {

    private static final String GTP_PATTERN = "dlink=\"/forum.php\\?mod=attachment.*?\"";
    private static final String TAG = JtsGtpTabAction.class.getSimpleName();


    @Override
    public void execute() {
        String webpageContent = (String) getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY);
        Context context = (Context) getKey(JtsTabAction.CONTEXT_KEY);
        long gid = (long) getKey(JtsTabAction.GID_KEY);

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
        action.setKey(DownloadAction.CONTEXT_KEY, getKey(JtsTabAction.CONTEXT_KEY));
        action.setKey(DownloadAction.URL_KEY, url);
        action.setDownloadHandler(new DownloadListener() {
            @Override
            public void onStart(long size) {
                JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[download] size " + size);
            }

            @Override
            public void onFinish(String result) {
                JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[download] result " + result);
                showGtp(result);
            }

            @Override
            public void onError(String msg) {
                JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[download] error " + msg);
            }

            @Override
            public void onProgress(long progress) {
                JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[download] progress " + progress);
            }
        });
        action.execute();
    }

    private void showGtp(String filePath) {
        Context context = (Context) getKey(JtsTabAction.CONTEXT_KEY);
        Uri gtpUri = Uri.parse("file://" + filePath);
        Intent intent = new Intent();
        intent.setData(gtpUri);

        Bundle bundle = new Bundle();
        bundle.putSerializable("title", "test");
        intent.putExtras(bundle);

        intent.setAction(Intent.ACTION_VIEW);
        intent.setClass(context, TGActivity.class);
        context.startActivity(intent);
    }
}
