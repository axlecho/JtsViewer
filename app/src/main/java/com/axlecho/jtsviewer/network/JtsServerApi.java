package com.axlecho.jtsviewer.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface JtsServerApi {
    String TAG = JtsServerApi.class.getSimpleName();
    String USER_AGENT = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Mobile Safari/537.36";

    @GET("/")
    Observable<ResponseBody> index();

    @GET("/tab/{id}/{page}")
    Observable<ResponseBody> getDetail(@Path("id") long id, @Path("page") int page);

    @GET("/guide/newtab/{page}")
    Observable<ResponseBody> getNewTab(@Path("page") int page);

    @GET("/guide/hottab/{page}")
    Observable<ResponseBody> getHotTab(@Path("page") int page);

    @GET("/artist/{id}/{page}")
    Observable<ResponseBody> getArtist(@Path("id") int id, @Path("page") int page);

    @GET("/search/tab/{keyword}")
    Observable<ResponseBody> search(@Path("keyword") String keyword);

    @GET("/search.php?mod=tab&searchsubmit=yes")
    Observable<ResponseBody> searchById(@Query("searchid") int searchId, @Query("page") int page);

    @GET("/collection/my")
    Observable<ResponseBody> getCollection();

    @GET("/collection/{id}/{page}")
    Observable<ResponseBody> getCollectionDetail(@Path("id") long id, @Path("page") int page);

    @FormUrlEncoded
    @POST("/member.php?mod=logging&action=login&loginsubmit=yes&handlekey=login&loginhash=LFR1d&inajax=1")
    Observable<Response<ResponseBody>> login(@Field("formhash") String hash,
                                             @Field("referer") String referer,
                                             @Field("username") String username,
                                             @Field("password") String password,
                                             @Field("cookietime") long cookietime);

    @FormUrlEncoded
    @POST("/forum.php?mod=post&action=reply&extra=page%3D1&replysubmit=yes&infloat=yes&handlekey=fastpost&inajax=1")
    Observable<ResponseBody> postComment(@Query("fid") int fid,
                                         @Query("tid") long tid,
                                         @Field("message") String message,
                                         @Field("posttime") long posttime,
                                         @Field("formhash") String formhash,
                                         @Field("usesig") int usesig,
                                         @Field("subject") String subject);

    @FormUrlEncoded
    @POST("/forum.php?mod=collection&action=edit&op=addthread&inajax=1")
    Observable<ResponseBody> favorite(
                                      @Field("ctid")long ctid,
                                      @Field("reason") String reason,
                                      @Field("tids[]")long tid,
                                      @Field("inajax")int inajax,
                                      @Field("handlekey") String handlekey,
                                      @Field("formhash") String formhash,
                                      @Field("addthread") int addthread);

    @GET
    Observable<Response<ResponseBody>> download(@Url String fileUrl);
}
