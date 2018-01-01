package com.axlecho.jtsviewer.untils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class JtsViewerLog {
    private static final String TAG = JtsViewerLog.class.getSimpleName();
    public static final int NONE_LEVEL = 0;
    public static final int ERROR_LEVEL = 1;
    public static final int WARN_LEVEL = 2;
    public static final int DEBUG_LEVEL = 3;
    public static final int INFO_LEVEL = 4;
    public static final int VERBOSE_LEVEL = 5;

    public static String DEFAULT_MODULE = "jts-viewer";
    public static String NETWORK_MODULE = "jts-network";
    public static String CACHE_MODULE = "jts-cache";
    public static String TRACE_MODULE = "jts-trace";
    private static int level = DEBUG_LEVEL;


    public static int v(String module, String tag, String msg) {
        return level >= VERBOSE_LEVEL ? Log.v(module, "[" + tag + "] " + msg) : -1;
    }

    public static int v(String tag, String msg) {
        return v(DEFAULT_MODULE, tag, msg);
    }

    public static int d(String module, String tag, String msg) {
        return level >= DEBUG_LEVEL ? Log.d(module, "[" + tag + "] " + msg) : -1;
    }

    public static int d(String tag, String msg) {
        return d(DEFAULT_MODULE, tag, msg);
    }

    public static int i(String module, String tag, String msg) {
        return level >= INFO_LEVEL ? Log.i(module, "[" + tag + "] " + msg) : -1;
    }

    public static int i(String tag, String msg) {
        return i(DEFAULT_MODULE, tag, msg);
    }

    public static int w(String module, String tag, String msg) {
        return level >= WARN_LEVEL ? Log.w(module, "[" + tag + "] " + msg) : -1;
    }

    public static int w(String tag, String msg) {
        return w(DEFAULT_MODULE, tag, msg);
    }

    public static int e(String module, String tag, String msg) {
        return level >= ERROR_LEVEL ? Log.e(module, "[" + tag + "] " + msg) : -1;
    }

    public static int e(String tag, String msg) {
        return e(DEFAULT_MODULE, tag, msg);
    }

    public static int appendToFile(Context context, String msg) {
        String cache = context.getCacheDir().getPath();
        File log = new File(cache + File.separator + "jts.log");
        Writer out = null;
        try {
            if (!log.exists()) {
                log.createNewFile();
            }
            out = new FileWriter(log, true);
            out.write(msg);
            out.close();
        } catch (IOException e) {
            JtsViewerLog.e(TAG, "write log failed -- io exceprtion " + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }

            }
        }
        return -1;
    }
}
