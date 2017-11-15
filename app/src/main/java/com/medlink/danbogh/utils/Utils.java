package com.medlink.danbogh.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2017/10/16.
 */

public class Utils {
    private Utils() {
        throw new UnsupportedOperationException("no instance");
    }

    public static final String SMS_KEY = "21298843aea72";

    public static final String SMS_SECRETE = "bd0b6925735818c881252c6245d9ea9d";

    public static boolean isValidPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        return phone.matches("[1][34578]\\d{9}");
    }

    public static void showKeyBroad(EditText view) {
        if (view == null) {
            return;
        }
        InputMethodManager manager = (InputMethodManager)
                view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        if (manager != null) {
            manager.showSoftInput(view, 0);
        }
    }

    public static void hideKeyBroad(View view) {
        if (view == null || !ViewCompat.isAttachedToWindow(view)) {
            return;
        }
        InputMethodManager manager =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isNumeric(String in){
        if (in == null || in.length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(in);
        return isNum.matches();
    }

    public static String chineseMapToNumber(String chinese) {
        if (chinese == null || chinese.length() == 0) {
            return "";
        }

        char[] chars = chinese.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            switch (aChar) {
                case '零':
                    builder.append("0");
                    break;
                case '一':
                    builder.append("1");
                    break;
                case '二':
                case '两':
                    builder.append("2");
                    break;
                case '三':
                    builder.append("3");
                    break;
                case '四':
                    builder.append("4");
                    break;
                case '五':
                    builder.append("5");
                    break;
                case '六':
                    builder.append("6");
                    break;
                case '七':
                    builder.append("7");
                    break;
                case '八':
                    builder.append("8");
                    break;
                case '九':
                    builder.append("9");
                    break;
                case '百':
                    builder.append("00");
                    break;
                default:
                    break;
            }
        }
        return builder.toString();
    }

    public static String removeNonnumeric(String in) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(in);
        return matcher.replaceAll("").trim();
    }

    public static boolean inMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = getProcessName(context);
        return packageName.equals(processName);
    }

    public static String getProcessName(Context context) {
        String processName = null;
        ActivityManager manager = ((ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE));
        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : manager.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }
            }
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }
            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String md5(String text) {
        if (text == null || text.trim().length() == 0) {
            throw new IllegalArgumentException("text == null or empty");
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return bytesToHexString(md5.digest(text.getBytes("utf-8")));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String bytesToHexString(byte[] data) {
        if (data == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int length = data.length;
        for (int i = 0; i < length; i++) {
            String hex = Integer.toHexString(0xFF & data[i]);
            if (hex.length() == 1) {
                builder.append('0');
            }
            builder.append(hex);
        }
        return builder.toString();
    }

    public static String formatCallTime(int seconds) {
        final int hh = seconds / 60 / 60;
        final int mm = seconds / 60 % 60;
        final int ss = seconds % 60;
        return (hh > 9 ? "" + hh : "0" + hh) +
                (mm > 9 ? ":" + mm : ":0" + mm) +
                (ss > 9 ? ":" + ss : ":0" + ss);
    }

    public static String readText(InputStream in) {
        if (in == null) {
            return "";
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String readTextFromAssetFile(Context context, String fileName) {
        AssetManager manager = context.getAssets();
        InputStream in = null;
        try {
            in = manager.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readText(in);
    }
}

