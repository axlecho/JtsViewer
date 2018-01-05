package com.axlecho.jtsviewer.network;

import com.axlecho.jtsviewer.module.JtsVersionInfoModule;


import io.reactivex.Observable;
import retrofit2.http.GET;

public interface JtsGithubApi {
    String TAG = JtsGithubApi.class.getSimpleName();

    @GET("/repos/axlecho/JtsViewer/releases/latest")
    Observable<JtsVersionInfoModule> getLastVersion();
}
