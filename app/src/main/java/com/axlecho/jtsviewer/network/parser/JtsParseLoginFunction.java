package com.axlecho.jtsviewer.network.parser;

import com.axlecho.jtsviewer.untils.JtsViewerLog;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class JtsParseLoginFunction implements Function<Response<ResponseBody>, String> {
    private static final String TAG = JtsParseLoginFunction.class.getSimpleName();

    @Override
    public String apply(Response<ResponseBody> res) throws Exception {
        String cookies = "";
        for (String cookie : res.headers().values("Set-Cookie")) {
            if (cookie.contains("_auth=")) {
                JtsViewerLog.d(TAG, cookie.split(";")[0]);
                cookies += cookie.split(";")[0] + ";";
            }
        }
        return cookies;
    }
}
