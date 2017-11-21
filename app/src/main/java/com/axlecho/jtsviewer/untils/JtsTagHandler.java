package com.axlecho.jtsviewer.untils;

import android.text.Editable;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.View;

import com.pixplicity.htmlcompat.HtmlCompat;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/11/21.
 */

public class JtsTagHandler implements HtmlCompat.TagHandler {

    private static final String TAG = "html";

    @Override
    public void handleTag(boolean opening, String tag, Attributes attributes, Editable output, XMLReader xmlReader) {

        if (tag.equalsIgnoreCase("script")) {

            if (opening) {
                return;
            }

            JtsViewerLog.d(TAG, "[handleTag] -- script");
            JtsViewerLog.d(TAG, "[handleTag] content \n " + output.toString());
            String aid = this.parserAidForBiliBili(output.toString());
            JtsViewerLog.d(TAG, "[handleTag]  -- parase " + aid);
            output.clear();
            ClickableSpan s = new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    JtsViewerLog.d(TAG,"[handleTag] scirpt clicked");
                }
            };


            output.append("video " + aid);
            output.setSpan(s, 0, ("video " + aid).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private String parserAidForBiliBili(String content) {
        Pattern p = Pattern.compile("aid=(\\d+)");
        Matcher m = p.matcher(content);
        if (m.find()) {
            return m.group(1);
        }

        return null;
    }
}
