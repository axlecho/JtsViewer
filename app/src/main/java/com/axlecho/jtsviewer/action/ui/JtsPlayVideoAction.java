package com.axlecho.jtsviewer.action.ui;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.sakura.PlayerView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class JtsPlayVideoAction extends JtsBaseAction {
    private Context context;
    private String videoUrl;

    public JtsPlayVideoAction(Context context, String videoUrl) {
        this.context = context;
        this.videoUrl = videoUrl;
    }

    @Override
    public void processAction() {

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            FrameLayout root = (FrameLayout) activity.findViewById(android.R.id.content);

            PlayerView player = (PlayerView) root.findViewById(R.id.player);
            if (player == null) {
                player = new PlayerView(context);
                player.setId(R.id.player);
                root.addView(player);

                WindowManager wm = activity.getWindowManager();
                int width = wm.getDefaultDisplay().getWidth();
                int height = wm.getDefaultDisplay().getHeight();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) player.getLayoutParams();
                params.height = width * 1080 / 1920;
                player.setLayoutParams(params);
            }

            player.stop();
            player.setVideoUrl(videoUrl);
        }
    }
}
