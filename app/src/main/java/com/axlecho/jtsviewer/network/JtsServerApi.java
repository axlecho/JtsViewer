package com.axlecho.jtsviewer.network;

import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JtsServerApi {
    String TAG = JtsServerApi.class.getSimpleName();

    @GET("/tab/{id}")
    JtsTabDetailModule getDetail(@Path("id") String id);

    @GET("/newtab/{page}")
    List<JtsTabInfoModel> getNewTab(@Path("page") String page);

    @GET("/hottab/{page}")
    List<JtsTabInfoModel> getHotTab(@Path("page") String page);
}
