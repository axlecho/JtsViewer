package com.axlecho.jtsviewer.network;

import android.net.Uri;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class JtsCookieManager {

    public void saveCookie(WebViewClient client) {
    }

    public static void setCookie(WebView webView, String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= 21) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        cookieManager.setCookie(url, "yGhj_ec43_saltkey=fE06oZ0Z;");
        cookieManager.setCookie(url, "yGhj_ec43_lastvisit=1500909398;");
        cookieManager.setCookie(url, "yGhj_ec43_pvi=621312007;");
        cookieManager.setCookie(url, "yGhj_ec43_si=s1702441560;");
        cookieManager.setCookie(url, "yGhj_ec43_ulastactivity=1500913100%7C0;");
        cookieManager.setCookie(url, "yGhj_ec43_auth=1ce8GerBt38bgYyIQhSILhA46gUVfmUsBMpQM346LWYDVDnK9lKwarxbmWnKF%2FYzjhWUhMoAr52Qg%2Feo0K6eQ%2FhjOnI;");
        cookieManager.setCookie(url, "yGhj_ec43_creditnotice=0D0D1D0D0D0D0D0D0D104691;");
        cookieManager.setCookie(url, "yGhj_ec43_creditbase=0D0D122D0D0D0D0D0D0;");
        cookieManager.setCookie(url, "yGhj_ec43_creditrule=%E6%AF%8F%E5%A4%A9%E7%99%BB%E5%BD%95;");
        cookieManager.setCookie(url, "yGhj_ec43_lastcheckfeed=104691%7C1500913100;");
        cookieManager.setCookie(url, "yGhj_ec43_lip=27.38.242.120%2C1500913100;");
        cookieManager.setCookie(url, "yGhj_ec43_home_readfeed=1500913104;");
        cookieManager.setCookie(url, "yGhj_ec43_lastact=1500913202%09forum.php%09viewthread;");
        cookieManager.setCookie(url, "yGhj_ec43_connect_is_bind=0;");
        cookieManager.setCookie(url, "yGhj_ec43_st_p=104691%7C1500913202%7Ca4c2a450bd9a901897031c16a157d6fc;");
        cookieManager.setCookie(url, "yGhj_ec43_viewid=tid_1314565;");
        cookieManager.setCookie(url, "Hm_lvt_4047155fa7a144003ba78cf6ed8071a8=1500913002;");
        cookieManager.setCookie(url, "Hm_lpvt_4047155fa7a144003ba78cf6ed8071a8=1500913204");
        CookieSyncManager.getInstance().sync();
    }
}
