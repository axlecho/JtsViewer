package com.axlecho.jtsviewer.activity.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.afollestad.aesthetic.AestheticActivity;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.network.JtsServer;
import com.axlecho.jtsviewer.untils.JtsConf;
import com.axlecho.jtsviewer.untils.JtsToolUnitls;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.widget.Toolbar;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class JtsLoginActivity extends AestheticActivity implements View.OnClickListener {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressDialog loadingProgressDialog;
    private List<Disposable> disposables = new ArrayList<>();

    @Override
    protected void onDestroy() {
        for (Disposable disposable : disposables) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        disposables.clear();
        super.onDestroy();
    }

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
        findViewById(R.id.register_btn).setOnClickListener(this);
        usernameEditText = (EditText) findViewById(R.id.login_username_edittext);
        passwordEditText = (EditText) findViewById(R.id.login_password_edittext);

        loadingProgressDialog = new ProgressDialog(this);
        loadingProgressDialog.setTitle(null);
        loadingProgressDialog.setMessage(getResources().getString(R.string.login_tip));

        Observable.timer(400, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        showInputMethod();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showError(throwable.getMessage());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn) {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            login(username, password);
        } else if (v.getId() == R.id.register_btn) {
            JtsToolUnitls.openUrl(this, JtsConf.HOST_URL + JtsConf.REGISTER_URL);
        }
    }

    private void login(String username, String password) {
        JtsToolUnitls.hideSoftInput(this, this.getWindow().getDecorView());
        loadingProgressDialog.show();
        Disposable disposable = JtsServer.getSingleton(this).login(username, password)
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
        disposables.add(disposable);
    }

    private void showInputMethod() {
        usernameEditText.setFocusable(true);
        usernameEditText.setFocusableInTouchMode(true);
        usernameEditText.requestFocus();

        JtsToolUnitls.showSoftInput(this, usernameEditText);
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
