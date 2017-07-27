package com.axlecho.jtsviewer.network.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.axlecho.jtsviewer.network.JtsConf;
import com.axlecho.jtsviewer.network.JtsCookieManager;


public class MainWebView extends WebView {
    public MainWebView(Context context) {
        super(context);
        this.init();
    }

    public MainWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public MainWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        JtsCookieManager.getInstance(this.getContext()).setCookie(this, JtsConf.HOST_URL);
        this.setWebViewClient(new MainWebViewClient(this.getContext()));
        this.setWebChromeClient(new MainWebChromeClient());
        WebSettings webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

}
