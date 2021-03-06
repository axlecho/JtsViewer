package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/1.
 */

public class JtsThreadModule implements Serializable {
    public String avatar;
    public String authi;
    public String message;
    public String time;
    public String floor;
    public List<JtsThreadCommentModule> comments = new ArrayList<>();

    @Override
    public String toString() {
        return toJson();
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
