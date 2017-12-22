package com.axlecho.jtsviewer.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JtsServerApi {
    String TAG = JtsServerApi.class.getSimpleName();

    @GET("/tab/{id}")
    Observable<ResponseBody> getDetail(@Path("id") int id);

    @GET("/guide/newtab/{page}")
    Observable<ResponseBody> getNewTab(@Path("page") int page);

    @GET("/guide/hottab/{page}")
    Observable<ResponseBody> getHotTab(@Path("page") int page);

    @GET("/artist/{id}")
    Observable<ResponseBody> getArtist(@Path("id") int id);
}
