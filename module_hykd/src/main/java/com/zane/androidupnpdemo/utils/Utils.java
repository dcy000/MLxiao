package com.zane.androidupnpdemo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gzq on 2018/4/12.
 */

public class Utils {
    public static String parseCurrentTime2String(){
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
}
