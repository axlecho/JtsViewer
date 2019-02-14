package com.axlecho.jtsviewer.network.parser;

import com.axlecho.jtsviewer.untils.JtsConf;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class JtsParseFavoriteFunction implements Function<ResponseBody, String> {
    private static final String TAG = JtsParseFavoriteFunction.class.getSimpleName();
    @Override
    public String apply(ResponseBody responseBody) throws Exception {
        String ret = responseBody.string();
        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE,TAG,ret);
        if(ret.contains("琴包成功，主题已经被加入到相应琴包中")) {
             return JtsConf.STATUS_SUCCESSED;
        } else {
            return JtsConf.STATUS_FAILED;
        }
    }
}
