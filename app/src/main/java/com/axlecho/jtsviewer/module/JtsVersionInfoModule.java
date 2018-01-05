package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

public class JtsVersionInfoModule {
    private String html_url;
    private String tag_name;
    private String name;
    private String body;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
