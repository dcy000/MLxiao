package com.medlink.danbogh.utils;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.HashMap;

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

    public static String chineseToNumber(String chinese) {
        if (chinese == null || chinese.length() == 0) {
            return "";
        }
        char[] chars = chinese.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
                case '零':
                    chars[i] = '0';
                    break;
                case '一':
                    chars[i] = '1';
                    break;
                case '二':
                    chars[i] = '2';
                    break;
                case '三':
                    chars[i] = '3';
                    break;
                case '四':
                    chars[i] = '4';
                    break;
                case '五':
                    chars[i] = '5';
                    break;
                case '六':
                    chars[i] = '6';
                    break;
                case '七':
                    chars[i] = '7';
                    break;
                case '八':
                    chars[i] = '8';
                    break;
                case '九':
                    chars[i] = '9';
                    break;
                default:
                    break;
            }
        }
        return new String(chars);
    }
}

