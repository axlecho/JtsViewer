package com.axlecho.jtsviewer.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.afollestad.aesthetic.AestheticActivity;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.ui.JtsShowGtpTabAction;
import com.axlecho.jtsviewer.activity.detail.JtsDetailActivity;
import com.axlecho.jtsviewer.cache.CacheManager;
import com.axlecho.jtsviewer.module.CacheModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.axlecho.jtsviewer.widget.RecycleViewDivider;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;

public class JtsHistoryActivity extends AestheticActivity implements CacheViewAdapter.OnItemClickListener, CacheViewAdapter.OnItemLongClickListener {
    private static final String TAG = JtsHistoryActivity.class.getSimpleName();
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
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
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
            CacheManager.getInstance(this).writeToFile(module);
        } catch (IOException e) {
            JtsViewerLog.e(TAG, "update tab info failed");
        }

        JtsShowGtpTabAction action = new JtsShowGtpTabAction(this.getApplicationContext(), module.path);
        action.processAction();
    }

    private void processLongClickAction(final CacheModule module) {
        CacheManager.getInstance(this).generateShortcutFromCache(module)
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        Snackbar.make(JtsHistoryActivity.this.getWindow().getDecorView(),
                                JtsHistoryActivity.this.getResources().getString(R.string.add_short_cut),
                                Snackbar.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    public void onItemClick(CacheModule module) {
        processClickAction(module);
    }

    @Override
    public void onItemAvatarClick(JtsTabInfoModel module, View shareView) {
        this.startDetailActivity(module, shareView);
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
