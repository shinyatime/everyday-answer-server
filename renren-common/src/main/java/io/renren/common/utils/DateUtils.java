/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.common.utils;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 *
 * @author Mark sunlightcs@gmail.com
 */
public class DateUtils {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date 日期
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 字符串转换成日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期的格式，如：DateUtils.DATE_TIME_PATTERN
     */
    public static Date stringToDate(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        }

        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
        return fmt.parseLocalDateTime(strDate).toDate();
    }

    /**
     * 根据周数，获取开始日期、结束日期
     *
     * @param week 周期  0本周，-1上周，-2上上周，1下周，2下下周
     * @return 返回date[0]开始日期、date[1]结束日期
     */
    public static Date[] getWeekStartAndEnd(int week) {
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusWeeks(week));

        date = date.dayOfWeek().withMinimumValue();
        Date beginDate = date.toDate();
        Date endDate = date.plusDays(6).toDate();
        return new Date[]{beginDate, endDate};
    }

    /**
     * 对日期的【秒】进行加/减
     *
     * @param date    日期
     * @param seconds 秒数，负数为减
     * @return 加/减几秒后的日期
     */
    public static Date addDateSeconds(Date date, int seconds) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(seconds).toDate();
    }

    /**
     * 对日期的【分钟】进行加/减
     *
     * @param date    日期
     * @param minutes 分钟数，负数为减
     * @return 加/减几分钟后的日期
     */
    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }

    /**
     * 对日期的【小时】进行加/减
     *
     * @param date  日期
     * @param hours 小时数，负数为减
     * @return 加/减几小时后的日期
     */
    public static Date addDateHours(Date date, int hours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(hours).toDate();
    }

    /**
     * 对日期的【天】进行加/减
     *
     * @param date 日期
     * @param days 天数，负数为减
     * @return 加/减几天后的日期
     */
    public static Date addDateDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(days).toDate();
    }

    /**
     * 对日期的【周】进行加/减
     *
     * @param date  日期
     * @param weeks 周数，负数为减
     * @return 加/减几周后的日期
     */
    public static Date addDateWeeks(Date date, int weeks) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }

    /**
     * 对日期的【月】进行加/减
     *
     * @param date   日期
     * @param months 月数，负数为减
     * @return 加/减几月后的日期
     */
    public static Date addDateMonths(Date date, int months) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }

    /**
     * 对日期的【年】进行加/减
     *
     * @param date  日期
     * @param years 年数，负数为减
     * @return 加/减几年后的日期
     */
    public static Date addDateYears(Date date, int years) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(years).toDate();
    }

    /**
     * 两个时间之差
     * @param startDate
     * @param endDate
     * @return 毫秒
     */
    public static Long getBetweenMillisecond(Date startDate, Date endDate) {
        Long ms = 0L;
        try {
            if(startDate!=null&&endDate!=null) {

                if(startDate.before(endDate)) {
                    ms = endDate.getTime() - startDate.getTime();
                }else {
                    ms = startDate.getTime() - endDate.getTime();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ms;
    }

    /**
     * 两个时间之差
     * 时间格式 yyyy-MM-dd HH:mm:ss
     * @param startDate
     * @param endDate
     * @return 毫秒
     */
    public static Long getBetweenMillisecond(String startDate, String endDate) {
        Date s = stringToDate(startDate,DATE_TIME_PATTERN);
        Date e = stringToDate(endDate,DATE_TIME_PATTERN);
        return getBetweenMillisecond(s,e);
    }

    /**
     * 两个时间之差
     * @param startDate
     * @param endDate
     * @return 分钟
     */
    public static Long getBetweenMinutes(Date startDate, Date endDate) {
        Long minutes = getBetweenMillisecond(startDate,endDate)/(60*1000) ;
        return minutes;
    }

    /**
     * 两个时间之差
     * @param startDate
     * @param endDate
     * @return 分钟
     */
    public static Long getBetweenMinutes(String startDate, String endDate) {
        Long minutes = getBetweenMillisecond(startDate,endDate)/(60*1000) ;
        return minutes;
    }

    /**
     * 两个时间只差
     * @param startDate
     * @param endDate
     * @return 秒数
     */
    public static Long getBetweenSecond(Date startDate, Date endDate) {
        Long seconds  = getBetweenMillisecond(startDate,endDate)/(1000) ;
        return seconds;
    }

    /**
     * 两个时间只差
     * @param startDate
     * @param endDate
     * @return 秒数
     */
    public static Long getBetweenSecond(String startDate, String endDate) {
        Long seconds  = getBetweenMillisecond(startDate,endDate)/(1000) ;
        return seconds;
    }

    /**
     * 得到当天开始时间
     * @return
     */
    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    /**
     * 得到当天开始时间
     * @return
     */
    public static String getStartTimeStr() {
        return format(getStartTime(),DATE_TIME_PATTERN);
    }

    /**
     * 得到当天结束时间
     * @return
     */
    public static Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    /**
     * 得到当天结束时间
     * @return
     */
    public static String getEndTimeStr() {
        return format(getEndTime(),DATE_TIME_PATTERN);
    }

    public static void main(String[] args) {
        Long datediff = DateUtils.getBetweenSecond(new Date(),DateUtils.getEndTime());
        System.out.println(datediff);
    }
}
