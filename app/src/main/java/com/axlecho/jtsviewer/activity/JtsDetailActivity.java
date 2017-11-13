package com.axlecho.jtsviewer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.widget.RecycleViewDivider;

public class JtsDetailActivity extends AppCompatActivity {

    public RecyclerView recyclerView;

    public ImageView avatar;
    public TextView title;
    public TextView author;
    public PopupMenu popupMenu;
    public View otherActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        recyclerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));

        avatar = (ImageView) findViewById(R.id.tab_detail_avatar);
        title = (TextView) findViewById(R.id.tab_detail_title);
        author = (TextView) findViewById(R.id.tab_detail_author);
        otherActions = findViewById(R.id.tab_detail_other_actions);

        JtsDetailActivityController.getInstance().attachToActivity(this);
        JtsDetailActivityController.getInstance().getTabDetail();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JtsDetailActivityController.getInstance().detachToActivity();
    }


    public void initPopMenu() {
        if (popupMenu != null) {
            return;
        }

        popupMenu = new PopupMenu(this, otherActions, Gravity.TOP);
        popupMenu.getMenuInflater().inflate(R.menu.scene_tab_detail, popupMenu.getMenu());
        JtsDetailActivityController.getInstance().initPopMenuAction();
    }

    public void popMenu() {
        popupMenu.show();
    }
}
