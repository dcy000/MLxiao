package com.medlink.danbogh.utils;

import android.text.TextUtils;

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
}
