package com.axlecho.jtsviewer.action.user;

import android.content.Context;
import android.content.Intent;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.activity.login.LoginActivity;


public class JtsShowLoginAction extends JtsBaseAction {

    @Override
    public void execute() {
        Context context = (Context) getKey(JtsBaseAction.CONTEXT_KEY);
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
