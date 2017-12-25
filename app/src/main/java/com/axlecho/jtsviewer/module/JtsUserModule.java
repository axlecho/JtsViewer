package com.axlecho.jtsviewer.module;

public class JtsUserModule {
    public long uid;
    public String userName;
    public String avatarUrl;
    public String cookies;

    @Override
    public String toString() {
        return "[uid " + uid + " userName " + userName + " avatarUrl " + avatarUrl + "]\n";
    }
}
