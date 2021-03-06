package com.gcml.common.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {
    @SuppressLint("MissingPermission")
    public static String getDeviceId(ContentResolver contentResolver) {
        return Settings.System.getString(contentResolver, Settings.System.ANDROID_ID);
    }

    public static boolean isValidPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        return phone.matches("[1][34578]\\d{9}");
    }

    @SuppressLint("MissingPermission")
    public static String getMacAddress(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wm.getConnectionInfo().getMacAddress();
    }

    public static int ageByBirthday(String birthday) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        int age;
        try {
            Date date = format.parse(birthday);
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            Calendar birth = Calendar.getInstance();
            birth.setTime(date);
            if (birth.after(now)) {
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int age(String idCard) {
        if (TextUtils.isEmpty(idCard)
                || idCard.length() != 18) {
            return 0;
        }
        try {
            int birthYear = Integer.valueOf(idCard.substring(6, 10));
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            if (birthYear > currentYear) {
                return 0;
            }
            return currentYear - birthYear;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
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

    public static String getLocalVersionName(Context context) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }


    public static String getChineseNumber(int number) {
        String[] str = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String ss[] = new String[]{"", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿"};
        String s = String.valueOf(number);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            String index = String.valueOf(s.charAt(i));
            sb = sb.append(str[Integer.parseInt(index)]);
        }
        String sss = String.valueOf(sb);
        int i = 0;
        for (int j = sss.length(); j > 0; j--) {
            sb = sb.insert(j, ss[i++]);
        }
        return sb.toString();
    }

    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg);
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    public static boolean checkIdCard1(String idCard) {
        return idCard != null
                && idCard.length() == 18
                && isDate(idCard.substring(6, 14));
    }

    public static boolean checkIdCard(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return false;
        }
        char[] chars = idCard.toCharArray();
        int length = chars.length - 1;
        int[] ratios = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        int[] tails = {1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2};
        int sum = 0;
        int i;
        for (i = 0; i < length; i++) {
            if (chars[i] < '0' && chars[i] > '9') {
                return false;
            }
            sum += (chars[i] - '0') * ratios[i];
        }
        if (!(chars[i] == 'x'
                || chars[i] == 'X'
                || (chars[i] >= '0' && chars[i] <= '9'))) {
            return false;
        }
        int value;
        if (chars[i] == 'x' || chars[i] == 'X') {
            value = 10;
        } else {
            value = chars[i] - '0';
        }
        return value == tails[sum % 11] && isDate(new String(chars, 6, 8));
    }

    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern.compile(
                "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");
        Matcher m = pattern.matcher(strDate);
        return m.matches();
    }

    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
