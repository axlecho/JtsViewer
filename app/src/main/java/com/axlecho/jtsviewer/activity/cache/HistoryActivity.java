package com.axlecho.jtsviewer.activity.cache;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.ui.JtsShowGtpTabAction;
import com.axlecho.jtsviewer.activity.JtsDetailActivity;
import com.axlecho.jtsviewer.cache.CacheManager;
import com.axlecho.jtsviewer.module.CacheModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.axlecho.jtsviewer.widget.RecycleViewDivider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements CacheViewAdapter.OnItemClickListener, CacheViewAdapter.OnItemLongClickListener {
    private static final String TAG = HistoryActivity.class.getSimpleName();
    private List<CacheModule> modules;
    private RecyclerView cacheView;
    private RecyclerView.LayoutManager layoutManager;
    private CacheViewAdapter cacheViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cacheView = (RecyclerView) findViewById(R.id.cache_recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cacheView.setLayoutManager(layoutManager);
        cacheView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));

        CacheManager.getInstance(this).reloadModule();
        this.modules = CacheManager.getInstance(this).getModule();
        JtsViewerLog.d(JtsViewerLog.CACHE_MODULE, TAG, modules.toString());

        cacheViewAdapter = new CacheViewAdapter(this, modules);
        cacheViewAdapter.addOnItemClickListener(this);
        cacheViewAdapter.addOnItemLongClickListener(this);
        cacheView.setAdapter(cacheViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Collections.sort(modules);
        cacheViewAdapter.notifyDataSetChanged();
    }

    private void processClickAction(CacheModule module) {

        module.frequency++;
        try {
            module.writeToFile();
        } catch (IOException e) {
            JtsViewerLog.e(TAG, "update tab info failed");
        }

        JtsShowGtpTabAction action = new JtsShowGtpTabAction();
        action.setKey(JtsBaseAction.CONTEXT_KEY, this.getApplicationContext());
        action.setKey(JtsShowGtpTabAction.GTP_FILE_PATH, module.path + File.separator + module.fileName);
        action.processAction();
    }

    private void processLongClickAction(CacheModule module) {
        Intent launcherIntent = new Intent();
        launcherIntent.setAction("android.intent.action.VIEW");
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launcherIntent.setData(Uri.fromFile(new File(module.path + File.separator + module.fileName)));
        launcherIntent.addCategory("android.intent.category.DEFAULT");
        launcherIntent.setComponent(new ComponentName("com.axlecho.jtsviewer", "org.herac.tuxguitar.android.activity.TGActivity"));

        final Intent addShortcutIntent = new Intent();
        addShortcutIntent.putExtra("duplicate", false);
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, module.tabInfo.title);
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher));
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
        addShortcutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

        Glide.with(this).load(module.tabInfo.avatar).asBitmap().into(new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, resource);
                HistoryActivity.this.sendBroadcast(addShortcutIntent);
                Snackbar.make(HistoryActivity.this.getWindow().getDecorView(),
                        HistoryActivity.this.getResources().getString(R.string.add_short_cut),
                        Snackbar.LENGTH_LONG).show();
            }
        });

    }
    

    @Override
    public void onItemClick(CacheModule module) {
        processClickAction(module);
    }

    @Override
    public void onItemAvatarClick(JtsTabInfoModel module,View shareView) {
        this.startDetailActivity(module,shareView);
    }

    @Override
    public void onItemLongClick(CacheModule module) {
        processLongClickAction(module);
    }

    public void startDetailActivity(JtsTabInfoModel model, View shareView) {

        String transition_name = this.getResources().getString(R.string.detail_transition);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, shareView, transition_name);

        Intent intent = new Intent();
        intent.putExtra("tabinfo", model);
        intent.setClass(this, JtsDetailActivity.class);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
}
