package com.axlecho.jtsviewer.network.parser;

import android.content.Context;

import com.axlecho.jtsviewer.module.JtsThreadModule;
import com.axlecho.jtsviewer.network.JtsPageParser;

import java.util.List;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Created by axlecho on 2018/1/3.
 */

public class JtsParseThreadFunction implements Function<ResponseBody, List<JtsThreadModule>> {

    private Context context;

    public JtsParseThreadFunction(Context context) {
        this.context = context;
    }

    @Override
    public List<JtsThreadModule> apply(ResponseBody res) throws Exception {
        String html = res.string();
        JtsPageParser.getInstance(context).setContent(html);
        return JtsPageParser.getInstance(context).parserThread();
    }
}
