package com.axlecho.jtsviewer.untils;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JtsTextUnitls {
    private static final String TAG = JtsTextUnitls.class.getSimpleName();

    public static List<String> findByPattern(String s, String reg) {
        if (s == null || reg == null) {
            return null;
        }

        ArrayList<String> result = new ArrayList<>();
        Pattern pat = Pattern.compile(reg);
        Matcher mat = pat.matcher(s);
        while (mat.find()) {
            result.add(mat.group());
        }
        return result;
    }

    public static long getTabKeyFromUrl(String url) {
        if (url == null) {
            JtsViewerLog.e(TAG, "get tabkey from url failed -- url is null");
            return -1;
        }

        String path = Uri.parse(url).getPath();
        if (!path.contains("/tab/")) {
            JtsViewerLog.e(TAG, "get tabkey from url failed -- url is not a tab page");
            return -1;
        }

        JtsViewerLog.d(TAG, "url path -- " + path);
        for (String s : path.split("/")) {
            if (s.matches("\\d+")) {
                return Long.parseLong(s);
            }
        }

        JtsViewerLog.e(TAG, "get tabkey from url failed -- path error " + path);
        return -1;
    }
}
