package com.axlecho.jtsviewer.module;

public class JtsTabInfoModel {
    public String title;
    public String author;
    public String time;
    public String reply;
    public String watch;
    public String type;
    public String uper;
    public String avatar;

    @Override
    public String toString() {
        return "[title " + title + " author " + author + " time " + time + " reply " + reply + " watch " + watch + " type " + type + " uper " + uper + " avatar " + avatar + "]";
    }
}
