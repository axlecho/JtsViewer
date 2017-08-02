package com.axlecho.jtsviewer.module;

public class UserModule {
    public long uid;
    public String userName;
    public String avatarUrl;

    @Override
    public String toString() {
        return "[uid " + uid + " userName " + userName + " avatarUrl " + avatarUrl + "]\n";
    }
}
