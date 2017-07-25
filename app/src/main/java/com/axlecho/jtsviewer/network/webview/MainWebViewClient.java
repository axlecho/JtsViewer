package com.axlecho.jtsviewer.network.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.axlecho.jtsviewer.network.JtsConf;
import com.axlecho.jtsviewer.network.JtsCookieManager;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

public class MainWebViewClient extends WebViewClient {

    private static final String TAG = MainWebViewClient.class.getSimpleName();
    private Context mContext;
    private MainWebViewListener mListener;

    public MainWebViewClient(Context context, MainWebViewListener listener) {
        mContext = context;
        mListener = listener;
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        JtsViewerLog.d(TAG, url);
        if (url.contains(JtsConf.HOST_URL)) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mContext.startActivity(intent);
        return true;
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        if (mListener != null) {
            mListener.urlChange(url);
        }
        super.onPageCommitVisible(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        JtsCookieManager.getInstance(mContext).saveCookie(url);
        super.onPageFinished(view, url);
    }
}
