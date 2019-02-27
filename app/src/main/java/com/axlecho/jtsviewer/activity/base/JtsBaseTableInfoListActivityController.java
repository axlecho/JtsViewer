package com.axlecho.jtsviewer.activity.base;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.view.View;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.activity.main.JtsTabListAdapter;
import com.axlecho.jtsviewer.bookmark.JtsBookMarkWidget;
import com.axlecho.jtsviewer.bookmark.JtsBookMarkWidgetService;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import androidx.appcompat.app.AppCompatActivity;


public abstract class JtsBaseTableInfoListActivityController implements JtsBaseController,
        JtsBaseRecycleViewAdapter.OnItemClickListener<JtsTabInfoModel>,
        JtsBaseRecycleViewAdapter.OnItemLongClickListener<JtsTabInfoModel> {

    private static final String TAG = JtsBaseTableInfoListActivityController.class.getSimpleName();
    private AppCompatActivity activity;
    private JtsBaseRecycleViewAdapter adapter;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
        this.adapter = new JtsTabListAdapter(activity);

        adapter.addOnItemClickListener(this);
        adapter.addOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(JtsTabInfoModel module, View shareView) {
        JtsViewerLog.d(TAG, module.url);
        startDetailActivity(module, shareView);
    }

    @Override
    public void onItemAvatarClick(JtsTabInfoModel module, View shareView) {

    }

    @Override
    public void onItemLongClick(JtsTabInfoModel module) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(activity);
        ComponentName cn = new ComponentName(activity, JtsBookMarkWidget.class);

        JtsBookMarkWidgetService.addItem(module);
        mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.bookmark_listview);
    }
}
