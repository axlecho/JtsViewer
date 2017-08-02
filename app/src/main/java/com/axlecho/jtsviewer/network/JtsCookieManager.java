package com.axlecho.jtsviewer.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import com.axlecho.jtsviewer.untils.JtsViewerLog;

public class JtsCookieManager {
    private static final String TAG = JtsCookieManager.class.getSimpleName();
    private static final String PREFERENCES_PATH = "network";
    private static final String COOKIES_KEY = "cookies";
    private static JtsCookieManager instance;
    private Context context;

    private JtsCookieManager(Context context) {
        this.context = context;
    }

    public static JtsCookieManager getInstance(Context context) {
        if (instance == null) {
            synchronized (JtsCookieManager.class) {
                if (instance == null) {
                    instance = new JtsCookieManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public void saveCookie(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        String cookieStr = cookieManager.getCookie(url);
        if (cookieStr == null) {
            return;
        }

        if (!cookieStr.contains("auth=")) {
            return;
        }

        SharedPreferences preferences = this.context.getSharedPreferences(PREFERENCES_PATH, Context.MODE_PRIVATE);
        if (preferences.getString(COOKIES_KEY, null) != null) {
            return;
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(COOKIES_KEY, cookieStr);
        editor.apply();
        JtsViewerLog.i(TAG, "save cookie " + cookieStr);
    }

    public void setCookie(WebView webView, String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= 21) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }

        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        SharedPreferences preferences = this.context.getSharedPreferences(PREFERENCES_PATH, Context.MODE_PRIVATE);
        String cookieStr = preferences.getString(COOKIES_KEY, null);
        if (cookieStr == null) {
            return;
        }

        for (String cookie : cookieStr.split(";")) {
            JtsViewerLog.i(TAG, "setcookie " + cookie);
            cookieManager.setCookie(url, cookie);
        }
        CookieSyncManager.getInstance().sync();
    }

    public String getCookie() {
        SharedPreferences preferences = this.context.getSharedPreferences(PREFERENCES_PATH, Context.MODE_PRIVATE);
        return preferences.getString(COOKIES_KEY, null);
    }
}
