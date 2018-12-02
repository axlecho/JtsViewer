package com.axlecho.jtsviewer.activity.my;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.module.JtsCollectionInfoModel;
import com.axlecho.jtsviewer.network.JtsServer;
import com.axlecho.jtsviewer.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class JtsCollectionActivity extends AppCompatActivity {
    // implements CacheViewAdapter.OnItemClickListener, CacheViewAdapter.OnItemLongClickListener {
    private static final String TAG = JtsCollectionActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private JtsCollectionListAdapter cacheViewAdapter;

    private List<JtsCollectionInfoModel> modules = new ArrayList<>();
    private Disposable disposable;

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

        recyclerView = (RecyclerView) findViewById(R.id.cache_recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));


        cacheViewAdapter = new JtsCollectionListAdapter(this, modules);
        // cacheViewAdapter.addOnItemClickListener(this);
        // cacheViewAdapter.addOnItemLongClickListener(this);
        recyclerView.setAdapter(cacheViewAdapter);

        disposable = JtsServer.getSingleton(this).getCollection()
                .subscribe(new Consumer<List<JtsCollectionInfoModel>>() {
                    @Override
                    public void accept(List<JtsCollectionInfoModel> jtsCollectionInfoModels) throws Exception {
                        modules.addAll(jtsCollectionInfoModels);
                        cacheViewAdapter.notifyDataSetChanged();
                    }
                });
    }


    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}