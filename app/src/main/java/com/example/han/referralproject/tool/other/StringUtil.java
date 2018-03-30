package com.example.han.referralproject.tool.other;

/**
 * Created by lenovo on 2018/3/8.
 */

public class StringUtil {
    public static String formatTime(int recTime) {
        int minute = recTime / 1000 / 60;
        int second = (recTime / 1000) % 60;
        return String.format(" %02d:%02d ", minute, second);
    }
}
