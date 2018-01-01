package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */

public class JtsTabDetailModule {
    public List<JtsThreadModule> threadList;
    public String formhash;
    public int fid;
    // public String raw;

    public String gtpUrl;
    public List<String> imgUrls;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
