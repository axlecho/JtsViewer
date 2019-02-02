package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */

public class JtsTabDetailModule implements Serializable {
    public List<JtsThreadModule> threadList;
    public String formhash;
    public int fid;
    // public String raw;

    public String gtpUrl;
    public List<String> imgUrls;

    public List<JtsRelatedVideoModule> relatedVideos;
    public List<JtsRelatedTabModule> relatedTabs;
    public List<JtsRelatedPostModule> relatedPosts;
    public List<JtsRelateCollectionModule> relatedCollections;

    public String info;
    public String lyric;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
