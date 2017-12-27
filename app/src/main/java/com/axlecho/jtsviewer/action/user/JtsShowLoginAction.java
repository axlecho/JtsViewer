package com.axlecho.jtsviewer.action.user;

import android.content.Context;
import android.content.Intent;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.activity.login.JtsLoginActivity;


public class JtsShowLoginAction extends JtsBaseAction {

    @Override
    public void processAction() {
        Context context = (Context) getKey(JtsBaseAction.CONTEXT_KEY);
        Intent intent = new Intent();
        intent.setClass(context, JtsLoginActivity.class);
        context.startActivity(intent);
    }
}
