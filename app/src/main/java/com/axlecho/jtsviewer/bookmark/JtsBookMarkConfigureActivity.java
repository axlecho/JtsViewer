package com.axlecho.jtsviewer.bookmark;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

import com.axlecho.jtsviewer.R;

import androidx.appcompat.app.AppCompatActivity;

public class JtsBookMarkConfigureActivity extends AppCompatActivity {
    private static final String TAG = "book_mark";
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_book_mark_configure);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        // JtsBookMarkWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId, titlePrefix);
    }
}
