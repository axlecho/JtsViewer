package com.axlecho.jtsviewer.action.parser;

import android.content.Context;

import com.axlecho.jtsviewer.network.JtsPageParser;
import com.axlecho.jtsviewer.network.JtsSearchHelper;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.nio.charset.Charset;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Response;

public class JtsParseSearchKeyConsumer implements Consumer<ResponseBody> {
    private static final String TAG = JtsParseSearchKeyConsumer.class.getSimpleName();
    private String keyword;
    private Context context;

    public JtsParseSearchKeyConsumer(Context context, String keyword) {
        this.context = context;
        this.keyword = keyword;
    }

    @Override
    public void accept(ResponseBody responseBody) throws Exception {
        long contentLength = responseBody.contentLength();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();

        if (contentLength != 0) {
            Charset charset = Charset.forName("Utf-8");
            String html = buffer.clone().readString(charset);
            JtsPageParser.getInstance(context).setContent(html);
            int searchKey = JtsPageParser.getInstance(context).parserSearchId();
            if (searchKey > 0) {
                JtsSearchHelper.getSingleton().updateKey(keyword, searchKey);
            }
        }
    }
}
