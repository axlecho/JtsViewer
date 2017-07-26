package com.axlecho.jtsviewer.action.tab;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.download.DownloadAction;
import com.axlecho.jtsviewer.network.JtsConf;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.util.List;

public class JtsGtpTabAction extends JtsBaseAction {

    private static final String GTP_PATTERN = "dlink=\"/forum.php\\?mod=attachment.*?\"";
    private static final String TAG = JtsGtpTabAction.class.getSimpleName();

    public static final String CONTEXT_KEY = "gtp_tab_action_context";
    public static final String GID_KEY = "gtp_tab_action_webpage_content";

    @Override
    public void execute() {
        String webpageContent = (String) getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY);
        Context context = (Context) getKey(CONTEXT_KEY);
        long gid = (long) getKey(GID_KEY);

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
        action.setKey(DownloadAction.CONTEXT_KEY, getKey(CONTEXT_KEY));
        action.setKey(DownloadAction.URL_KEY, url);
        action.execute();
    }
}
