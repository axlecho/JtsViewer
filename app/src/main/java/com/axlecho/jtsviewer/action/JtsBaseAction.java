package com.axlecho.jtsviewer.action;

import android.app.Activity;
import android.view.View;

import java.util.HashMap;

public abstract class JtsBaseAction implements View.OnClickListener {
    public static final String CONTEXT_KEY = "action_context";

    private HashMap<String, Object> param = new HashMap<>();


    public abstract void processAction();

    public void executeOnUiThread() {
        Activity activity = (Activity) getKey(CONTEXT_KEY);
        if (activity == null) {
            processAction();
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                processAction();
            }
        });
    }

    public void execute() {
        processAction();
    }

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

}
