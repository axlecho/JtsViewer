package com.axlecho.jtsviewer.network.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.axlecho.jtsviewer.network.Conf;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

public class MainWebViewClient extends WebViewClient {

    private static final String TAG = MainWebViewClient.class.getSimpleName();
    private Context mContext;

    public MainWebViewClient(Context context) {
        mContext = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        JtsViewerLog.d(TAG, url);

        if (url.contains(Conf.HOST)) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mContext.startActivity(intent);
        return true;
    }

    public void onPageFinished(WebView view, String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        String CookieStr = cookieManager.getCookie(url);
        JtsViewerLog.i(TAG, "Cookies = " + CookieStr);
        super.onPageFinished(view, url);
    }
}
