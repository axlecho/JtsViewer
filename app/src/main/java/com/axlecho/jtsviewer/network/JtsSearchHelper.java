package com.axlecho.jtsviewer.network;

import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class JtsSearchHelper {
    private static final String TAG = JtsSearchHelper.class.getSimpleName();
    private volatile static JtsSearchHelper singleton;
    private Map<String, Integer> searchKeyMap;

    private JtsSearchHelper() {
        this.searchKeyMap = new HashMap<>();
    }

    public static JtsSearchHelper getSingleton() {
        if (singleton == null) {
            synchronized (JtsSearchHelper.class) {
                if (singleton == null) {
                    singleton = new JtsSearchHelper();
                }
            }
        }
        return singleton;
    }

    public void updateKey(String keyword, int searchKey) {
        searchKeyMap.put(keyword, searchKey);
    }

    public int getSearchKey(String keyword) {
        Integer searchKey = searchKeyMap.get(keyword);
        return searchKey == null ? -1 : searchKey;
    }

    public void dump() {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        JtsViewerLog.d(TAG, gson.toJson(searchKeyMap));
    }
}
