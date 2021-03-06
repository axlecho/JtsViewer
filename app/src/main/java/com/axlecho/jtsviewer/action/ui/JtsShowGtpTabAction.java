package com.axlecho.jtsviewer.action.ui;

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

    private Context context;
    private String filePath;

    public JtsShowGtpTabAction(Context context, String filePath) {
        this.context = context;
        this.filePath = filePath;
    }

    @Override
    public void processAction() {
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
