package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

public class JtsRelateCollectionModule {
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
