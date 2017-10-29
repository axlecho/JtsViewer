package com.axlecho.jtsviewer.action.ui;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.activity.main.MainActivityController;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;

import java.util.ArrayList;
import java.util.List;


public class JtsLoadMoreAction extends JtsBaseAction {
    public static final String DATA_KEY = "data_key";

    @Override
    public void execute() {
        List<JtsTabInfoModel> data = (ArrayList<JtsTabInfoModel>) getKey(DATA_KEY);
        MainActivityController.getInstance().getScene().processLoadMore(data);
    }
}
