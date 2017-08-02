package com.axlecho.jtsviewer.action.tab;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import org.herac.tuxguitar.android.activity.TGActivity;

public class JtsShowGtpTabAction extends JtsBaseAction {
    private static final String TAG = JtsShowGtpTabAction.class.getSimpleName();
    public static final String GTP_FILE_PATH = "show_gtp_file_path";

    @Override
    public void execute() {
        Context context = (Context) getKey(CONTEXT_KEY);
        String filePath = (String) getKey(JtsShowGtpTabAction.GTP_FILE_PATH);
        if (filePath == null) {
            JtsViewerLog.e(TAG, "file path is null");
            return;
        }

        Uri gtpUri = Uri.parse("file://" + filePath);
        Intent intent = new Intent();
        intent.setData(gtpUri);

        Bundle bundle = new Bundle();
        bundle.putSerializable("title", JtsTextUnitls.getFileNameFromPath(filePath));
        intent.putExtras(bundle);

        intent.setAction(Intent.ACTION_VIEW);
        intent.setClass(context, TGActivity.class);
        context.startActivity(intent);
    }
}
