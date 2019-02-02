package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

import java.io.Serializable;

public class JtsRelatedTabModule implements Serializable {
    public String title;
    public String url;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
