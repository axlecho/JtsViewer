package com.axlecho.jtsviewer.action.user;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.activity.main.MainActivityController;
import com.axlecho.jtsviewer.network.JtsConf;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class JtsLoginAction extends JtsBaseAction {
    private static final String TAG = JtsLoginAction.class.getSimpleName();
    public static final String USERNAME_KEY = "username";
    public static final String KEYWORD_KEY = "keyword";

    @Override
    public void processAction() {
        Context context = (Context) getKey(JtsBaseAction.CONTEXT_KEY);
        String username = (String) getKey(USERNAME_KEY);
        String keyword = (String) getKey(KEYWORD_KEY);

        JtsViewerLog.d(TAG, "username " + username + " keyword " + keyword);

        RequestBody data = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", keyword)
                .addFormDataPart("quickforward", "yes")
                .addFormDataPart("handlekey", "1s")
                .build();

        JtsNetworkManager.getInstance(context).post(JtsConf.DESKTOP_LOGIN_URL, data, null);
    }
}
