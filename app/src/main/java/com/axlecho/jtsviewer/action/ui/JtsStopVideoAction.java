package com.axlecho.jtsviewer.action.ui;

import android.app.Activity;
import android.content.Context;
import android.widget.FrameLayout;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.sakura.SakuraPlayerView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class JtsStopVideoAction extends JtsBaseAction {
    private Context context;

    public JtsStopVideoAction(Context context) {
        this.context = context;
    }

    @Override
    public void processAction() {

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            FrameLayout root = (FrameLayout) activity.findViewById(android.R.id.content);

            SakuraPlayerView player = (SakuraPlayerView) root.findViewById(R.id.player);
            if (player != null) {
                player.stop();
                root.removeView(player);
            }
        }
    }
}
