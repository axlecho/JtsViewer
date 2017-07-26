package com.axlecho.jtsviewer.network.download;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CacheDatabase extends SQLiteOpenHelper {
    private static final String mDatabaseName = "cache";
    private static final int mDatabaseVersion = 1;

    public CacheDatabase(Context context) {
        super(context, mDatabaseName, null, mDatabaseVersion);
    }

    public static final String CACHE_TABLE = "cache_map";
    public static final String CACHE_KEY = "key";
    public static final String CACHE_PATH = "path";
    public static final String CACHE_HASH = "hash";
    public static final String CACHE_CREATETIME = "createtime";
    public static final String CACHE_FREQUENCY = "frequency";
    public static final String CACHE_FILESIZE = "filesize";

    private static final String CREATE_CACHE_TABLE = "create table if not exists " + CACHE_TABLE + "(" +
            CACHE_KEY + " text primary key not null," +
            CACHE_PATH + " text not null unique," +
            CACHE_HASH + " text not null unique," +
            CACHE_CREATETIME + " timestamp default (datetime('now', 'localtime'))," +
            CACHE_FREQUENCY + " integer not null default 0," +
            CACHE_FILESIZE + "  long not null" + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CACHE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insert(CacheInfo cacheInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(CACHE_TABLE, null, cacheInfo.toContentValues());
    }

    public CacheInfo getCacheInfoByKey(String key) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = CACHE_KEY + "=?";
        String[] selectionArgs = {key};
        Cursor c = db.query(CACHE_TABLE, null, selection, selectionArgs, null, null, null);
        if (c != null) {
            // we only take one because the key is unique
            if (c.moveToNext()) {
                return CacheInfo.buildFromCursor(c);
            }
            c.close();
        }

        return null;
    }
}
