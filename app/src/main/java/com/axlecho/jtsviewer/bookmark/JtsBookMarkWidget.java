package com.axlecho.jtsviewer.bookmark;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.andryr.guitartuner.TunerActivity;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.activity.detail.JtsDetailDialogActivity;

 import james.metronome.activities.MetronomeActivity;


public class JtsBookMarkWidget extends AppWidgetProvider {

    public final static String UPDATE_BOOKMARK_ACTION = "com.axlecho.action.update_bookmark";

    public void updateWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context, JtsBookMarkWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.bookmark_listview);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null && intent.getAction().equals(UPDATE_BOOKMARK_ACTION)) {
            updateWidget(context);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews bookmark = new RemoteViews(context.getPackageName(), R.layout.widget_bookmark);

            // set button action
            Intent clickIntent = new Intent(UPDATE_BOOKMARK_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);
            bookmark.setOnClickPendingIntent(R.id.btn_setting, pendingIntent);

            Intent metronomeIntent = new Intent(context, MetronomeActivity.class);
            PendingIntent metronomePendingIntent = PendingIntent.getActivity(context,0,metronomeIntent,0);
            bookmark.setOnClickPendingIntent(R.id.btn_metronome, metronomePendingIntent);

            Intent tunnerIntent = new Intent(context,TunerActivity.class);
            PendingIntent tunnerPendingIntent = PendingIntent.getActivity(context,0,tunnerIntent,0);
            bookmark.setOnClickPendingIntent(R.id.btn_tuner, tunnerPendingIntent);


            // set list view template action
            Intent listDataSrc = new Intent(context, JtsBookMarkWidgetService.class);
            listDataSrc.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            listDataSrc.setData(Uri.parse(listDataSrc.toUri(Intent.URI_INTENT_SCHEME)));
            bookmark.setRemoteAdapter(appWidgetId, R.id.bookmark_listview, listDataSrc);
            bookmark.setEmptyView(R.id.bookmark_listview, R.id.empty_view);

            Intent toastIntent = new Intent(context, JtsDetailDialogActivity.class);
            toastIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            PendingIntent toastPendingIntent = PendingIntent.getActivity(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            bookmark.setPendingIntentTemplate(R.id.bookmark_listview, toastPendingIntent);


            appWidgetManager.updateAppWidget(appWidgetId, bookmark);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}

