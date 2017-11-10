package com.axlecho.jtsviewer.activity.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.user.JtsLoginAction;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameEditText;
    private EditText keywordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login_btn).setOnClickListener(this);
        usernameEditText = (EditText) findViewById(R.id.login_username_edittext);
        keywordEditText = (EditText) findViewById(R.id.login_keyword_edittext);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn) {
            JtsLoginAction action = new JtsLoginAction();
            String username = usernameEditText.getText().toString();
            String keyword = keywordEditText.getText().toString();
            action.setKey(JtsBaseAction.CONTEXT_KEY, this.getApplicationContext());
            action.setKey(JtsLoginAction.USERNAME_KEY, username);
            action.setKey(JtsLoginAction.KEYWORD_KEY, keyword);
            action.processAction();
        }
    }
}
