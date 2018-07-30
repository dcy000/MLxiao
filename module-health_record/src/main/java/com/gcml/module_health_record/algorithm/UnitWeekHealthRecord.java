package com.gcml.module_health_record.algorithm;

import com.gcml.lib_utils.data.TimeUtils;
import com.gcml.module_health_record.bean.BaseBean;
import com.gcml.module_health_record.bean.BloodOxygenHistory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/29 14:45
 * created by:gzq
 * description:按周计算健康数据
 */
public class UnitWeekHealthRecord {
    /**
     * 获取指定时间的周一
     *
     * @param time
     * @return
     */
    public Date getTheDayMonday(long time) {
        Calendar cal = Calendar.getInstance();
        Date date = TimeUtils.milliseconds2Date(time);
        cal.setTime(date);
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }

        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一

        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        return cal.getTime();
    }

    /**
     * 获取指定时间的周日
     *
     * @param time
     * @return
     */
    public Date getTheDaySunday(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTheDayMonday(time));
        cal.add(Calendar.DATE, 6);
        return cal.getTime();
    }

    public int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    public int getTheDayOfWeekInMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_MONTH);
    }

    public ArrayList<BaseBean> regroupDatas(ArrayList<BaseBean> datas) {
        if (datas == null || datas.size() == 0) {
            return null;
        }
        HashMap<Long, ArrayList<BaseBean>> hashMap = new HashMap<>();
        ArrayList<Long> timeNode = new ArrayList<>();
        long startTime = datas.get(0).time;

        Date theDayMonday = getTheDayMonday(startTime);
        long mondayMillisecond = TimeUtils.date2Milliseconds(theDayMonday);
        Date theDaySunday = getTheDaySunday(startTime);
        long sundayMillisecond = TimeUtils.date2Milliseconds(theDaySunday);
        ArrayList<BaseBean> beans = new ArrayList<>();
        for (BaseBean bean : datas) {
            if (bean.time < sundayMillisecond) {
                beans.add(bean);

            } else {
                timeNode.add(mondayMillisecond);
                hashMap.put(mondayMillisecond, beans);
                beans.clear();
                beans.add(bean);
                theDayMonday = getTheDayMonday(bean.time);
                mondayMillisecond = TimeUtils.date2Milliseconds(theDayMonday);
                theDaySunday = getTheDaySunday(bean.time);
                sundayMillisecond = TimeUtils.date2Milliseconds(theDaySunday);
            }
        }
        ArrayList<BaseBean> results = new ArrayList<>();
        for (Long l : timeNode) {
            ArrayList<BaseBean> beans1 = hashMap.get(l);
            float sum1 = 0;
            float sum2 = 0;
            Class clazz = null;
            for (BaseBean bean : beans1) {
                if (bean instanceof BloodOxygenHistory) {
                    BloodOxygenHistory bloodOxygenHistory = (BloodOxygenHistory) bean;
                    clazz = bloodOxygenHistory.getClass();
                    sum1 += bloodOxygenHistory.blood_oxygen;
                }
            }
            BaseBean bean = null;
            if (clazz == BloodOxygenHistory.class) {
                bean = new BloodOxygenHistory(sum1 / beans1.size(), l);
            }
            results.add(bean);
        }
        return results;
    }

}
