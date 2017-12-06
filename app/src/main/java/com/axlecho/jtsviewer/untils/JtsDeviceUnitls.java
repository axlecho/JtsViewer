package com.axlecho.jtsviewer.untils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by axlecho on 17-12-6.
 */

public class JtsDeviceUnitls {

    public static String getVersionName(Context context) throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return pInfo.versionName;      ///或者 pInfo.versionCode;
    }
}
