package com.axlecho.jtsviewer.module;

import com.google.gson.Gson;

import java.util.List;

public class JtsVersionInfoModule {
    private String html_url;
    private String tag_name;
    private String name;
    private String body;
    private List<Assets> assets;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public String getHtml_url() {
        return html_url;
    }


    public String getTag_name() {
        return tag_name;
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

    public List<Assets> getAssets() {
        return assets;
    }

    public class Assets {
        private String name;
        private int size;
        private String browser_download_url;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
        
        public String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }

        public String getBrowser_download_url() {
            return browser_download_url;
        }
    }
}
