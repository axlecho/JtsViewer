package com.axlecho.jtsviewer.action.ui;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.activity.main.MainActivityController;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;

import java.util.ArrayList;
import java.util.List;

public class JtsRefreshAction extends JtsBaseAction {

    public static final String DATA_KEY = "data_key";
    public static final String SEARCH_KEY = "search_key";

    @Override
    public void processAction() {
        List<JtsTabInfoModel> data = (ArrayList<JtsTabInfoModel>) getKey(DATA_KEY);
        int searchKey = (Integer) getKey(SEARCH_KEY);
        MainActivityController.getInstance().getScene().processRefreah(data);
        MainActivityController.getInstance().getScene().setSearchKey(searchKey);
    }
}
