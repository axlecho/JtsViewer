package com.axlecho.jtsviewer;

import android.app.Application;

import com.axlecho.jtsviewer.untils.JtsCrashHandler;

/**
 * Created by axlecho on 17-12-5.
 */

public class JtsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JtsCrashHandler.getInstance().init(this);
    }
}