package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

import java.io.Serializable;

public class JtsRelateCollectionModule implements Serializable {
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
