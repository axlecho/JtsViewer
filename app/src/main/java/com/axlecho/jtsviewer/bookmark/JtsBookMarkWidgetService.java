package com.axlecho.jtsviewer.bookmark;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsServer;

import java.util.ArrayList;
import java.util.List;

public class JtsBookMarkWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetFactory(this.getApplicationContext(), intent);
    }

    public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context mContext;
        private List<JtsTabInfoModel> mWidgetItems = new ArrayList<>();
        private int mAppWidgetId;

        public WidgetFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            mWidgetItems = JtsServer.getSingleton(mContext).getCollectionDetail(244939, 1).blockingFirst();

        }

        @Override
        public void onDestroy() {
            mWidgetItems.clear();
        }

        @Override
        public int getCount() {
            return mWidgetItems.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            JtsTabInfoModel item = mWidgetItems.get(position);
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
            rv.setTextViewText(android.R.id.text1, item.title);

            // set list view item action
            Intent intent = new Intent();
            intent.putExtra("tabinfo", item);
            rv.setOnClickFillInIntent(android.R.id.text1, intent);
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
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
