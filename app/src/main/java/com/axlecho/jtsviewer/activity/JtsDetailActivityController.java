package com.axlecho.jtsviewer.activity;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.tab.JtsParseTabTypeAction;
import com.axlecho.jtsviewer.action.tab.JtsParseThreadAction;
import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsConf;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2017/11/7.
 */

public class JtsDetailActivityController {
    private JtsDetailActivity activity;
    private JtsThreadListAdapter adapter;
    private JtsTabDetailModule detail;
    private JtsTabInfoModel info;

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


    public void attachToActivity(JtsDetailActivity activity) {
        this.activity = activity;
        this.bindTabInfo();
    }

    public void getTabDetail() {
        this.info = (JtsTabInfoModel) activity.getIntent().getSerializableExtra("tabinfo");
        JtsNetworkManager.getInstance(activity).get(JtsConf.DESKTOP_HOST_URL + info.url, createDetailInfoProcessor());
    }

    public void processDetail(JtsTabDetailModule detail) {
        this.detail = detail;

        this.registerListener();
        if (adapter == null) {
            adapter = new JtsThreadListAdapter(activity);
            activity.recyclerView.setAdapter(adapter);
        }

        adapter.addData(detail.threadList);
    }

    public void bindTabInfo() {
        JtsTabInfoModel model = (JtsTabInfoModel) activity.getIntent().getSerializableExtra("tabinfo");
        Picasso.with(activity).load(JtsTextUnitls.getResizePicUrl(model.avatar,200,300)).into(activity.avatar);
        activity.title.setText(model.title);
        activity.author.setText(model.author);
    }

    public void detachToActivity() {
        this.activity = null;
        this.adapter = null;
    }

    public void registerListener() {
        activity.findViewById(R.id.tab_detail_play).setOnClickListener(createPlayProcessor());
    }

    public JtsBaseAction createPlayProcessor() {
        JtsViewerLog.appendToFile(activity,detail.raw);
        JtsParseTabTypeAction action = new JtsParseTabTypeAction();
        action.setKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY, detail.raw);
        action.setKey(JtsBaseAction.CONTEXT_KEY, activity);
        action.setKey(JtsParseTabTypeAction.GID_KEY, JtsTextUnitls.getTabKeyFromUrl(info.url));
        return action;
    }

    public JtsBaseAction createDetailInfoProcessor() {
        JtsParseThreadAction action = new JtsParseThreadAction(activity);
        action.setKey(JtsBaseAction.CONTEXT_KEY, activity);
        return action;
    }
}
