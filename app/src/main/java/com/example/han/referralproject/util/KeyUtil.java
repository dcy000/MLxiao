package com.example.han.referralproject.util;


import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class KeyUtil {

    public static String API_KEY = "jYBB0hDwnH6EJmBe_yiy-evErDQAlMrO";
    public static String API_SECRET = "bPfzihMf3GD6sZtmPRD9MTREJx2reYs9";

    public static boolean isReadKey(Context context) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count = -1;
        try {
            inputStream = context.getAssets().open("key");
            while ((count = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, count);
            }
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String str = new String(byteArrayOutputStream.toByteArray());
        String authKey = "jYBB0hDwnH6EJmBe_yiy-evErDQAlMrO";
        String authScrect = "bPfzihMf3GD6sZtmPRD9MTREJx2reYs9";
        try {
            String[] strs = str.split(";");
            authKey = strs[0].trim();
            authScrect = strs[1].trim();
        } catch (Exception e) {
        }
        API_KEY = authKey;
        API_SECRET = authScrect;
        if (API_KEY == null || API_SECRET == null)
            return false;

        return true;
    }


}
