package com.axlecho.jtsviewer.action;

import android.view.View;

import java.util.HashMap;

public abstract class JtsBaseAction implements View.OnClickListener, Runnable {
    public static final String CONTEXT_KEY = "action_context";

    private HashMap<String, Object> param = new HashMap<>();

    public abstract void execute();

    public void setKey(String key, Object o) {
        param.put(key, o);
    }


    public Object getKey(String key) {
        return param.get(key);
    }

    @Override
    public void onClick(View v) {
        execute();
    }

    @Override
    public void run() {
        execute();
    }

}
