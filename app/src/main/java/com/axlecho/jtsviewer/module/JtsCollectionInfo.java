package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

public class JtsCollectionInfo {
    public String title;
    public int num;
    public String description;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
