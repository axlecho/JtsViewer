package com.axlecho.jtsviewer.network;

import android.content.Context;
import android.net.Uri;

import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;


public class JtsCookieJar implements CookieJar {
    private static final String TAG = JtsCookieJar.class.getSimpleName();
    private Context context;

    public JtsCookieJar(Context context) {
        this.context = context;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[saveFromResponse]" + cookies.toString());
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
//        List<Cookie> cookies = new ArrayList<>();
//        String cookieStr = JtsCookieManager.getInstance(context).getCookie();
//        if (cookieStr == null) {
//            JtsViewerLog.e(TAG, "no cookie");
//            return cookies;
//        }
//        for (String s : cookieStr.split(";")) {
//            Cookie c = new Cookie.Builder()
//                    .domain("jitashe.org")
//                    .path("/")
//                    .name(s.split("=")[0].trim())
//                    .value(s.split("=")[1].trim())
//                    .build();
//            cookies.add(c);
//        }
//
//        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "loadForRequest" + cookies.toString());
        return null;
    }
}
