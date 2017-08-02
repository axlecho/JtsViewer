package com.axlecho.jtsviewer.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.axlecho.jtsviewer.cache.module.CacheModule;
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
                    instance = new CacheManager(context);
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

    public String getCachePath() {
        return cachePath;
    }

    public void reloadModule() {
        moduleList.clear();
        loadCacheModule();
    }
}
