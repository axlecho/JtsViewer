package com.axlecho.jtsviewer.cache;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.amulyakhare.textdrawable.TextDrawable;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.module.CacheModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.axlecho.jtsviewer.widget.GlideRoundTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

public class CacheManager {

    private static final String TAG = CacheManager.class.getSimpleName();
    private final static String INFO_FILE_NAME = "info.json";

    private static CacheManager instance;
    private Context context;
    private String cachePath;
    private List<CacheModule> moduleList;

    private CacheManager(Context context) {
        this.context = context.getApplicationContext();
        this.moduleList = new ArrayList<>();
        this.loadCachePath();
        this.loadCacheModule();
    }

    public static CacheManager getInstance(Context context) {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager(context);
                }
            }
        }

        return instance;
    }

    private void loadCachePath() {
        SharedPreferences preferences = context.getSharedPreferences("cache", Context.MODE_PRIVATE);
        cachePath = preferences.getString("path", getDefaultCachePath());
    }

    private String getDefaultCachePath() {
        return context.getDir("download_cache", Context.MODE_PRIVATE).getPath();
    }

    private void loadCacheModule() {
        // check cache dir is ready
        File root = new File(cachePath);
        if (!root.exists() || !root.isDirectory()) {
            JtsViewerLog.e(JtsViewerLog.CACHE_MODULE, TAG, "cache dir is not ready");
            return;
        }

        // go through all the subdir
        for (File f : root.listFiles()) {
            JtsViewerLog.d(JtsViewerLog.CACHE_MODULE, TAG, "process " + f.getAbsolutePath());
            if (f.isDirectory()) {
                File info = new File(f.getAbsolutePath() + File.separator + "info.json");
                if (!info.exists()) {
                    continue;
                }

                try {
                    CacheModule module = CacheModule.loadFromFile(info);
                    moduleList.add(module);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<CacheModule> getModule() {
        return moduleList;
    }

    public CacheModule getModule(long gid) {
        for (CacheModule module : moduleList) {
            if (Long.parseLong(module.gid) == gid) {
                return module;
            }
        }
        return null;
    }

    public String getCachePath() {
        return cachePath;
    }

    public void reloadModule() {
        moduleList.clear();
        loadCacheModule();
    }

    public void cacheInfo(long gid, String path, JtsTabInfoModel tabInfo) {
        if (findCacheByGid(gid)) {
            return;
        }
        CacheModule cacheInfo = new CacheModule();
        cacheInfo.path = path;
        cacheInfo.gid = String.valueOf(gid);
        cacheInfo.type = "gtp";
        cacheInfo.frequency = 0;
        cacheInfo.tabInfo = tabInfo;
        try {
            writeToFile(cacheInfo);
        } catch (IOException e) {
            JtsViewerLog.e(TAG, "cache info failed " + e.getMessage());
        }

        reloadModule();
    }

    public void writeToFile(CacheModule cache) throws IOException {
        String infoString = cache.toJson();

        JtsViewerLog.d(TAG, "cache to json " + infoString);

        if (infoString == null) {
            JtsViewerLog.e(TAG, "prase info to json failed");
            return;
        }

        String path = getCachePath() + File.separator + cache.gid;

        if (!new File(path).exists()) {
            JtsViewerLog.e(TAG, "path " + path + " is not exists");
            return;
        }

        File info = new File(path, INFO_FILE_NAME);
        if (info.exists()) {
            info.delete();
        }

        if (!info.createNewFile()) {
            JtsViewerLog.e(TAG, "create info file failed " + info.getAbsolutePath());
            return;
        }

        FileWriter writer = new FileWriter(info);
        writer.write(infoString);
        writer.close();
    }

    private boolean findCacheByGid(long gid) {
        for (CacheModule cache : moduleList) {
            if (cache.gid.equals(String.valueOf(gid))) {
                return true;
            }

        }
        return false;
    }

    public Observable<Bitmap> generateShortcutFromCache(final CacheModule cache) {
        Intent launcherIntent = new Intent();
        launcherIntent.setAction("android.intent.action.VIEW");
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launcherIntent.setData(Uri.fromFile(new File(cache.path)));
        launcherIntent.addCategory("android.intent.category.DEFAULT");
        launcherIntent.setComponent(new ComponentName("com.axlecho.jtsviewer", "org.herac.tuxguitar.android.activity.TGActivity"));

        final Intent addShortcutIntent = new Intent();
        addShortcutIntent.putExtra("duplicate", false);
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, cache.tabInfo.title);
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher));
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
        addShortcutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");


        Observable<Bitmap> network = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(final ObservableEmitter<Bitmap> emitter) throws Exception {
                Target<Bitmap> target = new SimpleTarget<Bitmap>(144, 144) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        emitter.onNext(resource);
                        emitter.onComplete();
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        e.printStackTrace();
                        emitter.onComplete();
                    }
                };

                Glide.with(context).load(cache.tabInfo.avatar).asBitmap()
                        .transform(new GlideRoundTransform(context))
                        .into(target);
            }
        });

        Observable<Bitmap> location = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(final ObservableEmitter<Bitmap> emitter) throws Exception {
                TextDrawable defaultDrawable = TextDrawable.builder()
                        .beginConfig().height(128).width(128).bold().endConfig()
                        .buildRoundRect(cache.tabInfo.title.substring(0, 1),
                                context.getResources().getColor(R.color.colorPrimary), 8);

                Bitmap bitmap = Bitmap.createBitmap(
                        defaultDrawable.getIntrinsicWidth(),
                        defaultDrawable.getIntrinsicHeight(),
                        defaultDrawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);

                Canvas canvas = new Canvas(bitmap);
                defaultDrawable.setBounds(0, 0, defaultDrawable.getIntrinsicWidth(),
                        defaultDrawable.getIntrinsicHeight());
                defaultDrawable.draw(canvas);
                emitter.onNext(bitmap);
                emitter.onComplete();
            }
        });

        return Observable.concat(network, location).take(1).doOnNext(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap) throws Exception {
                addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
                context.sendBroadcast(addShortcutIntent);
            }
        });
    }
}
