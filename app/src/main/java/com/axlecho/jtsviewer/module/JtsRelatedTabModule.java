package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

public class JtsRelatedTabModule {
    public String title;
    public String url;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
