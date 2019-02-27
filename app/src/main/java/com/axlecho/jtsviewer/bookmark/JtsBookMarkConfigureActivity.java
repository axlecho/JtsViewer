package com.axlecho.jtsviewer.bookmark;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.afollestad.aesthetic.AestheticActivity;
import com.afollestad.materialcab.MaterialCab;
import com.axlecho.jtsviewer.R;

import androidx.appcompat.widget.Toolbar;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class JtsBookMarkConfigureActivity extends AestheticActivity {
    private static final String TAG = "book_mark";
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

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

//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new DragAndDropCallBack(adapter, adapter.getData()));
//        itemTouchHelper.attachToRecyclerView(activity.recyclerView);

        MaterialCab.Companion.attach(this, R.id.cab_stub, new Function1<MaterialCab, Unit>() {
            @Override
            public Unit invoke(MaterialCab materialCab) {
                materialCab.setTitle("选择模式");
                return null;
            }
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

//        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
//            finish();
//        }

        // JtsBookMarkWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId, titlePrefix);
    }
}
