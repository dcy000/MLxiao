package com.witspring.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @date 2013-1-23
 * @author Goven
 * @email gxl3999@gmail.com
 */
@SuppressLint("SimpleDateFormat") public class DateUtil {

    //将Strin的日期转为long数据
    public static final long changeDateStringToLong(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date mdate = format.parse(date);
            return mdate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取当前的日期
    public static String getSystemCurrentDate() {
        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = new Date(time);
        String result = format.format(d1);
        return result;
    }

    //计算开始时间跟结束时间，开始时间小于结束时间返回true，大于返回false
    public static final boolean compareDateByString(String startDate, String endDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = format.parse(startDate);
            Date date2 = format.parse(endDate);
            return date1.getTime() <= date2.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
	
	/**
	 * 获得日历对象
	 * @param date 日期
	 * @return 日历
	 * @author Goven
	 * @date 2012-12-31
	 */
	public static Calendar calendar(Date date){
		if (date == null) {
			date = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
	
	/**
	 * 获取给定日期的月份和日的组合字符串，例如：12月31日
	 * @param date 日期，如果为null，则设为当前日期
	 * @return 月份和日的组合字符串
	 * @author Goven
	 * @date 2012-12-31
	 */
	public static String getMMdd(Date date) {
		Calendar calendar = calendar(date);
		
		int month = calendar.get(Calendar.MONTH) + 1;
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		
		StringBuffer sb = new StringBuffer();
		sb.append(month).append("月").append(dayOfMonth).append("日");
		return sb.toString();
	}

    /**
     * 获取给定日期的年份和月份的组合字符串，
     * @param date 日期，如果为null，则设为当前日期
     * @return 月份和日的组合字符串
     */
    public static String getYYMM(Date date) {
        Calendar calendar = calendar(date);

        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        StringBuffer sb = new StringBuffer();
        sb.append(year).append("年").append(month).append("月");
        return sb.toString();
    }


    /**
     * 获取对应时间戳几月几日
     */
    public static String getDayStr(long millius) {
        Calendar calendar = calendar(new Date(millius));
        return  calendar.get(Calendar.MONTH)+1+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日";
    }

    /**
     * 获取对应时间戳几日
     */
    public static String getDay(long millius) {
        Calendar calendar = calendar(new Date(millius));
        return  calendar.get(Calendar.DAY_OF_MONTH)+"";
    }

	/**
	 * 获取给定日期的星期，例如：星期一
	 * @param date 日期，如果为null，则设为当前日期
	 * @return 星期几
	 * @author Goven
	 * @date 2012-12-31
	 */
	public static String getDayOfWeek(Date date) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar calendar = calendar(date);
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

    public static int getDayOfWeekInNumber(Date date) {
        int[] weekDays = { 7, 1,2,3,4,5,6 };
        Calendar calendar = calendar(date);
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

	/**
	 * 格式化给定的日期，转化为字符串
	 * @return 日期字符串
	 * @author Goven
	 * @date 2012-12-31
	 */
	public static String dateMonthTime(){
		String format = new SimpleDateFormat("MM/dd HH:mm").format(new Date());
		return "["+format+"]";
	}
	
	/**
	 * 获取给定日期的开始时间，例如2012年12月31日的开始时间是：2012-12-31 00:00:00
	 * @param date 日期，如果为null，则设为当前日期
	 * @return 日期的开始时间字符串
	 * @author Goven
	 * @date 2012-12-31
	 */
	public static String dayBegin(Date date){
		Calendar calendar = calendar(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return date2Str(calendar.getTime());
	}

	public static Date beginOnlyDay(Date date){
		Calendar calendar = calendar(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
	}

	/**
	 * 获取给定日期的结束时间，即第二天的开始时间，例如2012年12月31日的结束时间是：2012-12-31 23:59:59
	 * 当然数据库查询记录时要小于这个时间，不然可能会有重复的记录
	 * @param date 日期，如果为null，则设为当前日期
	 * @return 日期的结束时间字符串
	 * @author Goven
	 * @date 2012-12-31
	 */
	public static String dayEnd(Date date){
		Calendar calendar = calendar(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.add(Calendar.MILLISECOND, 59);
		return date2Str(calendar.getTime());
	}

    public static Date endDay(Date date){
        Calendar calendar = calendar(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.add(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static int getWeeksOfMonth(int dayOfWeek ,int daysOfMonth) {
        // getCalendar(year, month);
        int preMonthRelax = 0 ,weeksOfMonth;
        if (dayOfWeek != 7) {
            preMonthRelax = dayOfWeek;
        }
        if ((daysOfMonth + preMonthRelax) % 7 == 0) {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7;
        } else {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7 + 1;
        }
        return weeksOfMonth;
    }

    /**
     * 计算一月几周
     * @return
     */
    public static int getWeeksOfGivenMonth(int year, int month) {
        int weeksOfMonth;
        // 先判断某月的第一天为星期几
        int preMonthRelax = 0;
        int dayFirst = getWeekdayOfMonth(year, month);
        int days = getDaysOfMonth(isLeapYear(year), month);
        if (dayFirst != 7) {
            preMonthRelax = dayFirst;
        }
        if ((days + preMonthRelax) % 7 == 0) {
            weeksOfMonth = (days + preMonthRelax) / 7;
        } else {
            weeksOfMonth = (days + preMonthRelax) / 7 + 1;
        }
        return weeksOfMonth;
    }

    public static int getWeekDayOfLastMonth(int year,int month,int day){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day);
        return cal.get(Calendar.DAY_OF_WEEK)-1;
    }

    /**
     * 指定某年中的某月的第一天是星期几
     */
    public static int getWeekdayOfMonth(int year, int month){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, 1);
        return cal.get(Calendar.DAY_OF_WEEK)-1;
    }

	/**
	 * 格式化给定的日期，转化为字符串
	 * @param date 日期
	 * @return 日期字符串，格式：yyyy-MM-dd HH:mm:ss
	 * @author Goven
	 * @date 2012-12-31
	 */
	public static String date2Str(Date date){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
	
	/**
	 * 格式化指定格式的日期
	 * @param date 日期
	 * @param format 格式
	 * @author Goven 2014年10月9日 下午4:40:49
	 */
	public static String date2Str(Date date, String format) {
	    if (date == null) {
	        date = new Date();
        }
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 * 解析给定的字符串，转换为日期对象
	 * @param dateStr 日期字符串，格式：yyyy-MM-dd HH:mm:ss
	 * @return 日期
	 * @author Goven
	 * @date 2013-1-6
	 */
	public static Date str2Date(String dateStr){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 解析给定的字符串，指定格式
	 * @param dateStr 日期字符串
	 * @param format 格式
	 * @author Goven 2014年10月9日 下午7:59:42
	 */
	public static Date str2Date(String dateStr, String format){
		DateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 解析给定的字符串，转换为毫秒数
	 * @param dateStr 日期字符串，格式：yyyy-MM-dd HH:mm:ss
	 * @return 毫秒数
	 * @author yubin
	 * @date 2013-6-18
	 */
	public static long str2Long(String dateStr) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(dateStr).getTime();
		} catch (Exception e) {
			return -1;
		}
	}

    /**
     * 时间轴处理
     * @param startTime
     * @return
     */
    public static String getDistanceTime(String startTime, String formatStr) {
        String time = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            Date startDate = format.parse(startTime);
            Date endData = new Date();
            if ((endData.getTime() - startDate.getTime()) < 0) {
                time = "";
            } else {
                if ((endData.getTime() - startDate.getTime()) >= 0 && (endData.getTime() - startDate.getTime()) / 60000 < 1) {
                    time = "刚刚";
                } else if ((endData.getTime() - startDate.getTime()) / 60000 >= 1 && (endData.getTime() - startDate.getTime()) / 60000 < 60) {
                    time = (endData.getTime() - startDate.getTime()) / 60000 + "分钟前";
                } else if ((endData.getTime() - startDate.getTime()) / 3600000 >= 1 && (endData.getTime() - startDate.getTime()) / 3600000 < 24) {
                    time = (endData.getTime() - startDate.getTime()) / 3600000 + "小时前";
                } else {
                    long day = (endData.getTime() - startDate.getTime()) / (3600000 * 24);
                    if (day > 0 && day <= 7) {
                        time = day + "天前";
                    } else if (day > 7 && day <= 30) {
                        time = day / 7 + "周前";
                    } else {
                        time = day / 30 + "月前";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String dayName(Date date) {
        try {
            Calendar currCalendar = Calendar.getInstance();
            Calendar newCalendar = Calendar.getInstance();
            newCalendar.setTime(date);
            currCalendar.set(Calendar.HOUR, 0);
            currCalendar.set(Calendar.MINUTE, 0);
            currCalendar.set(Calendar.SECOND, 0);
            currCalendar.set(Calendar.MILLISECOND, 0);
            currCalendar.set(Calendar.AM_PM, 0);
            newCalendar.set(Calendar.HOUR, 0);
            newCalendar.set(Calendar.MINUTE, 0);
            newCalendar.set(Calendar.SECOND, 0);
            newCalendar.set(Calendar.MILLISECOND, 0);
            newCalendar.set(Calendar.AM_PM, 0);
            long interval = newCalendar.getTimeInMillis() - currCalendar.getTimeInMillis();
            int dayInterval = (int)(interval / (24 * 60 * 60 *1000));
            if (dayInterval == -2) {
                return "前天";
            } else if (dayInterval == -1) {
                return "昨天";
            } else if (dayInterval == 0) {
                return "今天";
            } else if (dayInterval == 1) {
                return "明天";
            } else if (dayInterval == 2) {
                return "后天";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(newCalendar.get(Calendar.MONTH) + 1).append("月");
                sb.append(newCalendar.get(Calendar.DAY_OF_MONTH)).append("日");
//                sb.append("\n");
//                int newDayOfWeek = newCalendar.get(Calendar.DAY_OF_WEEK) - 1;
//                String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
//                sb.append(weekDays[newDayOfWeek]);
                return sb.toString();
            }
        } catch (Exception e) {
            return null;
        }
    }

    // 判断是否为闰年
    public static boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else if (year % 100 != 0 && year % 4 == 0) {
            return true;
        }
        return false;
    }

    //得到某月有多少天数
    public static int getDaysOfMonth(boolean isLeapyear, int month) {
        int daysOfMonth = 30;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysOfMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysOfMonth = 30;
                break;
            case 2:
                if (isLeapyear) {
                    daysOfMonth = 29;
                } else {
                    daysOfMonth = 28;
                }

        }
        return daysOfMonth;
    }

    public static String dayName(String queryDate , String currentDate , String currentMonth) {
        String dayName = null;
        String queryMonth = queryDate.split("-")[1];
        boolean isToday = (currentDate != null && currentDate.equals(queryDate)) ;
        boolean isSameMonth = (currentMonth != null && currentMonth.equals(queryMonth)) ;

        String today;
        if(isToday){
            today = "今天";
        }else if(!isSameMonth){
            today = Integer.parseInt(queryMonth)+"月";
        }else{
            today = DateUtil.getDayOfDate(queryDate, "yyyy-MM-dd");
        }

        String dayOfMonth = DateUtil.getWeekOfDate(queryDate, "yyyy-MM-dd");
        dayName = today+"\n"+dayOfMonth;
        return dayName;
    }

    /**
     * 获取当前日期是星期几
     */
    public static String getDayOfDate(String date , String format) {
        Date dt = null;
        try {
           dt = new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获取当前日期几号
     */
    public static String getWeekOfDate(String date , String format) {
        Date dt = null;
        try {
            dt = new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return  cal.get(Calendar.DAY_OF_MONTH)+"";
    }

    /**
     * 获取过去几天的日期
     * @param num 过去几天
     */
    public static Calendar getLastDays(int num) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -num);
        return cal;
    }

    public static Calendar getLastWeeks(int num) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, -num);
        return cal;
    }

    public static Calendar getLastMonths(int num) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -num);
        return cal;
    }

}
