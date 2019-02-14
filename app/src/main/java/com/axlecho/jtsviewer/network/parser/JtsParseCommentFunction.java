package com.axlecho.jtsviewer.network.parser;

import com.axlecho.jtsviewer.untils.JtsConf;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class JtsParseCommentFunction implements Function<ResponseBody, String> {
    private static final String TAG = JtsParseCommentFunction.class.getSimpleName();

    @Override
    public String apply(ResponseBody res) throws Exception {
        String ret = res.string();
        if (ret.contains("回复发布成功")) {
            return JtsConf.STATUS_SUCCESSED;
        } else {
            // TODO return error message
            return JtsConf.STATUS_FAILED;
        }
    }
}
