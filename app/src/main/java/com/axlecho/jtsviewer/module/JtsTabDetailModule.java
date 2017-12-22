package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */

public class JtsTabDetailModule {
    public List<JtsThreadModule> threadList;
    public String raw;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
