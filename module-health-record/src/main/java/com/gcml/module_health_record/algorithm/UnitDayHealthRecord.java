package com.gcml.module_health_record.algorithm;

import com.gcml.module_health_record.bean.BaseBean;
import com.gzq.lib_core.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/28 14:50
 * created by:gzq
 * description:按天计算健康数据
 */
public class UnitDayHealthRecord {
    private HashMap<String, ArrayList<BaseBean>> hashMap;

    private static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取一天0点的时间戳
     * 传入的格式：yyyy-MM-dd
     *
     * @param date
     * @return
     */
    private long getOneDayStart(String date) {
        String st = date + " 00:00:00";
        return TimeUtils.string2Milliseconds(st, format1);
    }

    /**
     * 获取一天
     *
     * @param date
     * @return
     */
    private long getOneDayEnd(String date) {
        String st = date + " 23:59:59";
        return TimeUtils.string2Milliseconds(st, format1);
    }

    public HashMap<String, ArrayList<BaseBean>> getDataGroup(ArrayList<BaseBean> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }
        hashMap = new HashMap<>();
        long time = arrayList.get(0).time;
        String firstDay = TimeUtils.milliseconds2String(time, format2);
        long oneDayStart = getOneDayStart(firstDay);
        long oneDayEnd = getOneDayEnd(firstDay);
        ArrayList<BaseBean> beans=new ArrayList<>();
        hashMap.put(firstDay,beans);

        return null;
    }
}
