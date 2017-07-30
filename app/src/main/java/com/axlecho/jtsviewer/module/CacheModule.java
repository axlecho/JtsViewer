package com.axlecho.jtsviewer.module;

import com.axlecho.jtsviewer.untils.JtsViewerLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CacheModule {
    private final static String TAG = CacheModule.class.getSimpleName();
    private final static String INFO_FILE_NAME = "info.json";
    public String path;
    public String fileName;
    public String type;
    public String gid;

    public String toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("path", path);
        json.put("fileName", fileName);
        json.put("type", type);
        json.put("gid", gid);
        return json.toString();
    }

    public void writeToFile() throws IOException {
        String infoString = null;
        try {
            infoString = this.toJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
}