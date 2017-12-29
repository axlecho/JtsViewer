package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

public class JtsUserModule {
    public long uid;
    public String userName;
    public String avatarUrl;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
