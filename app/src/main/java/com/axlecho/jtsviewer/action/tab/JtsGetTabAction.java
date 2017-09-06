package com.axlecho.jtsviewer.action.tab;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.network.JtsConf;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;

public class JtsGetTabAction extends JtsBaseAction {
    public static final String URL_KEY = "get_tab_url";

    @Override
    public void execute() {
        Context context = (Context) getKey(CONTEXT_KEY);
        String url = (String) getKey(URL_KEY);

        JtsParseTabTypeAction action = new JtsParseTabTypeAction();
        action.setKey(CONTEXT_KEY, context);
        action.setKey(JtsParseTabTypeAction.GID_KEY, JtsTextUnitls.getTabKeyFromUrl(url));
        JtsNetworkManager.getInstance(context).get(JtsConf.HOST_URL + url, action);
    }
}
