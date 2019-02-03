package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

import java.io.Serializable;

public class JtsRelatedVideoModule implements Serializable {
    public String url;
    public String thumbnail;
    public String title;
    public String info;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
