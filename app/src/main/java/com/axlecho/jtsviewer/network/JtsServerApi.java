package com.axlecho.jtsviewer.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JtsServerApi {
    String TAG = JtsServerApi.class.getSimpleName();
    String USER_AGENT = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Mobile Safari/537.36";

    @GET("/tab/{id}")
    Observable<ResponseBody> getDetail(@Path("id") long id);

    @GET("/guide/newtab/{page}")
    Observable<ResponseBody> getNewTab(@Path("page") int page);

    @GET("/guide/hottab/{page}")
    Observable<ResponseBody> getHotTab(@Path("page") int page);

    @GET("/artist/{id}")
    Observable<ResponseBody> getArtist(@Path("id") int id);

    @GET("/search/tab/{keyword}")
    Observable<ResponseBody> search(@Path("keyword") String keyword);

    @GET("/search.php?mod=tab&searchsubmit=yes")
    Observable<ResponseBody> searchById(@Query("searchid") int searchId, @Query("page") int page);
}
