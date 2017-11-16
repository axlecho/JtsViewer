package com.axlecho.jtsviewer.untils;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

    public static String findByPatternOnce(String s, String reg) {
        List<String> result = findByPattern(s, reg);
        if (result != null && result.size() > 0) {
            return result.get(0);
        }

        return null;
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

    public static String getRandomUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String getFileNameFromPath(String path) {
        int start = path.lastIndexOf("/");
        int end = path.lastIndexOf(".");

        if (start != -1 && end != -1 && end > start) {
            return path.substring(start + 1, end);
        } else {
            return "";
        }
    }

    public static String removePostfixFromFileName(String fileName) {
        int end = fileName.lastIndexOf(".");
        if (end == -1) {
            return fileName;
        }

        return fileName.substring(0, end);
    }

    public static String getResizePicUrl(String url, int width, int height) {
        String target = url.replace("w_100", "w_" + width);
        target = target.replace("h_100", "h_" + height);
        return target;
    }
}
