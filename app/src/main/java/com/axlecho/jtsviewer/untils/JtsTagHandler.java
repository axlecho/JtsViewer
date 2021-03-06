package com.axlecho.jtsviewer.untils;

import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.axlecho.jtsviewer.action.ui.JtsPlayVideoAction;
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
    private Context context;
    private TextView parent;
    private String source;
    private int mark;

    public JtsTagHandler(Context context, TextView parent,String source) {
        this.context = context;
        this.parent = parent;
        this.source = source;
    }

    private void handleIframe(boolean opening, String tag, Attributes attributes, Editable output, XMLReader xmlReader) {
        if (tag.equalsIgnoreCase("iframe")) {
            JtsViewerLog.d(TAG, "[handleTag] - " + tag + " - " + opening);
            if (opening) {
                mark = output.length();
                return;
            }

            JtsViewerLog.d(TAG, "[handleTag] -- iframe");
            JtsViewerLog.d(TAG, "[handleTag] content \n " + output.toString());

            final String videoUrl;
            if (source.contains("biliPlayer")) {
                videoUrl = this.parserAidForBiliBili(source);
            } else {
                videoUrl = this.parserAidForYouku(source);
            }

            ClickableSpan s = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    JtsPlayVideoAction action = new JtsPlayVideoAction(context, videoUrl);
                    action.execute();
                }
            };

            // output.clear();
            output.replace(mark, output.length(), "\n[" + videoUrl + "]\n");

            if (videoUrl == null) {
                return;
            }
            output.setSpan(s, mark, mark + ("[" + videoUrl + "]").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void handleScript(boolean opening, String tag, Attributes attributes, Editable output, XMLReader xmlReader) {
        if (tag.equalsIgnoreCase("script")) {
            if (opening) {
                mark = output.length();
                return;
            }

            JtsViewerLog.d(TAG, "[handleTag] -- script");
            JtsViewerLog.d(TAG, "[handleTag] content \n " + output.toString());

            // output.clear();
            output.replace(mark, output.length(), "");
        }
    }

    @Override
    public void handleTag(boolean opening, String tag, Attributes attributes, Editable output, XMLReader xmlReader) {
        this.handleIframe(opening, tag, attributes, output, xmlReader);
        this.handleScript(opening, tag, attributes, output, xmlReader);
    }

    private String parserAidForBiliBili(String content) {
        Pattern p = Pattern.compile("aid=(\\d+)");
        Matcher m = p.matcher(content);
        if (m.find()) {
            return "https://www.bilibili.com/video/av" + m.group(1);
        }

        return null;
    }

    private String parserAidForYouku(String context) {
        JtsViewerLog.d(TAG, context);
        Pattern p = Pattern.compile("vid:\\s+\"([a-zA-Z0-9=]+)\"");
        Matcher m = p.matcher(context);
        if (m.find()) {
            return "http://v.youku.com/v_show/id_" + m.group(1) + ".html";
        }
        return null;
    }
}
