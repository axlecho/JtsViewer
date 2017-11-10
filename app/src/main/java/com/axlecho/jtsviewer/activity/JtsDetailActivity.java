package com.axlecho.jtsviewer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.axlecho.jtsviewer.R;

public class JtsDetailActivity extends AppCompatActivity {

    public RecyclerView recyclerView;

    public ImageView avatar;
    public TextView title;
    public TextView reply;
    public TextView watch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        avatar = (ImageView) findViewById(R.id.tab_detail_avatar);
        title = (TextView) findViewById(R.id.tab_detail_title);
        reply = (TextView) findViewById(R.id.tab_detail_reply);
        watch = (TextView) findViewById(R.id.tab_detail_watch);

        JtsDetailActivityController.getInstance().attachToActivity(this);
        JtsDetailActivityController.getInstance().getTabDetail();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JtsDetailActivityController.getInstance().detachToActivity();
    }
}
