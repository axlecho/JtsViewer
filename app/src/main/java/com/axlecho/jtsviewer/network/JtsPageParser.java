package com.axlecho.jtsviewer.network;

import android.content.Context;

import com.axlecho.jtsviewer.module.TabInfoModel;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;
import java.util.List;

public class JtsPageParser {
    private static final String TAG = JtsPageParser.class.getSimpleName();
    private String html;
    private static JtsPageParser instance;

    private JtsPageParser() {
    }

    public static JtsPageParser getInstance() {
        if (instance == null) {
            synchronized (JtsPageParser.class) {
                if (instance == null) {
                    instance = new JtsPageParser();
                }
            }
        }
        return instance;
    }

    public void setContent(String html) {
        this.html = html;
    }

    public List<TabInfoModel> parserTabList() {
        if (html == null) return null;
        Document doc = Jsoup.parse(html);
        Elements tbodys = doc.select("tbody");
        Iterator it = tbodys.iterator();

        while (it.hasNext()) {
            Element element = (Element) it.next();
            if (element.id().matches("normalthread_\\d+")) {
                JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, element.toString());
            }
        }

        return null;
    }
}
