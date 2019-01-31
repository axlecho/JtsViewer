package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

public class JtsCollectionInfoModel {
    public String title;
    public String url;
    public String uper;
    public String subscribe;
    public String comments;
    public String time;
    public String avatar;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
