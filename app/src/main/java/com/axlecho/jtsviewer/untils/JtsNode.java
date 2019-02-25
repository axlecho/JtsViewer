package com.axlecho.jtsviewer.untils;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by Hiroshi on 2016/9/11.
 */
public class JtsNode {

    private Element element;

    public JtsNode(String html) {
        this.element = Jsoup.parse(html).body();
    }

    public JtsNode(Element element) {
        this.element = element;
    }

    public JtsNode id(String id) {
        return new JtsNode(element.getElementById(id));
    }

    public List<JtsNode> list(String cssQuery) {
        List<JtsNode> list = new LinkedList<>();
        Elements elements = element.select(cssQuery);
        for (Element e : elements) {
            list.add(new JtsNode(e));
        }
        return list;
    }

    public Element get() {
        return element;
    }

    public String text() {
        try {
            return element.text().trim();
        } catch (Exception e) {
            return null;
        }
    }

    public String text(String cssQuery) {
        try {
            return element.select(cssQuery).first().text().trim();
        } catch (Exception e) {
            return null;
        }
    }

    public String html() {
        try {
            return element.html();
        } catch (Exception e) {
            return null;
        }
    }

    public String html(String cssQuery) {
        try {
            return element.select(cssQuery).first().html();
        } catch (Exception e) {
            return null;
        }
    }

    public String textWithSubstring(String cssQuery, int start, int end) {
        return JtsStringUtils.substring(text(cssQuery), start, end);
    }

    public String textWithSubstring(String cssQuery, int start) {
        return textWithSubstring(cssQuery, start, -1);
    }

    public String textWithSplit(String cssQuery, String regex, int index) {
        return JtsStringUtils.split(text(cssQuery), regex, index);
    }

    public String attr(String attr) {
        try {
            return element.attr(attr).trim();
        } catch (Exception e) {
            return null;
        }
    }

    public String attr(String cssQuery, String attr) {
        try {
            return element.select(cssQuery).first().attr(attr).trim();
        } catch (Exception e) {
            return null;
        }
    }

    public String attrWithSubString(String attr, int start, int end) {
        return JtsStringUtils.substring(attr(attr), start, end);
    }

    public String attrWithSubString(String attr, int start) {
        return attrWithSubString(attr, start, -1);
    }

    public String attrWithSubString(String cssQuery, String attr, int start, int end) {
        return JtsStringUtils.substring(attr(cssQuery, attr), start, end);
    }

    public String attrWithSubString(String cssQuery, String attr, int start) {
        return attrWithSubString(cssQuery, attr, start, -1);
    }

    public String attrWithSplit(String attr, String regex, int index) {
        return JtsStringUtils.split(attr(attr), regex, index);
    }

    public String attrWithSplit(String cssQuery, String attr, String regex, int index) {
        return JtsStringUtils.split(attr(cssQuery, attr), regex, index);
    }

    public String src() {
        return attr("src");
    }

    public String src(String cssQuery) {
        return attr(cssQuery, "src");
    }

    public String href() {
        return attr("href");
    }

    public String href(String cssQuery) {
        return attr(cssQuery, "href");
    }

    public String hrefWithSubString(int start, int end) {
        return attrWithSubString("href", start, end);
    }

    public String hrefWithSubString(int start) {
        return hrefWithSubString(start, -1);
    }

    public String hrefWithSubString(String cssQuery, int start, int end) {
        return attrWithSubString(cssQuery, "href", start, end);
    }

    public String hrefWithSubString(String cssQuery, int start) {
        return hrefWithSubString(cssQuery, start, -1);
    }

    public String hrefWithSplit(int index) {
        return splitHref(href(), index);
    }

    public String hrefWithSplit(String cssQuery, int index) {
        return splitHref(href(cssQuery), index);
    }

    private String splitHref(String str, int index) {
        if (str == null) {
            return null;
        }
        str = str.replaceFirst(".*\\..*?/", "");
        str = str.replaceAll("[/\\.=\\?]", " ");
        str = str.trim();
        return JtsStringUtils.split(str, "\\s+", index);
    }

}
