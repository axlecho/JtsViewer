package com.axlecho.jtsviewer.bookmark;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.activity.login.JtsLoginActivity;

/**
 * Implementation of App Widget functionality.
 */
public class JtsBookMarkWidget extends AppWidgetProvider {

    public final static String CLICK_ACTION = "com.stone.action.clickset";
    public final static String CLICK_ITEM_ACTION = "com.stone.action.clickset.item";
    public final static String EXTRA_ITEM = "extra_item";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("XXXX",intent.getAction());
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, JtsLoginActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jts_book_mark_widget);
            views.setOnClickPendingIntent(R.id.btn_test_widget, pendingIntent);

            Intent list = new Intent(context, JtsBookMarkWidgetService.class);
            list.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            list.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(appWidgetId, R.id.bookmark_listview, list);
            views.setEmptyView(R.id.bookmark_listview, R.id.empty_view);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
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

