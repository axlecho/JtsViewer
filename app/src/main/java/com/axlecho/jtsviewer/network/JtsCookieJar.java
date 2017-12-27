package com.axlecho.jtsviewer.network;

import android.content.Context;

import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class JtsCookieJar implements CookieJar {
    private static final String TAG = "cookie";
    private JtsPersistentCookieStore cookieStore;

    public JtsCookieJar(Context context) {
        cookieStore = new JtsPersistentCookieStore(context);
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

        for (Cookie cookie : cookies) {
            if (cookie.name().contains("auth")) {
                JtsViewerLog.d(TAG, "save for url " + url.toString());
                saveCookies(url, cookies);
                break;
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }

    private void saveCookies(HttpUrl url, List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            cookieStore.put(url, cookie);
        }
    }
}
