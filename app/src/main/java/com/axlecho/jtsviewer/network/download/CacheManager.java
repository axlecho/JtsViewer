package com.axlecho.jtsviewer.network.download;

import android.content.Context;

import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.io.File;
import java.io.FileInputStream;

public class CacheManager {
    private static final String TAG = "CacheManager";
    private static CacheManager mInstance;
    private CacheDatabase mCacheDatabase;
    private Context mContext;

    private CacheManager(Context context) {
        mCacheDatabase = new CacheDatabase(context);
        mContext = context;
    }

    public static CacheManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (CacheManager.class) {
                if (mInstance == null) {
                    mInstance = new CacheManager(context);
                }
            }
        }
        return mInstance;
    }

    public long addCacheInfo(CacheInfo info) {
        return mCacheDatabase.insert(info);
    }

    public long addCache(File file, String key) {
        if (file == null) {
            JtsViewerLog.e(TAG, "[addCache] file can not be null");
            return -1;
        }

        if (!file.isFile()) {
            JtsViewerLog.e(TAG, "[addCache] file " + file.getAbsolutePath() + " is not a file");
            return -1;
        }

        try {
            CacheInfo ci = new CacheInfo();
            ci.filesize = new FileInputStream(file).available();
            ci.key = key;
            ci.path = file.getAbsolutePath();
            ci.frequency = 0;
            // ci.hash = SHAUtil.getFileMD5(file);
            return mCacheDatabase.insert(ci);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

}
