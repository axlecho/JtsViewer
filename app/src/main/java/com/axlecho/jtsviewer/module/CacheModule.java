package com.axlecho.jtsviewer.module;

import com.axlecho.jtsviewer.untils.JtsViewerLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CacheModule extends JtsTabInfoModel{
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

    public static CacheModule loadFromFile(File f) throws IOException, JSONException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String jsonStr = reader.readLine();
        reader.close();
        JtsViewerLog.d(JtsViewerLog.CACHE_MODULE, TAG, jsonStr);
        JSONObject json = new JSONObject(jsonStr);

        CacheModule module = new CacheModule();
        module.path = json.getString("path");
        module.fileName = json.getString("fileName");
        module.type = json.getString("type");
        module.gid = json.getString("gid");
        return module;
    }

    @Override
    public String toString() {
        return "[path " + path + " fileName " + fileName + " type " + type + " gid " + gid + "]\n";
    }
}