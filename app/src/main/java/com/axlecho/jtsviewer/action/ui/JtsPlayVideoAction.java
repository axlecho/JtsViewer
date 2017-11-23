package com.axlecho.jtsviewer.action.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.sakura.PlayerView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class JtsPlayVideoAction extends JtsBaseAction {
    private Context context;
    private String videoUrl;
    private View parent;

    public JtsPlayVideoAction(Context context, String videoUrl, View parent) {
        this.context = context;
        this.videoUrl = videoUrl;
        this.parent = parent;
    }

    @Override
    public void processAction() {
        PlayerView player = new PlayerView(context);
        player.setVideoUrl(videoUrl);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            FrameLayout root = (FrameLayout) activity.findViewById(android.R.id.content);
            root.addView(player);

            WindowManager wm = activity.getWindowManager();
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) player.getLayoutParams();
            params.height = width * 1080 / 1920;
            player.setLayoutParams(params);
        }
    }
}
