package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

public class JtsCollectionInfoModel {
    public String title;
    public int num;
    public String description;
    public String url;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}