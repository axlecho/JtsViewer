package com.axlecho.jtsviewer.network;

import com.axlecho.jtsviewer.module.JtsRelateCollectionModule;
import com.axlecho.jtsviewer.module.JtsRelatedPostModule;
import com.axlecho.jtsviewer.module.JtsRelatedTabModule;
import com.axlecho.jtsviewer.module.JtsRelatedVideoModule;
import com.axlecho.jtsviewer.module.JtsThreadCommentModule;
import com.axlecho.jtsviewer.module.JtsThreadModule;
import com.axlecho.jtsviewer.untils.JtsConf;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JtsDetailPageParserHelper {
    private static final String TAG = JtsPageParser.class.getSimpleName();
    private static final String IMAGE_PATTERN = "(?<=src=\"http:)//att.jitashe.org/.+?.(?:jpg|png|gif)";
    private static final String IMAGE_PATTERN2 = "(?<=src=\"http:)/data/attachment/forum/.+?.(?:jpg|png|gif)@!tab_thumb";
    private static final String GTP_PATTERN = "dlink=\"/forum.php\\?mod=attachment.*?\"";

    public static String parserFormHash(String html) {
        if (html == null) return null;
        Document doc = Jsoup.parse(html);
        return doc.select("input[name=formhash]").first().attr("value");
    }

    public static int parserFid(String html) {
        String fid = JtsTextUnitls.findByPatternOnce(html, "(?<=fid=)\\d+");

        return fid == null ? -1 : Integer.parseInt(fid);
    }

    public static List<JtsThreadModule> parserThread(String html) {
        if (html == null) return null;

        Document doc = Jsoup.parse(html);
        Elements tbodys = doc.select("div.post-item");
        List<JtsThreadModule> moduleList = new ArrayList<>();

        Iterator it = tbodys.iterator();
        while (it.hasNext()) {
            Element c = (Element) it.next();
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, c.toString());
            if (!c.attr("id").matches("post_\\d+")) {
                continue;
            }
            JtsThreadModule module = parserThraedItem(c);
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE, TAG, "[parserThread]" + module);
            moduleList.add(module);
        }
        return moduleList;
    }

    private static JtsThreadModule parserThraedItem(Element e) {
        if (e == null) return null;
        JtsThreadModule module = new JtsThreadModule();
        Element authiObject = e.select("span.authi2").first();
        module.authi = authiObject != null ? authiObject.text() : null;
        // module.avatar = e.select("img[src*=http://att.jitashe.org/data/attachment/avatar/]").attr("src");
        try {
            module.avatar = e.select("div.avatar").first().child(0).child(0).attr("src");
        } catch (Exception ex) {
            module.avatar = "";
        }
        module.time = e.select("em[id*=authorposton]").first().text();
        module.time = module.time.replaceAll("发表于 ", "");
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
            comment.time = comment.time.replaceAll("发表于 ", "");
            module.comments.add(comment);

        }
        return module;
    }

    public static List<String> parserImgUrl(String html) {

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

    public static List<String> parserPdfUrl(String html) {

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

    public static String parserGtpUrl(String html) {
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

    public static String parserInfo(String html) {
        if (html == null) {
            return null;
        }
        Document doc = Jsoup.parse(html);
        Element locationObject = doc.select("div.panel-h:contains(曲谱信息)").first();
        if (locationObject == null) {
            JtsViewerLog.w(JtsViewerLog.NETWORK_MODULE, TAG, "info model not found");
            return null;
        }
        Element infoObject = locationObject.nextElementSibling();
        return infoObject == null ? null : infoObject.html();
    }

    public static String parserLyric(String html) {
        if (html == null) {
            return null;
        }

        Document doc = Jsoup.parse(html);
        Element lyricObject = doc.select("p.lyric").first();
        if (lyricObject == null) {
            return null;
        }

        return lyricObject.html();
    }

    public static Document parserDocument(String html)  {
        if (html == null) {
            JtsViewerLog.w(JtsViewerLog.NETWORK_MODULE, TAG, "html is null");
            return null;
        }

        return Jsoup.parse(html);
    }

    public static Element parserRelatedObject(Document doc, String type){
        int index = -1;
        Element locationObject = doc.select("label.flex-fill:contains(" + type+ ")").first();
        if (locationObject == null) {
            JtsViewerLog.w(JtsViewerLog.NETWORK_MODULE, TAG, type +"model not found");
            return null;
        }

        String forAttr = locationObject.attr("for");
        if (forAttr == null) {
            JtsViewerLog.w(JtsViewerLog.NETWORK_MODULE, TAG, type +"attr for not found");
            return null;
        }

        try {
            index = Integer.parseInt(forAttr.substring(forAttr.length() - 1));
        } catch (NumberFormatException e) {
            JtsViewerLog.w(JtsViewerLog.NETWORK_MODULE, TAG, type + "index not found - " + forAttr);
            return null;
        }

        Element content = doc.select("div.tabpanel-c").first();
        if (content == null) {
            JtsViewerLog.w(JtsViewerLog.NETWORK_MODULE, TAG, "content panel not found - ");
            return null;
        }

        Element relatedObject = content.child(index - 1);

        if (relatedObject == null) {
            JtsViewerLog.w(JtsViewerLog.NETWORK_MODULE, TAG, "related tab not found");
            return null;
        }

        return relatedObject;
    }

    public static List<JtsRelatedVideoModule> parserVideo(String html) {
        Document doc = parserDocument(html);
        if(doc == null) {
            JtsViewerLog.w(JtsViewerLog.NETWORK_MODULE,TAG,"parser document failed");
            return null;
        }

        Element relatedTabObject = parserRelatedObject(doc,"视频");
        if(relatedTabObject == null) {
            JtsViewerLog.w(JtsViewerLog.NETWORK_MODULE,TAG,"get related Object failed for video");
            return null;
        }
        Elements relatedVideosObject = relatedTabObject.select("a[vid]");
        List<JtsRelatedVideoModule> relatedVideoModules = new ArrayList<>();
        Iterator it = relatedVideosObject.iterator();
        while (it.hasNext()) {
            Element c = (Element) it.next();
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE,TAG,c.toString());
            JtsRelatedVideoModule module = new JtsRelatedVideoModule();
            module.title = c.select("p.v-title").first().text();
            module.url = c.attr("href");
            module.thumbnail = c.select("img[onerror]").first().attr("src");
            module.info = c.select("span.v_info").first().text();
            relatedVideoModules.add(module);
        }
        return relatedVideoModules;
    }

    public static List<JtsRelatedTabModule> parserRelatedTab(String html) {
        Document doc = parserDocument(html);
        if(doc == null) {
            JtsViewerLog.w(JtsViewerLog.NETWORK_MODULE,TAG,"parser document failed");
            return null;
        }

        Element relatedTabObject = parserRelatedObject(doc,"曲谱");
        if(relatedTabObject == null) {
            JtsViewerLog.w(JtsViewerLog.NETWORK_MODULE,TAG,"get related Object failed for tab");
            return null;
        }

        Elements relatedTabsObject = relatedTabObject.select("a[href]");
        List<JtsRelatedTabModule> relatedTabs = new ArrayList<>();
        Iterator it = relatedTabsObject.iterator();
        while (it.hasNext()) {
            Element c = (Element) it.next();
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE,TAG,c.toString());
            JtsRelatedTabModule module = new JtsRelatedTabModule();
            module.title = c.attr("title");
            module.url = c.attr("href");
            relatedTabs.add(module);
        }
        return relatedTabs;
    }

    public static List<JtsRelatedPostModule> parserRelatedPost(String html) {
        Document doc = parserDocument(html);
        if(doc == null) {
            JtsViewerLog.w(JtsViewerLog.NETWORK_MODULE,TAG,"parser document failed");
            return null;
        }

        Element relatedTabObject = parserRelatedObject(doc,"帖子");
        if(relatedTabObject == null) {
            JtsViewerLog.w(JtsViewerLog.NETWORK_MODULE,TAG,"get related Object failed for posts");
            return null;
        }

        Elements relatedTabsObject = relatedTabObject.select("a[href]");
        List<JtsRelatedPostModule> relatedTabs = new ArrayList<>();
        Iterator it = relatedTabsObject.iterator();
        while (it.hasNext()) {
            Element c = (Element) it.next();
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE,TAG,c.toString());
            JtsRelatedPostModule module = new JtsRelatedPostModule();
            module.title = c.attr("title");
            module.url = c.attr("href");
            relatedTabs.add(module);
        }
        return relatedTabs;
    }

    public static List<JtsRelateCollectionModule> parserRelatedCollection(String html) {
        Document doc = parserDocument(html);
        if(doc == null) {
            JtsViewerLog.w(JtsViewerLog.NETWORK_MODULE,TAG,"parser document failed");
            return null;
        }

        Element relatedTabObject = parserRelatedObject(doc,"琴包");
        if(relatedTabObject == null) {
            JtsViewerLog.w(JtsViewerLog.NETWORK_MODULE,TAG,"get related Object failed for collections");
            return null;
        }

        Elements relatedTabsObject = relatedTabObject.select("a[href]");
        List<JtsRelateCollectionModule> relatedTabs = new ArrayList<>();
        Iterator it = relatedTabsObject.iterator();
        while (it.hasNext()) {
            Element c = (Element) it.next();
            JtsViewerLog.i(JtsViewerLog.NETWORK_MODULE,TAG,c.toString());
            JtsRelateCollectionModule module = new JtsRelateCollectionModule();
            module.title = c.attr("title");
            module.url = c.attr("href");
            relatedTabs.add(module);
        }
        return relatedTabs;
    }

}
