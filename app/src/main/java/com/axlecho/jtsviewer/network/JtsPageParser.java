package com.axlecho.jtsviewer.network;

import android.content.Context;
import android.text.TextUtils;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.tab.JtsParseTabListAction;
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
    private Context context;
    private static JtsPageParser instance;

    private JtsPageParser(Context context) {
        this.context = context.getApplicationContext();
    }

    public static JtsPageParser getInstance(Context context) {
        if (instance == null) {
            synchronized (JtsPageParser.class) {
                if (instance == null) {
                    instance = new JtsPageParser(context);
                }
            }
        }
        return instance;
    }

    public void setContent(String html) {
        this.html = html;
    }

    public List<JtsTabInfoModel> parserTabList(String srcFrom) {
        if (srcFrom.equals(JtsParseTabListAction.SRC_FROM_DIALY)) {
            return parserTabListFromDaily();
        } else if (srcFrom.equals(JtsParseTabListAction.SRC_FROM_SEARCH)) {
            return parserTabListFromSearch();
        }
        return null;
    }

    public List<JtsTabInfoModel> parserTabListFromSearch() {
        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "parser from search");

        if (html == null) return null;
        Document doc = Jsoup.parse(html);
        Elements tbodys = doc.select("li.pbw");
        Iterator it = tbodys.iterator();

        List<JtsTabInfoModel> models = new ArrayList<>();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            if (element.id().matches("\\d+")) {
                JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, element.toString());
                JtsTabInfoModel model = parserTabByElementFromSearch(element);
                models.add(model);
            }
        }
        return models;
    }

    private JtsTabInfoModel parserTabByElementFromSearch(Element e) {
        if (e == null) return null;
        JtsTabInfoModel model = new JtsTabInfoModel();
        model.author = e.select("a[href*=/artist/]").first().text();
        model.title = e.select("a[href*=/tab/]").first().text();
        model.url = e.select("a[href*=/tab/]").first().attr("href");
        model.type = e.select("img[src*=/static/image/filetype/]").first().attr("title");

//        String info = e.select(".search_tab_info").text();
//        model.watch = JtsTextUnitls.findByPatternOnce(info, "（-）\\s*\\d+\\s*(?<=次查看)").trim();
//        model.time = JtsTextUnitls.findByPatternOnce(info,"(?<=发表于:).*(?=\\s\\s\\s").trim();
        return model;
    }

    public List<JtsTabInfoModel> parserTabListFromDaily() {
        if (html == null) return null;
        Document doc = Jsoup.parse(html);
        Elements tbodys = doc.select("tbody");
        Iterator it = tbodys.iterator();

        List<JtsTabInfoModel> models = new ArrayList<>();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            if (element.id().matches("normalthread_\\d+")) {
                JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, element.toString());
                JtsTabInfoModel model = parserTabByElementFromDaily(element);
                JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, model.toString());
                models.add(model);
            }
        }

        return models;
    }

    private JtsTabInfoModel parserTabByElementFromDaily(Element e) {
        if (e == null) return null;
        JtsTabInfoModel model = new JtsTabInfoModel();

        model.avatar = e.select("img[src*=avatar.php]").attr("src");
        model.author = e.select("a[href*=/artist/]").first().text();
        model.title = e.select("a[href*=/tab/]").first().text();
        model.url = e.select("a[href*=/tab/]").first().attr("href");
        model.type = e.select("img[src*=/static/image/filetype/]").first().attr("title");
        Element time = e.select("span[title~=^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}.*$]").first();
        if (time == null) {
            time = e.select("span:matches(^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}.*$)").first();
        }
        model.time = time.text();

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

        if (TextUtils.isEmpty(model.avatar)) {
            model.avatar = "@" + model.title;
        }

        if (TextUtils.isEmpty(model.uper)) {
            model.uper = context.getString(R.string.unknown_uper);
        }
        return model;
    }
}
