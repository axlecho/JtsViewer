package com.axlecho.jtsviewer.network.download;

import android.content.ContentValues;
import android.database.Cursor;

public class CacheInfo {
    public String key;
    public String path;
    public String hash;
    public long createtime;
    public int frequency;
    public long filesize;

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(CacheDatabase.CACHE_KEY, key);
        cv.put(CacheDatabase.CACHE_PATH, path);
        cv.put(CacheDatabase.CACHE_HASH, hash);
        cv.put(CacheDatabase.CACHE_FILESIZE, filesize);
        cv.put(CacheDatabase.CACHE_FREQUENCY, frequency);
        return cv;
    }

    public static CacheInfo buildFromCursor(Cursor c) {
        CacheInfo ci = new CacheInfo();
        ci.key = c.getString(c.getColumnIndex(CacheDatabase.CACHE_KEY));
        ci.path = c.getString(c.getColumnIndex(CacheDatabase.CACHE_PATH));
        ci.hash = c.getString(c.getColumnIndex(CacheDatabase.CACHE_HASH));
        ci.createtime = c.getLong(c.getColumnIndex(CacheDatabase.CACHE_CREATETIME));
        ci.frequency = c.getInt(c.getColumnIndex(CacheDatabase.CACHE_FREQUENCY));
        ci.filesize = c.getLong(c.getColumnIndex(CacheDatabase.CACHE_FILESIZE));
        return ci;
    }
}
