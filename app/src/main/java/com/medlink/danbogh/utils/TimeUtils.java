package com.medlink.danbogh.utils;

import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * @author vondear
 * @date 2016/1/24
 * 时间相关工具类
 */
public class TimeUtils {
    public static String milliseconds2String(long milliseconds, SimpleDateFormat format) {
        return format.format(new Date(milliseconds));
    }
}