package com.axlecho.jtsviewer.module;

import android.support.annotation.NonNull;

import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CacheModule implements Comparable<CacheModule> {
    private final static String TAG = CacheModule.class.getSimpleName();
    private final static String INFO_FILE_NAME = "info.json";
    public String path;
    public String fileName;
    public String type;
    public String gid;
    public int frequency = 0;

    public JtsTabInfoModel tabInfo;

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void writeToFile() throws IOException {
        String infoString = this.toJson();

        JtsViewerLog.d(TAG, "cache to json " + infoString);

        if (infoString == null) {
            JtsViewerLog.e(TAG, "prase info to json failed");
            return;
        }
        if (!new File(path).exists()) {
            JtsViewerLog.e(TAG, "path " + path + " is not exists");
            return;
        }

        File info = new File(path, INFO_FILE_NAME);
        if (info.exists()) {
            info.delete();
        }

        if (!info.createNewFile()) {
            JtsViewerLog.e(TAG, "create info file failed " + info.getAbsolutePath());
        }

        FileWriter writer = new FileWriter(info);
        writer.write(infoString);
        writer.close();
    }

    public static CacheModule loadFromFile(File f) throws IOException, JSONException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String jsonStr = reader.readLine();
        reader.close();
        JtsViewerLog.d(JtsViewerLog.CACHE_MODULE, TAG, jsonStr);

        Gson gson = new Gson();
        CacheModule module = gson.fromJson(jsonStr, CacheModule.class);
        return module;
    }

    @Override
    public String toString() {
        return toJson() + "\n";
    }

    @Override
    public int compareTo(@NonNull CacheModule o) {
        return o.frequency - this.frequency;
    }
}