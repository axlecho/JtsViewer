package com.axlecho.jtsviewer.bookmark;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class JtsBookMarkHelper {
    private volatile static JtsBookMarkHelper singleton;
    private static final String BOOKMARK_PREFERENCES = "bookmark";
    private static final String BOOKMARK_KEY = "data";
    private Context context;
    private SharedPreferences preferences;

    private JtsBookMarkHelper(Context context) {
        this.context = context.getApplicationContext();
        this.preferences = this.context.getSharedPreferences(BOOKMARK_PREFERENCES, Context.MODE_PRIVATE);
    }

    public List<JtsTabInfoModel> load() {
        String raw = this.preferences.getString(BOOKMARK_KEY, "[]");

        return new Gson().fromJson(raw, new TypeToken<List<JtsTabInfoModel>>() {
        }.getType());
    }

    public void save(List<JtsTabInfoModel> data) {
        String raw = new Gson().toJson(data);
        this.preferences.edit().putString(BOOKMARK_KEY, raw).apply();
    }

    public void add(JtsTabInfoModel data) {
        List<JtsTabInfoModel> t = load();
        t.add(data);
        save(t);
    }

    public void remove(JtsTabInfoModel data) {
        List<JtsTabInfoModel> t = load();
        for(JtsTabInfoModel info:t) {
            if(info.url.equalsIgnoreCase(data.url)) {
                t.remove(info);
                break;
            }
        }

        save(t);
    }

    public void notifyDataChange() {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        ComponentName cn = new ComponentName(context, JtsBookMarkWidget.class);

        mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.bookmark_listview);
    }

    public static JtsBookMarkHelper getSingleton(Context context) {
        if (singleton == null) {
            synchronized (JtsBookMarkHelper.class) {
                if (singleton == null) {
                    singleton = new JtsBookMarkHelper(context);
                }
            }
        }
        return singleton;
    }
}
