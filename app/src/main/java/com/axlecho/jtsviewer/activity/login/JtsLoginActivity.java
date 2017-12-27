package com.axlecho.jtsviewer.activity.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.axlecho.jtsviewer.R;

public class JtsLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameEditText;
    private EditText keywordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.login_btn).setOnClickListener(this);
        usernameEditText = (EditText) findViewById(R.id.login_username_edittext);
        keywordEditText = (EditText) findViewById(R.id.login_password_edittext);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn) {
            String username = usernameEditText.getText().toString();
            String keyword = keywordEditText.getText().toString();
        }
    }
}
