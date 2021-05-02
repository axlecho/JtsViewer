package com.axlecho.jtsviewer.network;

import android.content.Context;
import android.text.TextUtils;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.module.JtsCollectionInfoModel;
import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.module.JtsThreadModule;
import com.axlecho.jtsviewer.module.JtsUserModule;
import com.axlecho.jtsviewer.untils.JtsStringUtils;
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
    private String UID_URL_PATTERN = "(?<=discuz_uid = ')\\d+";
    private String USER_NAME_PATTERN = "(?<=title=\"访问我的空间\">).*?(?=</a>)";
    private String USER_IMAGE_PATTERN = "https://att.jitashe.org/data/attachment/avatar/.*?.jpg";


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
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, element.toString());
            JtsTabInfoModel model = parserTabByElementFromDaily(element);
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, model.toString());
            models.add(model);
        }

        return models;
    }

    private JtsTabInfoModel parserTabByElementFromDaily(Element e) {
        if (e == null) return null;
        JtsTabInfoModel model = new JtsTabInfoModel();

        model.avatar = e.select("img").attr("src");
        model.title = e.select("a.title").first().text();

        model.url = e.select("a[href*=/tab/]").first().attr("href");
        model.type = e.select("span.tabtype").first().text();
        // model.uper = e.select("span[title*=发布者]").first().nextElementSibling().text();
        model.uper = e.select("a[href*=/space/]").first().text();
        model.watch = e.select("span[title*=查看]").first().nextElementSibling().text();
        model.reply = e.select("span[title*=回复]").first().nextElementSibling().text();
        try {
            model.time = e.select("span[title*=发布时间]").first().nextElementSibling().text();
        } catch (Exception ex) {
            model.time = "";
        }

        Element authorObject = e.select("a[href*=/artist/]").first();
        if (authorObject != null) {
            model.author = authorObject.text();
        } else {
            model.author = context.getResources().getString(R.string.unknown_author);
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

    public List<JtsThreadModule> parserThread() {
        return JtsDetailPageParserHelper.parserThread(html);
    }

    public JtsTabDetailModule parserTabDetail() {
        JtsTabDetailModule detail = new JtsTabDetailModule();
        // detail.raw = html;
        detail.formhash = JtsDetailPageParserHelper.parserFormHash(html);
        detail.fid = JtsDetailPageParserHelper.parserFid(html);
        detail.threadList = JtsDetailPageParserHelper.parserThread(html);
        detail.gtpUrl = JtsDetailPageParserHelper.parserGtpUrl(html);
        detail.imgUrls = JtsDetailPageParserHelper.parserImgUrl(html);
        if (detail.imgUrls == null || detail.imgUrls.size() == 0) {
            detail.imgUrls = JtsDetailPageParserHelper.parserPdfUrl(html);
        }
        detail.textTabData = JtsDetailPageParserHelper.parserTextTabData(html);
        detail.lyric = JtsDetailPageParserHelper.parserLyric(html);
        detail.info = JtsDetailPageParserHelper.parserInfo(html);
        detail.relatedTabs = JtsDetailPageParserHelper.parserRelatedTab(html);
        detail.relatedVideos = JtsDetailPageParserHelper.parserVideo(html);
        detail.relatedPosts = JtsDetailPageParserHelper.parserRelatedPost(html);
        detail.relatedCollections = JtsDetailPageParserHelper.parserRelatedCollection(html);
        return detail;
    }

    public JtsUserModule parserUserInfo() {
        JtsUserModule user = new JtsUserModule();
        String uidStr = JtsTextUnitls.findByPatternOnce(html, UID_URL_PATTERN);
        if (uidStr != null) {
            user.uid = Long.parseLong(uidStr);
        } else {
            user.uid = -1;
        }
        user.userName = JtsTextUnitls.findByPatternOnce(html, USER_NAME_PATTERN);
        user.avatarUrl = JtsTextUnitls.findByPatternOnce(html, USER_IMAGE_PATTERN);
        return user;
    }

    public int parserSearchId() {
        if (html == null) return -1;
        // Document doc = Jsoup.parse(html);
        // String href = doc.select("a[href*=search.php?mod=tab&searchid]").first().attr("href");
        String searchId = JtsStringUtils.search(html, "searchid=(\\d+)");
        return searchId == null ? -1 : Integer.parseInt(searchId);
    }

    public List<JtsCollectionInfoModel> parserCollection() {
        if (html == null) return null;
        Document doc = Jsoup.parse(html);

        // JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG,html);
        Elements tabItems = doc.select("div.collection-item");
        Iterator it = tabItems.iterator();
        List<JtsCollectionInfoModel> models = new ArrayList<>();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, element.toString());
            JtsCollectionInfoModel model = parserCollectionByElement(element);
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, model.toString());
            models.add(model);
        }

        return models;
    }

    public JtsCollectionInfoModel parserCollectionByElement(Element e) {
        if (e == null) return null;
        JtsCollectionInfoModel model = new JtsCollectionInfoModel();
        model.title = e.select("a.ci-name").text();
        model.url = e.select("a[href*=/collection/]").first().attr("href");
        model.uper = e.select("a[href*=/space/]").first().text();
        model.subscribe = e.select("span.icon-dingyue").text();
        model.comments = e.select("span.icon-huifu").text();
        model.time = e.select("span.timeline").text();
        model.avatar = e.select("div.ci-icon").first().child(0).attr("src");
        return model;
    }
}
