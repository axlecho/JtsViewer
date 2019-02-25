package com.axlecho.jtsviewer.action.tab;

import android.content.Context;
import android.content.Intent;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.activity.tab.JtsTextTabActivity;

public class JtsTextTabAction extends JtsBaseAction {

    private String data;
    private Context context;

    public JtsTextTabAction(Context context, String data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public void processAction() {
        Intent intent = new Intent();
        intent.setClass(context, JtsTextTabActivity.class);
        intent.putExtra(JtsTextTabActivity.KEY_TEXT_INFO, data);
        context.startActivity(intent);
    }
}
