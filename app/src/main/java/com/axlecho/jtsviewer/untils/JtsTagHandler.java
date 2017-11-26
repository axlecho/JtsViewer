package com.axlecho.jtsviewer.untils;

import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.axlecho.jtsviewer.action.ui.JtsPlayVideoAction;
import com.axlecho.sakura.PlayerView;
import com.axlecho.sakura.utils.SakuraTextUtils;
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

    public JtsTagHandler(Context context, TextView parent) {
        this.context = context;
        this.parent = parent;
    }

    @Override
    public void handleTag(boolean opening, String tag, Attributes attributes, Editable output, XMLReader xmlReader) {

        if (tag.equalsIgnoreCase("script")) {

            if (opening) {
                return;
            }

            JtsViewerLog.d(TAG, "[handleTag] -- script");
            JtsViewerLog.d(TAG, "[handleTag] content \n " + output.toString());

            final String videoUrl;
            if (output.toString().contains("YKU.Player")) {
                videoUrl = this.parserAidForYouku(SakuraTextUtils.urlDecode(output.toString()));
            } else {
                videoUrl = this.parserAidForBiliBili(SakuraTextUtils.urlDecode(output.toString()));
            }

            ClickableSpan s = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    JtsPlayVideoAction action = new JtsPlayVideoAction(context, videoUrl);
                    action.execute();
                }
            };

            output.clear();
            output.append("[" + videoUrl + "]");

            if(videoUrl == null) {
                return;
            }
            output.setSpan(s, 0, ("[" + videoUrl + "]").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
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
        JtsViewerLog.d(TAG,context);
        Pattern p = Pattern.compile("vid:\\s+\"([a-zA-Z0-9=]+)\"");
        Matcher m = p.matcher(context);
        if (m.find()) {
            return "http://v.youku.com/v_show/id_" + m.group(1) + ".html";
        }
        return null;
    }
}
