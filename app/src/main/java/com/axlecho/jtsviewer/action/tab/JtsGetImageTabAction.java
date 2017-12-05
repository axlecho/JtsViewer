package com.axlecho.jtsviewer.action.tab;

import android.content.Context;
import android.content.Intent;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.untils.JtsConf;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.axlecho.tabgallery.GalleryActivity;
import com.axlecho.tabgallery.ImageTabInfo;

import java.util.List;

public class JtsGetImageTabAction extends JtsBaseAction {

    private static final String IMAGE_PATTERN = "(?<=src=\"http:)//att.jitashe.org/.+?.(?:jpg|png|gif)@!tab_thumb";
    private static final String IMAGE_PATTERN2 = "(?<=src=\"http:)/data/attachment/forum/.+?.(?:jpg|png|gif)@!tab_thumb";

    private static final String TAG = JtsGetImageTabAction.class.getSimpleName();


    @Override
    public void processAction() {
        String webpageContent = (String) getKey(JtsNetworkManager.WEBPAGE_CONTENT_KEY);
        Context context = (Context) getKey(CONTEXT_KEY);
        long gid = (long) getKey(JtsParseTabTypeAction.GID_KEY);
        JtsViewerLog.appendToFile(context, webpageContent);
        JtsViewerLog.d(TAG, "gid " + gid);

        if (webpageContent == null) {
            JtsViewerLog.e(TAG, "processAction failed - webpage content is null");
            return;
        }

        if (context == null) {
            JtsViewerLog.e(TAG, "processAction failed - context is null");
            return;
        }

        List<String> imageUrl = JtsTextUnitls.findByPattern(webpageContent, IMAGE_PATTERN);
        JtsViewerLog.i(TAG, imageUrl.toString());

        List<String> imageUrl2 = JtsTextUnitls.findByPattern(webpageContent, IMAGE_PATTERN2);
        JtsViewerLog.i(TAG, imageUrl2.toString());

        ImageTabInfo info = new ImageTabInfo();
        if (imageUrl.size() == 0 && imageUrl2.size() == 0) {
            JtsViewerLog.e(TAG, "processAction failed - image url is null");
            return;
        }

        for (int i = 0; i < imageUrl.size(); i++) {
            imageUrl.set(i, "http:" + imageUrl.get(i));
        }

        if(imageUrl.size() == 0) {
            for (String url : imageUrl2) {
                imageUrl.add(JtsConf.HOST_URL + url);
            }
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
