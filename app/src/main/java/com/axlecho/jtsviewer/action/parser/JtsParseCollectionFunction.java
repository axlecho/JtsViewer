package com.axlecho.jtsviewer.action.parser;

import android.content.Context;

import com.axlecho.jtsviewer.module.JtsCollectionInfoModel;
import com.axlecho.jtsviewer.network.JtsPageParser;

import java.util.List;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class JtsParseCollectionFunction implements Function<ResponseBody, List<JtsCollectionInfoModel>> {
    private static final String TAG = JtsParseCollectionFunction.class.getSimpleName();

    private Context context;

    public JtsParseCollectionFunction(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public List<JtsCollectionInfoModel> apply(ResponseBody res) throws Exception {
        String html = res.string();
        JtsPageParser.getInstance(context).setContent(html);
        return JtsPageParser.getInstance(context).parserCollection();
    }
}