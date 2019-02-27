package com.axlecho.jtsviewer.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.afollestad.aesthetic.AestheticActivity;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.activity.base.JtsCommonSingleTabInfoListActivity;
import com.axlecho.jtsviewer.module.JtsCollectionInfoModel;
import com.axlecho.jtsviewer.network.JtsServer;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.activity.base.JtsBaseRecycleViewAdapter;
import com.axlecho.jtsviewer.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class JtsCollectionActivity extends AestheticActivity implements
        JtsBaseRecycleViewAdapter.OnItemClickListener<JtsCollectionInfoModel>,
        JtsBaseRecycleViewAdapter.OnItemLongClickListener<JtsCollectionInfoModel> {
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
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));


        cacheViewAdapter = new JtsCollectionListAdapter(this, modules);
        cacheViewAdapter.addOnItemClickListener(this);
        cacheViewAdapter.addOnItemLongClickListener(this);
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

    @Override
    public void onItemClick(JtsCollectionInfoModel module,View shareView) {
        long collectionId = JtsTextUnitls.getCollectionIdFromUrl(module.url);
        Intent intent = new Intent();
        intent.putExtra("scene-type", "collection");
        intent.putExtra("collection-id", collectionId);
        intent.setClass(this, JtsCommonSingleTabInfoListActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onItemAvatarClick(JtsCollectionInfoModel module, View shareView) {

    }

    @Override
    public void onItemLongClick(JtsCollectionInfoModel module) {

    }
}