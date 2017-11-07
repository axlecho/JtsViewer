package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

import java.io.Serializable;

public class JtsTabInfoModel implements Serializable{
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
