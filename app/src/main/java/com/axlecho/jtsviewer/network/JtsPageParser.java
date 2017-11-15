package com.axlecho.jtsviewer.network;

import android.content.Context;
import android.text.TextUtils;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.tab.JtsParseTabListAction;
import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.module.JtsThreadCommentModule;
import com.axlecho.jtsviewer.module.JtsThreadModule;
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

    public List<JtsTabInfoModel> parserTabList() {
        return parserTabListFromDaily();
    }

    public List<JtsTabInfoModel> parserTabListFromDaily() {
        if (html == null) return null;
        Document doc = Jsoup.parse(html);


        Elements tabItems = doc.select("div.tab-item");
        Iterator it = tabItems.iterator();
        List<JtsTabInfoModel> models = new ArrayList<>();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, element.toString());
            JtsTabInfoModel model = parserTabByElementFromDaily(element);
            JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, model.toString());
            models.add(model);
        }

        return models;
    }

    private JtsTabInfoModel parserTabByElementFromDaily(Element e) {
        if (e == null) return null;
        JtsTabInfoModel model = new JtsTabInfoModel();

        model.avatar = e.select("img[src*=images/album]").attr("src");
        model.author = e.select("a[href*=/artist/]").first().text();
        model.title = e.select("a[href*=/tab/]").first().text();
        model.url = e.select("a[href*=/tab/]").first().attr("href");
        model.type = e.select("span.tabtype").first().text();
        model.uper = e.select("span[title*=发布者]").first().nextElementSibling().text();
        model.watch = e.select("span[title*=查看]").first().nextElementSibling().text();
        model.reply = e.select("span[title*=回复]").first().nextElementSibling().text();
        // search mode has no time attr
        try {
            model.time = e.select("span[title*=发布时间]").first().nextElementSibling().text();
        } catch (NullPointerException ex) {
            // ex.printStackTrace();
            model.time = context.getResources().getString(R.string.unknown_time);
        }

        if (TextUtils.isEmpty(model.avatar)) {
            model.avatar = "@" + model.title;
        }

        if (TextUtils.isEmpty(model.uper)) {
            model.uper = context.getString(R.string.unknown_uper);
        }

        Elements tagElemnets = e.select("a.tag");
        Iterator it = tagElemnets.iterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            String tag = element.text();
            model.tags.add(tag);
        }
        return model;
    }

    public JtsTabDetailModule parserTabDetail() {
        JtsTabDetailModule detail = new JtsTabDetailModule();
        detail.raw = html;
        detail.threadList = parserThread();
        return detail;
    }

    public List<JtsThreadModule> parserThread() {
        if (html == null) return null;

        Document doc = Jsoup.parse(html);
        Elements tbodys = doc.select("table.plhin");
        List<JtsThreadModule> moduleList = new ArrayList<>();

        Iterator it = tbodys.iterator();
        while (it.hasNext()) {
            Element c = (Element) it.next();
            JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, c.toString());
            JtsThreadModule module = parserThraed(c);
            JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[parserThread]" + module);
            moduleList.add(module);
        }
        return moduleList;
    }

    private JtsThreadModule parserThraed(Element e) {
        if (e == null) return null;
        JtsThreadModule module = new JtsThreadModule();
        module.authi = e.select("div.authi").first().text();
        module.avatar = e.select("img[src*=avatar.php]").attr("src");
        module.time = e.select("em:contains(发表于)").first().text();
        module.message = e.select("td.t_f").first().html();
        module.floor = e.select("a[onclick*=setCopy]").first().text();
        Elements comments = e.select("dl.cl");
        Iterator it = comments.iterator();
        while (it.hasNext()) {
            Element c = (Element) it.next();
            JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[parserComment]" + c.toString());
            JtsThreadCommentModule comment = new JtsThreadCommentModule();
            comment.time = c.select("span.xg1").first().text();
            comment.avatar = c.select("img[src*=avatar.php]").attr("src");
            comment.authi = c.select("a.xi2").first().text();
            comment.message = c.select("dd:not(.m)").first().text();
            JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "[parserComment]" + comment.toString());
            module.comments.add(comment);

        }
        return module;
    }
}
