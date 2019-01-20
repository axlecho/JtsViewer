package com.axlecho.jtsviewer.bookmark;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.module.JtsCollectionInfoModel;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsServer;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;

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

        public WidgetFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            try {
                List<JtsCollectionInfoModel> collectionInfoModelList = JtsServer.getSingleton(mContext).getCollection().blockingFirst();
                if (collectionInfoModelList != null && collectionInfoModelList.size() != 0) {
                    for (JtsCollectionInfoModel collection : collectionInfoModelList) {
                        long id = JtsTextUnitls.getCollectionIdFromUrl(collection.url);
                        mWidgetItems.addAll(JtsServer.getSingleton(mContext).getCollectionDetail(id, 1).blockingFirst());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_bookmark_item);
            rv.setTextViewText(R.id.bookmark_item_title, item.title);

            // set list view item action
            Intent intent = new Intent();
            intent.putExtra("tabinfo", item);
            rv.setOnClickFillInIntent(R.id.bookmark_item_title, intent);
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
