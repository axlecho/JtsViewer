package com.axlecho.jtsviewer.action.parser;

import android.content.Context;

import com.axlecho.jtsviewer.module.JtsCollectionInfo;
import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.network.JtsPageParser;
import com.axlecho.jtsviewer.untils.JtsConf;

import java.util.List;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class JtsParseCollectionFunction implements Function<ResponseBody, List<JtsCollectionInfo>> {
    private static final String TAG = JtsParseCollectionFunction.class.getSimpleName();

    private Context context;

    public JtsParseCollectionFunction(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public List<JtsCollectionInfo> apply(ResponseBody res) throws Exception {
        String html = res.string();
        JtsPageParser.getInstance(context).setContent(html);
        return JtsPageParser.getInstance(context).parserCollection();
    }
}