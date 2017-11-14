package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/14.
 */

public class JtsThreadCommentModule implements Serializable {
    public String avatar;
    public String authi;
    public String time;
    public String message;
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
