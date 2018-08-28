package com.gcml.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by wecent .
 * on 2017-08-10
 */

public class TimeHelper {

    private static final String DATE_TIME = "yyyy-MM-dd";
    private static final String DATE_TIME_HOUR = "HH:mm";
    private static final String DATE_TIME0 = "yyyy-MM-dd HH:mm";
    private static final String DATE_TIME1 = "MM-dd";
    private static final String DATE_TIME_MONTH = "MM-dd HH:mm:ss";
    private static final String DATE_Calendar = "yyyyMMdd";
    private static final String DATE_TIME_MILL = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_TIME_MILL_HOUR_MIN_MILL = "HH:mm:ss";

    /**
     * 格式化时间（yyyy-MM-dd）
     *
     * @param dateTime
     * @return
     */
    public static String formatDateTime(long dateTime) {
        return formatDateTime(dateTime, DATE_TIME);
    }

    /**
     * 格式化时间（yyyy-MM-dd）
     *
     * @param dateTime
     * @return
     */
    public static String formatDateTime0(long dateTime) {
        return formatDateTime(dateTime, DATE_TIME0);
    }

    /**
     * 格式化时间（MM-dd）
     *
     * @param dateTime
     * @return
     */
    public static String formatDateTimeMonth(long dateTime) {
        return formatDateTime(dateTime, DATE_TIME1);
    }

    /**
     * 格式化时间（HH-mm）
     *
     * @param dateTime
     * @return
     */
    public static String formatDateTimeHour(long dateTime) {
        return formatDateTime(dateTime, DATE_TIME_HOUR);
    }


    /**
     * 格式化时间（HH:mm:mm）
     *
     * @param mill 参数秒
     * @return
     */
    public static String formatDateTimeHMM(long mill) {
        return formatDateTime(mill, DATE_TIME_MILL_HOUR_MIN_MILL);
    }

    /**
     * 格式化时间
     *
     * @param dateTime
     * @param format
     * @return
     */
    public static String formatDateTime(long dateTime, String format) {
        Date date = new Date(dateTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 格式化时间
     *
     * @param dateTime
     * @return
     */

    public static String formatDateTimeMill(long dateTime) {
        Date date = new Date(dateTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_MILL);
        return simpleDateFormat.format(date);
    }

    /**
     * 格式化时间
     *
     * @param dateTime
     * @return
     */
    public static String formatDateTimeMMSS(long dateTime) {
        Date date = new Date(dateTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_MONTH);
        return simpleDateFormat.format(date);
    }

    public static String getCurrentDateTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_MILL);
        return simpleDateFormat.format(date);
    }

    public static String getCalendarTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_Calendar);
        return simpleDateFormat.format(date);
    }

    /**
     * 获取当前时间 格式 21:22:40
     *
     * @return
     */
    public static String getCurrentTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);//01:00:00~10:00:00
        return format.format(date);
    }

    /**
     * 2017-04-25 15:27
     *
     * @return
     */
    public static String getFullCurrentTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);//01:00:00~10:00:00
        return format.format(date);
    }

    /**
     * 判断当天是否是双休日
     *
     * @return
     */
    public static boolean isWeekend() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int i = cal.get(Calendar.DAY_OF_WEEK);//1 ,0
        return i == 0 || i == 1;
    }

    /**
     * 当前时间是否是上班时间段内：09~16
     *
     * @return
     */
    public static boolean isWorkingTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);//01:00:00~10:00:00
        String time = format.format(date);
        String substring = time.substring(0, 2);
        if (substring.substring(0, 1).equals("0")) {
            String substring1 = substring.substring(1, 2);
            return substring1.equals("9");
        } else if (substring.substring(0, 1).equals("1")) {
            String substring2 = substring.substring(1, 2);
            return !(substring2.equals("9") || substring2.equals("8"));
        }
        return false;
    }

    /**
     * 时间戳
     *
     * @param expireDate
     * @return
     */
    public static long getSecondsFromDate(String expireDate) {
        if (expireDate == null || expireDate.trim().equals(""))
            return 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(expireDate);
            return (long) (date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * GMT时区时间戳
     *
     * @return
     */
    public static String getCUSeconds() {

        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String cuTime = dff.format(new Date());

        return cuTime;
    }

}
