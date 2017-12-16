package com.axlecho.jtsviewer.network;

import com.axlecho.jtsviewer.module.JtsTabDetailModule;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JtsServerApi {
    String TAG = JtsServerApi.class.getSimpleName();

    @GET("/tab/{id}")
    JtsTabDetailModule getDetail(@Path("id") String id);
}
