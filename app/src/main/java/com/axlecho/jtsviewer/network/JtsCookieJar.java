package com.axlecho.jtsviewer.network;

import android.content.Context;

import java.util.ArrayList;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class JtsCookieJar implements CookieJar {
    private JtsPersistentCookieStore cookieStore;

    public JtsCookieJar(Context context) {
        cookieStore = new JtsPersistentCookieStore(context);
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            cookieStore.put(url, cookie);
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }
}
