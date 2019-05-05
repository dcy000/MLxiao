package com.example.han.referralproject.yizhinang;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

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
    @TargetApi(Build.VERSION_CODES.O)
    public static String encoder(String str) {
        final Base64.Encoder encoder = Base64.getMimeEncoder();
        String strEncoder = null;
        try {
            strEncoder = encoder.encodeToString(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strEncoder;
    }


}
