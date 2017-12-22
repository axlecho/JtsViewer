package com.axlecho.jtsviewer.action.parser;

import android.content.Context;

import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.network.JtsPageParser;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class JtsParseTabDetailFunction implements Function<ResponseBody, JtsTabDetailModule> {
    private static final String TAG = JtsParseTabDetailFunction.class.getSimpleName();

    private Context context;

    public JtsParseTabDetailFunction(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public JtsTabDetailModule apply(ResponseBody res) throws Exception {
        String html = res.string();
        JtsPageParser.getInstance(context).setContent(html);
        return JtsPageParser.getInstance(context).parserTabDetail();
    }
}
