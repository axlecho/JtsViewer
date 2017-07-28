package com.axlecho.jtsviewer.network.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.axlecho.jtsviewer.network.JtsConf;
import com.axlecho.jtsviewer.network.JtsCookieManager;
import com.axlecho.jtsviewer.network.JtsServerApi;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

public class MainWebViewClient extends WebViewClient {

    private static final String TAG = MainWebViewClient.class.getSimpleName();
    private Context context;

    public MainWebViewClient(Context context) {
        this.context = context;
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.contains(JtsConf.NONE_HOST_URL)) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
        return true;
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        super.onPageCommitVisible(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        JtsViewerLog.d(TAG, url);
        JtsServerApi.getInstance(context).processUrlChange(url);
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        JtsCookieManager.getInstance(context).saveCookie(url);
        super.onPageFinished(view, url);
    }
}
