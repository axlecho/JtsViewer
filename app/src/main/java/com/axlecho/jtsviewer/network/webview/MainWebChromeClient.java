package com.axlecho.jtsviewer.network.webview;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.axlecho.jtsviewer.activity.main.MainActivityController;

public class MainWebChromeClient extends WebChromeClient {

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        MainActivityController.getInstance().processProgressChanged(newProgress);
        super.onProgressChanged(view, newProgress);
    }
}
