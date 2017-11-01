package com.axlecho.jtsviewer.activity.cache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.cache.CacheManager;
import com.axlecho.jtsviewer.module.CacheModule;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.axlecho.jtsviewer.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
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
        cacheView.setAdapter(cacheViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Collections.sort(modules);
        cacheViewAdapter.notifyDataSetChanged();
    }
}
