package com.axlecho.jtsviewer.network;

import android.content.Context;
import android.text.TextUtils;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.module.JtsCollectionInfo;
import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.module.JtsThreadCommentModule;
import com.axlecho.jtsviewer.module.JtsThreadModule;
import com.axlecho.jtsviewer.module.JtsUserModule;
import com.axlecho.jtsviewer.untils.JtsConf;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.axlecho.sakura.utils.SakuraTextUtils;
import com.axlecho.tabgallery.ImageTabInfo;

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

    private static final String IMAGE_PATTERN = "(?<=src=\"http:)//att.jitashe.org/.+?.(?:jpg|png|gif)";
    private static final String IMAGE_PATTERN2 = "(?<=src=\"http:)/data/attachment/forum/.+?.(?:jpg|png|gif)@!tab_thumb";

    private static final String GTP_PATTERN = "dlink=\"/forum.php\\?mod=attachment.*?\"";

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

        model.avatar = e.select("img[src*=http://pic.xiami.net/]").attr("src");
        // model.author = e.select("a[href*=/artist/]").first().text();
        model.author = "???";
        model.title = e.select("a[href*=/tab/]").first().text();
        model.url = e.select("a[href*=/tab/]").first().attr("href");
        model.type = e.select("span.tabtype").first().text();
        // model.uper = e.select("span[title*=发布者]").first().nextElementSibling().text();
        model.uper = e.select("a[href*=/space/]").first().text();
        model.watch = e.select("span[title*=查看]").first().nextElementSibling().text();
        model.reply = e.select("span[title*=回复]").first().nextElementSibling().text();

        // search mode has no time attr
        try {
            model.time = e.select("span.time-line").first().text();
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
        // detail.raw = html;
        detail.formhash = parserFormHash();
        detail.fid = Integer.parseInt(JtsTextUnitls.findByPatternOnce(html, "(?<=fid=)\\d+"));
        detail.threadList = parserThread();
        detail.gtpUrl = parserGtpUrl();
        detail.imgUrls = parserImgUrl();
        if(detail.imgUrls.size() == 0) {
            detail.imgUrls = parserPdfUrl();
        }
        return detail;
    }

    public List<String> parserImgUrl() {

        List<String> imageUrls = new ArrayList<>();

        if (html == null) return imageUrls;
        Document doc = Jsoup.parse(html);
        Element tab = doc.select("div.imgtab").first();

        if (tab == null) {
            return imageUrls;
        }

        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, tab.toString());

        Elements tabimg = tab.select("img[id*=aimg]");


        Iterator it = tabimg.iterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, element.toString());
            String url = element.attr("src");
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, url);
            imageUrls.add(url);
        }

        if (imageUrls.size() == 0) {
            JtsViewerLog.e(TAG, "processAction failed - image url is null");
            return null;
        }

        return imageUrls;
    }

    public List<String> parserPdfUrl() {

        List<String> imageUrls = new ArrayList<>();

        if (html == null) return imageUrls;
        Document doc = Jsoup.parse(html);
        Element tab = doc.select("div#pdfpngContainer").first();

        if (tab == null) {
            return imageUrls;
        }

        JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, tab.toString());

        Elements tabimg = tab.select("img");


        Iterator it = tabimg.iterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, element.toString());
            String url = element.attr("src");
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, url);
            imageUrls.add(url);
        }

        if (imageUrls.size() == 0) {
            JtsViewerLog.e(TAG, "processAction failed - image url is null");
            return null;
        }

        return imageUrls;
    }

    public String parserGtpUrl() {
        List<String> gtpUrls = JtsTextUnitls.findByPattern(html, GTP_PATTERN);
        JtsViewerLog.i(TAG, gtpUrls.toString());

        if (gtpUrls.size() == 0) {
            return null;
        }

        String gtpUrl = gtpUrls.get(0);
        gtpUrl = gtpUrl.replaceAll("dlink=\"", "");
        gtpUrl = gtpUrl.replaceAll("\"$", "");
        gtpUrl = gtpUrl.replaceAll("amp;", "");
        gtpUrl = JtsConf.HOST_URL + gtpUrl;
        return gtpUrl;
    }

    public String parserFormHash() {
        if (html == null) return null;
        Document doc = Jsoup.parse(html);
        return doc.select("input[name=formhash]").first().attr("value");
    }

    public List<JtsThreadModule> parserThread() {
        if (html == null) return null;

        Document doc = Jsoup.parse(html);
        Elements tbodys = doc.select("div.post-item");
        List<JtsThreadModule> moduleList = new ArrayList<>();

        Iterator it = tbodys.iterator();
        while (it.hasNext()) {
            Element c = (Element) it.next();
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, c.toString());
            if(!c.attr("id").matches("post_\\d+")) {
                continue;
            }
            JtsThreadModule module = parserThraed(c);
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, "[parserThread]" + module);
            moduleList.add(module);
        }
        return moduleList;
    }

    private JtsThreadModule parserThraed(Element e) {
        if (e == null) return null;
        JtsThreadModule module = new JtsThreadModule();
        module.authi = e.select("span.authi2").first().text();
        module.avatar = e.select("img[src*=http://att.jitashe.org/data/attachment/avatar/]").attr("src");
        module.time = e.select("em[id*=authorposton]").first().text();
        module.message = e.select("td.t_f").first().html();
        module.floor = e.select("a[onclick*=setCopy]").first().text();
        Elements comments = e.select("div.cmtl");
        Iterator it = comments.iterator();
        while (it.hasNext()) {
            Element c = (Element) it.next();
            // JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, "[parserComment]" + c.toString());
            JtsThreadCommentModule comment = new JtsThreadCommentModule();
            comment.time = c.select("span.xg1").first().text();
            comment.avatar = c.select("img[src*=avatar.php]").attr("src");
            comment.authi = c.select("a.xi2").first().text();
            comment.message = c.select("dd:not(.m)").first().text();
            // JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, "[parserComment]" + comment.toString());
            module.comments.add(comment);

        }
        return module;
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
        String searchId = SakuraTextUtils.search(html, "searchid=(\\d+)");
        return searchId == null ? -1 : Integer.parseInt(searchId);
    }

    public List<JtsCollectionInfo> parserCollection() {
        if (html == null) return null;
        Document doc = Jsoup.parse(html);

        JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG,html);
        Elements tabItems = doc.select("div.xld");
        Iterator it = tabItems.iterator();
        List<JtsCollectionInfo> models = new ArrayList<>();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, element.toString());
            JtsCollectionInfo model = parserCollectionByElement(element);
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, model.toString());
            models.add(model);
        }

        return models;
    }

    public JtsCollectionInfo parserCollectionByElement(Element e) {
        if (e == null) return null;
        JtsCollectionInfo model = new JtsCollectionInfo();
        return model;
    }
}
