package com.axlecho.jtsviewer.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.afollestad.aesthetic.AestheticActivity;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.activity.base.JtsBaseRecycleViewAdapter;
import com.axlecho.jtsviewer.activity.detail.JtsDetailActivity;
import com.axlecho.jtsviewer.activity.main.JtsTabListAdapter;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.widget.RecycleViewDivider;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class JtsBookMarkConfigureActivity extends AestheticActivity implements
        JtsBaseRecycleViewAdapter.OnItemClickListener<JtsTabInfoModel>,
        JtsBaseRecycleViewAdapter.OnItemLongClickListener<JtsTabInfoModel>,
        JtsBaseRecycleViewAdapter.OnDataEditListener<JtsTabInfoModel> {
    private static final String TAG = "book_mark";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private JtsTabListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark_configure);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));

        adapter = new JtsTabListAdapter(this);
        adapter.addOnItemClickListener(this);
        adapter.addOnItemLongClickListener(this);
        adapter.addOnDataEditListener(this);
        recyclerView.setAdapter(adapter);
        adapter.addData(JtsBookMarkHelper.getSingleton(this).load());
        adapter.enableDragAndDrop(recyclerView);
    }

    @Override
    public void onItemClick(JtsTabInfoModel module, View shareView) {
        startDetailActivity(module, shareView);
    }

    @Override
    public void onItemAvatarClick(JtsTabInfoModel module, View shareView) {

    }

    @Override
    public void onItemLongClick(JtsTabInfoModel module) {

    }

    @Override
    public void onItemSwiped(JtsTabInfoModel module,int pos) {
        JtsBookMarkHelper.getSingleton(this).save(adapter.getData());
        JtsBookMarkHelper.getSingleton(this).notifyDataChange();
    }

    @Override
    public void onItemMoved(JtsTabInfoModel module, int from, int to) {
        JtsBookMarkHelper.getSingleton(this).save(adapter.getData());
        JtsBookMarkHelper.getSingleton(this).notifyDataChange();
    }

    private void startDetailActivity(JtsTabInfoModel model, View shareView) {
        String transition_name = this.getResources().getString(R.string.detail_transition);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, shareView, transition_name);

        Intent intent = new Intent();
        intent.putExtra("tabinfo", model);
        intent.setClass(this, JtsDetailActivity.class);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

}
