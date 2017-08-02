package com.axlecho.jtsviewer.action.user;

import android.app.Activity;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.activity.main.MainActivityController;
import com.axlecho.jtsviewer.module.UserModule;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;

public class JtsParseUserInfoAction extends JtsBaseAction {

    private String UID_URL_PATTERN = "(?<=discuz_uid = ')\\d+";
    private String USER_NAME_PATTERN = "(?<=<a href=\"#\"><i class=\"user icon\"></i>).*?(?=</a>)";
    private String USER_IMAGE_PREFIX = "http://www.jitashe.org/uc_server/avatar.php?uid=";

    @Override
    public void execute() {
        String webpageContent = (String) getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY);
        UserModule user = new UserModule();
        String uidStr = JtsTextUnitls.findByPatternOnce(webpageContent, UID_URL_PATTERN);
        if (uidStr != null) {
            user.uid = Long.parseLong(uidStr);
        } else {
            user.uid = -1;
        }
        user.userName = JtsTextUnitls.findByPatternOnce(webpageContent, USER_NAME_PATTERN);
        if (user.uid != -1) {
            user.avatarUrl = USER_IMAGE_PREFIX + user.uid;
        }

        MainActivityController.getInstance().processLoadUserInfo(user);
    }
}
