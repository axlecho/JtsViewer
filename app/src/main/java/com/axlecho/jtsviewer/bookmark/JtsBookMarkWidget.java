package com.axlecho.jtsviewer.bookmark;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.axlecho.jtsviewer.R;

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
        System.out.println(intent.getAction());
        if (TextUtils.equals(CLICK_ACTION, intent.getAction())) {
            int extraType = intent.getIntExtra("view_tag", 0);
            if (extraType > 0) {
                System.out.println("extra:::" + extraType);

                switch (extraType) {
                    case 1:
                        updateWidget(context, R.id.listview, R.id.gridview, R.id.stackview, R.id.viewflipper);
                        break;
                    case 2:
                        updateWidget(context, R.id.gridview, R.id.listview, R.id.stackview, R.id.viewflipper);
                        break;
                    case 3:
                        updateWidget(context, R.id.stackview, R.id.gridview, R.id.listview, R.id.viewflipper);
                        break;
                    case 4:
                        updateWidget(context, R.id.viewflipper, R.id.gridview, R.id.stackview, R.id.listview);
                        break;

                    default:
                        break;
                }
            }
        } else if (TextUtils.equals(CLICK_ITEM_ACTION, intent.getAction())) {
            Bundle extras = intent.getExtras();
            int position = extras.getInt(JtsBookMarkWidget.EXTRA_ITEM, -1);
            if (position != -1) {
                System.out.println("--点击了item---" + position);
                System.out.println("");
//				Toast.makeText(context, "click item:" + position, 0).show();
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jts_book_mark_widget);

        Intent intent1 = new Intent(CLICK_ACTION);
        intent1.putExtra("view_tag", 1);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 101, intent1, 0);
        views.setOnClickPendingIntent(R.id.btn_listview, pendingIntent1);

        Intent intent2 = new Intent(CLICK_ACTION);
        intent2.putExtra("view_tag", 2);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 102, intent2, 0);
        views.setOnClickPendingIntent(R.id.btn_gridview, pendingIntent2);

        Intent intent3 = new Intent(CLICK_ACTION);
        intent3.putExtra("view_tag", 3);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context, 103, intent3, 0);
        views.setOnClickPendingIntent(R.id.btn_stackview, pendingIntent3);

        Intent intent4 = new Intent(CLICK_ACTION);
        intent4.putExtra("view_tag", 4);
        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(context, 104, intent4, 0);
        views.setOnClickPendingIntent(R.id.btn_viewflipper, pendingIntent4);

        appWidgetManager.updateAppWidget(appWidgetIds, views);

        System.out.println("setwidget update");
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context,
                                          AppWidgetManager appWidgetManager, int appWidgetId,
                                          Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
                newOptions);
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

    private void updateWidget(Context context, int visible, int gone1, int gone2, int gone3) {
        //RemoteViews处理异进程中的View
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jts_book_mark_widget);

        views.setViewVisibility(visible, View.VISIBLE);
        views.setViewVisibility(gone1, View.GONE);
        views.setViewVisibility(gone2, View.GONE);
        views.setViewVisibility(gone3, View.GONE);

        if (visible != R.id.viewflipper) {//viewflipper 不是 继承自AbsListView  or  AdapterViewAnimator  的view
            Intent intent = new Intent(context, JtsBookMarkWidgetService.class);
            views.setRemoteAdapter(visible, intent);//设置集合的adapter为intent指定的service
            views.setEmptyView(visible, R.id.tv_empty);//指定集合view为空时显示的view

            Intent toIntent = new Intent(CLICK_ITEM_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 200, toIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            /*
             * setPendingIntentTemplate 设置pendingIntent 模板
             * setOnClickFillInIntent   可以将fillInIntent 添加到pendingIntent中
             */
            views.setPendingIntentTemplate(visible, pendingIntent);

        } else if (visible == R.id.viewflipper) {
//			views.setPendingIntentTemplate(R.id.viewflipper, pendingIntentTemplate);
        }

        AppWidgetManager am = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = am.getAppWidgetIds(new ComponentName(context, JtsBookMarkWidgetService.class));
        for (int i = 0; i < appWidgetIds.length; i++) {
            am.updateAppWidget(appWidgetIds[i], views); //更新 实例
        }

    }
}

