package com.axlecho.jtsviewer.untils;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import com.axlecho.jtsviewer.R;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;
import retrofit2.Response;


public class JtsTextUnitls {
    private static final String TAG = JtsTextUnitls.class.getSimpleName();
    public static int CONST_PLAYER_ID = 100001;

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
        if (path == null || !path.contains("/tab/")) {
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

    public static long getCollectionIdFromUrl(String url) {
        if (url == null) {
            JtsViewerLog.e(TAG, "get collection id from url failed -- url is null");
            return -1;
        }

        String path = Uri.parse(url).getPath();
        if (path == null || !path.contains("/collection/")) {
            JtsViewerLog.e(TAG, "get collection id from url failed -- url is not a collection page");
            return -1;
        }

        JtsViewerLog.d(TAG, "url path -- " + path);
        for (String s : path.split("/")) {
            if (s.matches("\\d+")) {
                return Long.parseLong(s);
            }
        }

        JtsViewerLog.e(TAG, "get collection id  from url failed -- path error " + path);
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

    public static String getTitleFromPath(String path) {
        String fileName = getFileNameFromPath(path);
        return removePostfixFromFileName(fileName);
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

    public static String getErrorMessageFromException(Context context, Throwable e) {
        Resources res = context.getResources();
        if (e instanceof UnknownHostException) {
            return res.getString(R.string.error_network_unknow_host);
        } else if (e instanceof SocketTimeoutException) {
            return res.getString(R.string.error_network_time_out);
        } else {
            return e.getMessage();
        }
    }

    /***
     * compare for version code(ignore 'v' prefix)
     * @param version1
     * @param version2
     * @return Positive version1 > version2 ,Negative version1 > version2,0 version1 == version2
     * @throws Exception
     */
    public static int compareVersion(String version1, String version2) throws Exception {


        version1 = findByPatternOnce(version1, "[.\\d]+");
        version2 = findByPatternOnce(version2, "[.\\d]+");
        if (version1 == null || version2 == null) {
            throw new IllegalArgumentException("compareVersion error:illegal params.");
        }

        // remove v and split
        String[] versionArray1 = version1.split("\\.");
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

    public static String sizeFormat(long size) {

        if (size < 0) {
            throw new IllegalArgumentException();
        }

        double M = 1024 * 1024;
        double K = 1024;

        String s;
        if (size >= M) {
            s = String.format(Locale.CHINA, "%.2f MB", (double) size / M);
        } else if (size >= K) {
            s = String.format(Locale.CHINA, "%.2f KB", (double) size / K);
        } else {
            s = String.format(Locale.CHINA, "%d B", size);
        }
        return s;
    }

    public static String getFileNameFromResponse(Response response) {
        Headers headers = response.headers();
        List<String> contentDispoition = headers.values("Content-Disposition");
        String fileName = null;
        for (String s : contentDispoition) {
            List<String> fileNameGroup = findByPattern(s, "(?<=filename=\").*?(?=\")");
            if (fileNameGroup.size() > 0) {
                fileName = fileNameGroup.get(0);
                break;
            }
        }

        if (fileName == null) {
            fileName = getRandomUUID();
        }

        return fileName;
    }
}
