
package com.gcml.mall.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BillUtils {
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA);

    public static String genBillNum() {
        return simpleDateFormat.format(new Date());
    }
}
