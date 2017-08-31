package com.axlecho.jtsviewer.network;

import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
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

    public List<JtsTabInfoModel> parserTabList() {
        if (html == null) return null;
        Document doc = Jsoup.parse(html);
        Elements tbodys = doc.select("tbody");
        Iterator it = tbodys.iterator();

        List<JtsTabInfoModel> models = new ArrayList<>();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            if (element.id().matches("normalthread_\\d+")) {
                JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, element.toString());
                JtsTabInfoModel model = parserTabByElement(element);
                JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, model.toString());
                models.add(model);
            }
        }

        return models;
    }

    private JtsTabInfoModel parserTabByElement(Element e) {
        if (e == null) return null;
        JtsTabInfoModel model = new JtsTabInfoModel();

        model.avatar = e.select("img[src*=avatar.php]").attr("src");
        model.author = e.select("a[href*=/artist/]").first().text();
        model.title = e.select("a[href*=/tab/]").first().text();
        model.type = e.select("img[src*=/static/image/filetype/]").first().attr("title");

        Iterator i = e.select("a[href*=/space/]").iterator();

        while (i.hasNext()) {
            Element c = (Element) i.next();
            if (c.text() != null && !c.text().equals("")) {
                model.uper = c.text();
            }
        }

        i = e.select("a[href*=/tab/]").iterator();
        while (i.hasNext()) {
            Element c = (Element) i.next();
            if (c.text() != null && c.text().matches("\\d+")) {
                model.reply = c.text();
            }
        }

        String info = e.select(".mtn").text();
        model.watch = JtsTextUnitls.findByPatternOnce(info, "(?<=查看:)\\s*\\d+").trim();
//        model.time = JtsTextUnitls.findByPatternOnce(info,"(?<=发表于:).*(?=\\s\\s\\s").trim();
        return model;
    }
}
