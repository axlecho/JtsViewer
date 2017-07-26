package com.axlecho.jtsviewer.action.tab;

import android.content.Context;
import android.content.Intent;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.axlecho.tabgallery.GalleryActivity;
import com.axlecho.tabgallery.ImageTabInfo;

import java.util.List;

public class JtsImageTabAction extends JtsBaseAction {

    private static final String IMAGE_PATTERN = "//att.jitashe.org/.+?.(?:jpg|png)";
    private static final String TAG = JtsImageTabAction.class.getSimpleName();

    public static final String CONTEXT_KEY = "image_tab_action_context";
    public static final String GID_KEY = "image_tab_action_webpage_content";

    @Override
    public void execute() {
        String webpageContent = (String) getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY);
        Context context = (Context) getKey(CONTEXT_KEY);
        long gid = (long) getKey(GID_KEY);
        JtsViewerLog.appendToFile(context, webpageContent);
        JtsViewerLog.d(TAG, "gid " + gid);

        if (webpageContent == null) {
            JtsViewerLog.e(TAG, "execute failed - webpage content is null");
            return;
        }

        if (context == null) {
            JtsViewerLog.e(TAG, "execute failed - context is null");
            return;
        }

        List<String> imageUrl = JtsTextUnitls.findByPattern(webpageContent, IMAGE_PATTERN);
        JtsViewerLog.i(TAG, imageUrl.toString());

        ImageTabInfo info = new ImageTabInfo();
        if (imageUrl.size() == 0) {
            JtsViewerLog.e(TAG, "execute failed - image url is null");
            return;
        }

        for (int i = 0; i < imageUrl.size(); i++) {
            imageUrl.set(i, "http:" + imageUrl.get(i));
        }
        JtsViewerLog.d(TAG, imageUrl.toString());
        info.gid = gid;
        info.imgs = imageUrl.toArray(new String[imageUrl.size()]);

        Intent intent = new Intent();
        intent.setClass(context, GalleryActivity.class);
        intent.setAction("eh");
        intent.putExtra(GalleryActivity.KEY_GALLERY_INFO, info);
        context.startActivity(intent);
    }
}
