package com.axlecho.jtsviewer.activity.tab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.os.Bundle;
import android.widget.TextView;

import com.axlecho.jtsviewer.R;

public class JtsTextTabActivity extends AppCompatActivity {

    public static final String KEY_TEXT_INFO = "key_text_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_tab);

        String data = getIntent().getStringExtra(KEY_TEXT_INFO);
        TextView dataTextView = findViewById(R.id.text_tab_data);
        dataTextView.setText(HtmlCompat.fromHtml(data, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }
}
