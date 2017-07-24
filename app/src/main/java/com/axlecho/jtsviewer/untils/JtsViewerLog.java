package com.axlecho.jtsviewer.untils;

import android.util.Log;

public class JtsViewerLog {
    public static final int NONE_LEVEL = 0;
    public static final int ERROR_LEVEL = 1;
    public static final int WARN_LEVEL = 2;
    public static final int DEBUG_LEVEL = 3;
    public static final int INFO_LEVEL = 4;
    public static final int VERBOSE_LEVEL = 5;

    private static int level = VERBOSE_LEVEL;
    private static String TAG = "JtsViewer";

    public static int v(String tag, String msg) {
        return level >= VERBOSE_LEVEL ? Log.v(TAG, "[" + tag + "] " + msg) : -1;
    }

    public static int d(String tag, String msg) {
        return level >= DEBUG_LEVEL ? Log.d(TAG, "[" + tag + "] " + msg) : -1;
    }

    public static int i(String tag, String msg) {
        return level >= INFO_LEVEL ? Log.i(TAG, "[" + tag + "] " + msg) : -1;
    }

    public static int w(String tag, String msg) {
        return level >= WARN_LEVEL ? Log.w(TAG, "[" + tag + "] " + msg) : -1;
    }

    public static int e(String tag, String msg) {
        return level >= ERROR_LEVEL ? Log.e(TAG, "[" + tag + "] " + msg) : -1;
    }
}
