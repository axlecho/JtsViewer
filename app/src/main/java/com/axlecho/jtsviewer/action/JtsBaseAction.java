package com.axlecho.jtsviewer.action;

import java.util.HashMap;

public abstract class JtsBaseAction {
    private HashMap<String, Object> param = new HashMap<>();

    public abstract void execute();

    public void setKey(String key, Object o) {
        param.put(key, o);
    }

    public Object getKey(String key) {
        return param.get(key);
    }
}
