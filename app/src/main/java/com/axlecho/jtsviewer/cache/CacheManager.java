package com.axlecho.jtsviewer.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.axlecho.jtsviewer.activity.main.MainActivityController;
import com.axlecho.jtsviewer.module.CacheModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CacheManager {

    private static final String TAG = CacheManager.class.getSimpleName();
    private static CacheManager instance;
    private Context context;
    private String cachePath;
    private List<CacheModule> moduleList;

    private CacheManager(Context context) {
        this.context = context.getApplicationContext();
        this.moduleList = new ArrayList<>();
        this.loadCachePath();
        this.loadCacheModule();
    }

    public static CacheManager getInstance(Context context) {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager(context.getApplicationContext());
                }
            }
        }

        return instance;
    }

    private void loadCachePath() {
        SharedPreferences preferences = context.getSharedPreferences("cache", Context.MODE_PRIVATE);
        cachePath = preferences.getString("path", getDefaultCachePath());
    }

    private String getDefaultCachePath() {
        return context.getDir("download_cache", Context.MODE_PRIVATE).getPath();
    }

    private void loadCacheModule() {
        // check cache dir is ready
        File root = new File(cachePath);
        if (!root.exists() || !root.isDirectory()) {
            JtsViewerLog.e(JtsViewerLog.CACHE_MODULE, TAG, "cache dir is not ready");
            return;
        }

        // go through all the subdir
        for (File f : root.listFiles()) {
            JtsViewerLog.d(JtsViewerLog.CACHE_MODULE, TAG, "process " + f.getAbsolutePath());
            if (f.isDirectory()) {
                File info = new File(f.getAbsolutePath() + File.separator + "info.json");
                if (!info.exists()) {
                    continue;
                }

                try {
                    CacheModule module = CacheModule.loadFromFile(info);
                    moduleList.add(module);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<CacheModule> getModule() {
        return moduleList;
    }

    public CacheModule getModule(long gid) {
        for (CacheModule module : moduleList) {
            if (Long.parseLong(module.gid) == gid) {
                return module;
            }
        }
        return null;
    }

    public String getCachePath() {
        return cachePath;
    }

    public void reloadModule() {
        moduleList.clear();
        loadCacheModule();
    }

    public void cacheInfo(long gid, String path, String fileName, JtsTabInfoModel tabInfo) {
        if (findCacheByGid(gid)) {
            return;
        }
        CacheModule cacheInfo = new CacheModule();
        cacheInfo.path = path;
        cacheInfo.fileName = fileName;
        cacheInfo.gid = String.valueOf(gid);
        cacheInfo.type = "gtp";
        cacheInfo.frequency = 0;
        cacheInfo.tabInfo = tabInfo;
        try {
            cacheInfo.writeToFile();
        } catch (IOException e) {
            JtsViewerLog.e(TAG, "cache info failed " + e.getMessage());
        }

        reloadModule();
    }


    private boolean findCacheByGid(long gid) {
        for (CacheModule cache : moduleList) {
            if (cache.gid.equals(String.valueOf(gid))) {
                return true;
            }

        }
        return false;
    }
}
