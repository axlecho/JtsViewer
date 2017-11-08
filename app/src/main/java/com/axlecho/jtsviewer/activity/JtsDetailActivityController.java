package com.axlecho.jtsviewer.activity;

import com.axlecho.jtsviewer.action.tab.JtsParseTabTypeAction;
import com.axlecho.jtsviewer.action.tab.JtsParseThreadAction;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.module.JtsThreadModule;
import com.axlecho.jtsviewer.network.JtsConf;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class JtsDetailActivityController {
    private JtsDetailActivity activity;
    private JtsThreadListAdapter adapter;
    private static JtsDetailActivityController instance;

    public static JtsDetailActivityController getInstance() {
        if (instance == null) {
            synchronized (JtsDetailActivity.class) {
                if (instance == null) {
                    instance = new JtsDetailActivityController();
                }
            }
        }
        return instance;
    }


    public void setActivity(JtsDetailActivity activity) {
        this.activity = activity;
    }

    public void getTabDetail() {
        JtsTabInfoModel model = (JtsTabInfoModel) activity.getIntent().getSerializableExtra("tabinfo");
        // JtsParseTabTypeAction action = new JtsParseTabTypeAction();
        // action.setKey(CONTEXT_KEY, context);
        // action.setKey(JtsParseTabTypeAction.GID_KEY, JtsTextUnitls.getTabKeyFromUrl(url));

        JtsParseThreadAction action = new JtsParseThreadAction(activity);
        action.setKey(JtsParseTabTypeAction.GID_KEY, JtsTextUnitls.getTabKeyFromUrl(model.url));
        JtsNetworkManager.getInstance(activity).get(JtsConf.DESKTOP_HOST_URL + model.url, action);
    }

    public void processThreadData(List<JtsThreadModule> threads) {
        if (adapter == null) {
            adapter = new JtsThreadListAdapter(activity);
            activity.recyclerView.setAdapter(adapter);
        }

        adapter.addData(threads);
    }

    public void bindTabInfo() {
        JtsTabInfoModel model = (JtsTabInfoModel) activity.getIntent().getSerializableExtra("tabinfo");
        Picasso.with(activity).load(model.avatar).into(activity.avatar);
        activity.title.setText(model.title);
        activity.watch.setText(model.watch);
        activity.reply.setText(model.reply);
    }

    public void clearData() {
        this.activity = null;
        this.adapter = null;
    }
}
