package com.axlecho.jtsviewer.action.parser;

import com.axlecho.jtsviewer.untils.JtsConf;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class JtsParseFavoriteFunction implements Function<ResponseBody, String> {

    @Override
    public String apply(ResponseBody responseBody) throws Exception {
        String ret = responseBody.string();
        if(ret.contains("创建琴包")) {
             return JtsConf.STATUS_SUCCESSED;
        } else {
            return JtsConf.STATUS_FAILED;
        }
    }
}
