package com.axlecho.jtsviewer.activity;

import android.content.Intent;
import android.view.View;

import com.axlecho.jtsviewer.action.JtsBaseAction;

public class MainActivityController {

    private static MainActivityController instance;
    private MainActivity activity;

    public static MainActivityController getInstance() {
        if (instance == null) {
            instance = new MainActivityController();
        }
        return instance;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public MainActivity getActivity() {
        return this.activity;
    }

    public void enableFloatingActionButton(JtsBaseAction action) {
        activity.floatingActionButton.setVisibility(View.VISIBLE);
        activity.floatingActionButton.setOnClickListener(action);
    }

    public void disableFloatingActionButton() {
        activity.floatingActionButton.setVisibility(View.GONE);
        activity.floatingActionButton.setOnClickListener(null);
    }

    public void processProgressChanged(int progress) {
        if (progress == 100) {
            activity.progressBar.setVisibility(View.INVISIBLE);
        } else {
            if (View.INVISIBLE == activity.progressBar.getVisibility()) {
                activity.progressBar.setVisibility(View.VISIBLE);
            }
            activity.progressBar.setProgress(progress);
        }
    }

    public void processJumpCache() {
        Intent intent = new Intent();
        intent.setClass(activity, CacheActivity.class);
        activity.startActivity(intent);
    }
}
