package com.axlecho.jtsviewer.action.parser;

import android.content.Context;

import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsPageParser;

import java.util.List;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class JtsParseTabListFunction implements Function<ResponseBody, List<JtsTabInfoModel>> {
    private static final String TAG = JtsParseTabListFunction.class.getSimpleName();

    private Context context;

    public JtsParseTabListFunction(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public List<JtsTabInfoModel> apply(ResponseBody res) throws Exception {
        String html = res.string();
        JtsPageParser.getInstance(context).setContent(html);
        return JtsPageParser.getInstance(context).parserTabList();
    }
}
