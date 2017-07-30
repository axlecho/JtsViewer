package com.axlecho.jtsviewer.action.tab;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

public class JtsTabAction extends JtsBaseAction {
    private static final int TAB_TYPE_IMAGE = 1;
    private static final int TAB_TYPE_GTP = 2;

    public static final String CONTEXT_KEY = "tab_action_context";
    public static final String GID_KEY = "tab_action_gid";
    public static final String TAG = JtsTabAction.class.getSimpleName();

    @Override
    public void execute() {
        String webpageContent = (String) getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY);
        int type = praseTabType(webpageContent);

        JtsViewerLog.i(TAG, "tab type " + type);
        if (type == TAB_TYPE_GTP) {
            JtsGtpTabAction action = new JtsGtpTabAction();
            action.setKey(CONTEXT_KEY, getKey(CONTEXT_KEY));
            action.setKey(GID_KEY, getKey(GID_KEY));
            action.setKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY, getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY));
            action.execute();
        } else if (type == TAB_TYPE_IMAGE) {
            JtsImageTabAction action = new JtsImageTabAction();
            action.setKey(CONTEXT_KEY, getKey(CONTEXT_KEY));
            action.setKey(GID_KEY, getKey(GID_KEY));
            action.setKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY, getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY));
            action.execute();
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
