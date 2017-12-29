package com.axlecho.jtsviewer.activity.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.network.JtsServer;

import io.reactivex.functions.Consumer;

public class JtsLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressDialog loadingProgressDialog;

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
        passwordEditText = (EditText) findViewById(R.id.login_password_edittext);

        loadingProgressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn) {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            login(username, password);
        }
    }

    private void login(String username, String password) {
        loadingProgressDialog.show();
        JtsServer.getSingleton(this).login(username, password)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (TextUtils.isEmpty(s)) {
                            showResult(false);
                            return;
                        }
                        showResult(true);
                        JtsLoginActivity.this.finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showError(throwable.getMessage());
                    }
                });
    }

    private void showResult(boolean status) {
        loadingProgressDialog.dismiss();
        String msg = status ? "登录成功" : "登录失败";
        Snackbar.make(this.getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG).show();
    }

    private void showError(String msg) {
        loadingProgressDialog.dismiss();
        Snackbar.make(this.getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG).show();
    }
}