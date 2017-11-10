package com.axlecho.jtsviewer.action.tab;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

public class JtsParseTabTypeAction extends JtsBaseAction {
    private static final int TAB_TYPE_IMAGE = 1;
    private static final int TAB_TYPE_GTP = 2;

    private static final String TAG = JtsParseTabTypeAction.class.getSimpleName();
    public static final String GID_KEY = "tab_action_gid";

    @Override
    public void processAction() {
        String webpageContent = (String) getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY);
        int type = praseTabType(webpageContent);

        JtsViewerLog.i(TAG, "tab type " + type);
        if (type == TAB_TYPE_GTP) {
            JtsGetGtpTabAction action = new JtsGetGtpTabAction();
            action.setKey(CONTEXT_KEY, getKey(CONTEXT_KEY));
            action.setKey(GID_KEY, getKey(GID_KEY));
            action.setKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY, getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY));
            action.processAction();
        } else if (type == TAB_TYPE_IMAGE) {
            JtsGetImageTabAction action = new JtsGetImageTabAction();
            action.setKey(CONTEXT_KEY, getKey(CONTEXT_KEY));
            action.setKey(GID_KEY, getKey(GID_KEY));
            action.setKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY, getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY));
            action.processAction();
        }
    }

    private int praseTabType(String webpageContent) {
        if (webpageContent.contains("gtp_download")) {
            return TAB_TYPE_GTP;
        } else {
            return TAB_TYPE_IMAGE;
        }
    }
}
