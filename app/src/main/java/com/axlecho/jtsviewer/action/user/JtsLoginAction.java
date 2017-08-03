package com.axlecho.jtsviewer.action.user;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.activity.main.MainActivityController;

public class JtsLoginAction extends JtsBaseAction {
    @Override
    public void execute() {
        MainActivityController.getInstance().processLogin();
    }
}
