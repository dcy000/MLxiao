package com.gcml.common.utils.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by lenovo on 2018/3/8.
 */

public class StringUtil {
    public static String formatTime(int recTime) {
        int minute = recTime / 1000 / 60;
        int second = (recTime / 1000) % 60;
        return String.format(" %02d:%02d ", minute, second);
    }

    /**
     * 生产 xfid
     * @return
     */
    public static String produceXfid() {
        Date date = new Date();
        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss");
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            str.append(random.nextInt(10));
        }
        return simple.format(date) + str;
    }
}
