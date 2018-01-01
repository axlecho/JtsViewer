package com.axlecho.jtsviewer.action.tab;

import android.content.Context;
import android.content.Intent;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.axlecho.tabgallery.GalleryActivity;
import com.axlecho.tabgallery.ImageTabInfo;

import java.util.List;

public class JtsImgTabAction extends JtsBaseAction {

    private static final String TAG = JtsImgTabAction.class.getSimpleName();

    private List<String> imageUrl;
    private long gid;
    private Context context;

    public JtsImgTabAction(Context context, long gid, List<String> imageUrl) {
        this.context = context;
        this.imageUrl = imageUrl;
        this.gid = gid;
    }

    @Override
    public void processAction() {
        JtsViewerLog.d(TAG, imageUrl.toString());
        ImageTabInfo info = new ImageTabInfo();
        info.gid = gid;
        info.imgs = imageUrl.toArray(new String[imageUrl.size()]);

        Intent intent = new Intent();
        intent.setClass(context, GalleryActivity.class);
        intent.setAction("eh");
        intent.putExtra(GalleryActivity.KEY_GALLERY_INFO, info);
        context.startActivity(intent);
    }
}
