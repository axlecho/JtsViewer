package com.axlecho.jtsviewer.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JtsServerApi {
    String TAG = JtsServerApi.class.getSimpleName();

    @GET("/tab/{id}")
    Observable<ResponseBody> getDetail(@Path("id") String id);

    @GET("/guide/newtab/{page}")
    Observable<ResponseBody> getNewTab(@Path("page") String page);

    @GET("/guide/hottab/{page}")
    Observable<ResponseBody> getHotTab(@Path("page") String page);
}
