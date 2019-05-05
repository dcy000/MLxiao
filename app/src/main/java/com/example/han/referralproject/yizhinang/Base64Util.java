package com.example.han.referralproject.yizhinang;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Created by lenovo on 2019/5/5.
 */

public class Base64Util {
    /**
     * base64 编码
     *
     * @param str
     * @return
     */
    @SuppressLint("NewApi")
    public static String encoder(String str) {
        @SuppressLint({"NewApi", "LocalSuppress"}) final Base64.Encoder encoder = Base64.getMimeEncoder();
        String strEncoder = null;
        try {
            strEncoder = encoder.encodeToString(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strEncoder;
    }


}
