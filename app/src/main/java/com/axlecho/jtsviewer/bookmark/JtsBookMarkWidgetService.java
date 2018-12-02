package com.axlecho.jtsviewer.bookmark;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

public class JtsBookMarkWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetFactory(this.getApplicationContext(), intent);
    }


    public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {
        private static final int mCount = 10;
        private Context mContext;
        private List<String> mWidgetItems = new ArrayList<String>();


        public WidgetFactory(Context context, Intent intent) {
            mContext = context;
//	        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
//	                AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            for (int i = 0; i < mCount; i++) {
                mWidgetItems.add("item:" + i + "!");
            }
        }

        @Override
        public void onDataSetChanged() {
            /*
             * appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listview);
             * 使用该通知更新数据源，会调用onDataSetChanged
             */
            System.out.println("----onDataSetChanged----");
        }

        @Override
        public void onDestroy() {
            mWidgetItems.clear();
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
            views.setTextViewText(android.R.id.text1, "item:" + position);
            System.out.println("RemoteViewsService----getViewAt" + position);


            Bundle extras = new Bundle();
            extras.putInt(JtsBookMarkWidget.EXTRA_ITEM, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            /*
             * android.R.layout.simple_list_item_1 --- id --- text1
             * listview的item click：将fillInIntent发送，
             * fillInIntent它默认的就有action 是provider中使用 setPendingIntentTemplate 设置的action
             */
            views.setOnClickFillInIntent(android.R.id.text1, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            /* 在更新界面的时候如果耗时就会显示 正在加载... 的默认字样，但是你可以更改这个界面
             * 如果返回null 显示默认界面
             * 否则 加载自定义的，返回RemoteViews
             */
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

    }
}
