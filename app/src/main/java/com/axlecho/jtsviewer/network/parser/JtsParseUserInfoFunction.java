package com.axlecho.jtsviewer.network.parser;

import android.content.Context;

import com.axlecho.jtsviewer.module.JtsUserModule;
import com.axlecho.jtsviewer.network.JtsPageParser;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class JtsParseUserInfoFunction implements Function<ResponseBody, JtsUserModule> {
    private Context context;

    public JtsParseUserInfoFunction(Context context) {
        this.context = context;
    }

    @Override
    public JtsUserModule apply(ResponseBody res) throws Exception {
        String html = res.string();
        JtsPageParser.getInstance(context).setContent(html);
        return JtsPageParser.getInstance(context).parserUserInfo();
    }
}
