package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

public class JtsTabInfoModel {
    public String title;
    public String author;
    public String time;
    public String reply;
    public String watch;
    public String type;
    public String uper;
    public String avatar;
    public String url;

    @Override
    public String toString() {
        return toJson();
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
