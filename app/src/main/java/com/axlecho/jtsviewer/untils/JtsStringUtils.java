package com.axlecho.jtsviewer.untils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hiroshi on 2016/9/3.
 */
public class JtsStringUtils {

    private static final String TAG = JtsStringUtils.class.getSimpleName();

    public static String search(String data, String patternStr) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            String found = matcher.group(1);
            return found;
        }
        return null;
    }

    public static String urlDecode(String url) {
        try {
            return URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            JtsViewerLog.w(TAG, "decode failed " + url);
        }
        return null;
    }


    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format(Locale.CHINA, "%02d:%02d:%02d", hours, minutes, seconds)
                : String.format(Locale.CHINA, "%02d:%02d", minutes, seconds);
    }


    public static boolean endWith(String str, String... args) {
        if (str != null) {
            for (String arg : args) {
                if (str.endsWith(arg)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String filter(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("\\|\\\\\\?\\*<\":\\+\\[\\]/'", "");
    }

    public static boolean isEmpty(String... args) {
        for (String arg : args) {
            if (arg == null || arg.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static String split(String str, String regex, int position) {
        if (str == null) {
            return null;
        }
        String[] array = str.split(regex);
        if (position < 0) {
            position = array.length + position;
        }
        return position < 0 || position >= array.length ? null : array[position];
    }

    public static String replaceAll(String str, String regex, String replacement) {
        if (str == null) {
            return null;
        }
        return str.replaceAll(regex, replacement);
    }

    public static String substring(String str, int start) {
        return substring(str, start, -1);
    }

    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (end < 0) {
            end = str.length() + 1 + end;
        }
        if (start >= 0 && start <= str.length()) {
            return str.substring(start, end);
        }
        return null;
    }

    public static String format(String format, Object... args) {
        return String.format(Locale.getDefault(), format, args);
    }

    public static String getProgress(int progress, int max) {
        return format("%d/%d", progress, max);
    }

    public static String getFormatTime(String format, long time) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date(time));
    }

    public static String getDateStringWithSuffix(String suffix) {
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()).concat(".").concat(suffix);
    }

    public static String match(String regex, String input, int group) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                return matcher.group(group).trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] match(String regex, String input, int... group) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                String[] result = new String[group.length];
                for (int i = 0; i != result.length; ++i) {
                    result[i] = matcher.group(group[i]);
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
