package com.example.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期工具类
 *
 * @author jiangyi
 * @date 2016-6-2 下午17:26:11
 */
public class DateUtils {

    /**
     * 时间格式
     */
    public static final String DATEFORMAT1 = "yyyy-MM-dd-hh:mm:ss";
    public static final String DATEFORMAT2 = "yyyy-MM-dd";
    public static final String DATEFORMAT3 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATEFORMAT4 = "yyyyMMddHHmmss";
    public static final String DATEFORMAT5 = "yyyy年MM月dd日";
    public static final String DATEFORMAT6 = "yyyyMMddHHmmssSS";
    public static final String DATEFORMAT7 = "yyyyMMdd";

    /**
     * 格式化时间
     *
     * @param date
     * @return
     */
    public static String fromatDate(Object date) {
        SimpleDateFormat dateToStr = new SimpleDateFormat(DATEFORMAT3);
        String temp = dateToStr.format(date);
        return temp;
    }

    /**
     * DATE格式化成String
     *
     * @param date
     * @param fromat
     * @return
     */
    public static String dateToString(Date date, String fromat) {
        if (null == fromat) {
            fromat = DATEFORMAT3;
        }
        SimpleDateFormat dateToStr = new SimpleDateFormat(fromat);
        String temp = dateToStr.format(date);
        return temp;

    }

    /**
     * String格式化成DATE
     *
     * @param dateStr
     * @param fromat
     * @return
     */
    public static Date StringToDate(String dateStr, String fromat) {

        if (null == fromat) {
            fromat = DATEFORMAT3;
        }
        SimpleDateFormat strToDate = new SimpleDateFormat(fromat);
        Date date = new Date();

        try {
            date = strToDate.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获得当前系统时间（精确到秒）
     *
     * @return
     */
    public static String getCurrentTime() {
        String time = "";
        Date date = new Date();
        time = dateToString(date, DATEFORMAT3);
        return time;

    }

    /**
     * 获得当前系统时间（精确到秒）
     *
     * @return
     */
    public static String getCurrentTime(String format) {
        String time = "";
        Date date = new Date();
        time = dateToString(date, format);
        return time;

    }

    /**
     * 获得当前系统日期
     *
     * @return
     */
    public static String getCurrentDate() {
        String time = "";
        Date date = new Date();
        time = dateToString(date, DATEFORMAT2);
        return time;

    }

    /**
     * 获得当前系统日期
     *
     * @return
     */
    public static String getCurrentDate(String fromat) {
        String time = "";
        Date date = new Date();
        time = dateToString(date, fromat);
        return time;

    }

    /**
     * 获得前一天的时间
     *
     * @return String
     */
    public static String getPreviousDate(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String str = "";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        str = formatter.format(cal.getTime());
        return str;
    }

    /**
     * 获得后一天的时间
     *
     * @return String
     */
    public static Date getOntDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    /**
     * 获得按照起始和终止时间，获得所有的日期
     *
     * @param startTime String
     * @param endTime   String
     * @return List<String>
     */
    public static List<String> getMonth(String startTime, String endTime, String dateFormat) {
        List<String> vcMonth = new ArrayList<String>();
        try {
            SimpleDateFormat startDF = new SimpleDateFormat(dateFormat);
            startDF.parse(startTime);
            Calendar startCal = startDF.getCalendar();
            SimpleDateFormat endDF = new SimpleDateFormat(dateFormat);
            endDF.parse(endTime);
            Calendar endCal = endDF.getCalendar();
            vcMonth.add(startTime);
            startCal.set(Calendar.DAY_OF_MONTH, 1);
            startCal.set(Calendar.HOUR_OF_DAY, 0);
            startCal.set(Calendar.MINUTE, 0);
            startCal.set(Calendar.SECOND, 0);
            startCal.add(Calendar.MONTH, 1);
            startCal.add(Calendar.SECOND, -1);
            while (endCal.after(startCal)) {
                vcMonth.add(dateToString(startCal.getTime(), dateFormat));
                vcMonth.add(Integer.toString(startCal.get(2) + 1));
                startCal.add(Calendar.SECOND, 1);
                vcMonth.add(dateToString(startCal.getTime(), dateFormat));
                startCal.add(Calendar.MONTH, 1);
                startCal.add(Calendar.SECOND, -1);
            }
            vcMonth.add(endTime);
            vcMonth.add(Integer.toString(endCal.get(2) + 1));
        } catch (Exception ex) {
            vcMonth = new ArrayList<String>();
        }
        return vcMonth;
    }

    /**
     * 获得上月的最后一天和该月最后一天日期
     *
     * @param year  int 年份
     * @param month int 月份
     * @return String[]
     */
    public static String[] getMonth2(int year, int month) {
        SimpleDateFormat format = new SimpleDateFormat(DATEFORMAT7);
        String[] dates = new String[2];
        Calendar startCal = Calendar.getInstance();
        startCal.set(year, month - 1, 1);
        startCal.add(Calendar.DATE, -1);
        dates[0] = format.format(startCal.getTime());

        Calendar endCal = Calendar.getInstance();
        endCal.set(year, month, 1);
        endCal.add(Calendar.DATE, -1);
        dates[1] = format.format(endCal.getTime());
        return dates;
    }

    /**
     * 获得该月的第一天日期和最后一天日期
     *
     * @param year  int 年份
     * @param month int 月份
     * @return String[]
     */
    public static String[] getMonth(int year, int month) {
        SimpleDateFormat format = new SimpleDateFormat(DATEFORMAT7);
        String[] dates = new String[2];
        Calendar startCal = Calendar.getInstance();
        startCal.set(year, month - 1, 1);
        dates[0] = format.format(startCal.getTime());
        Calendar endCal = Calendar.getInstance();
        endCal.set(year, month, 1);
        endCal.add(Calendar.DATE, -1);
        dates[1] = format.format(endCal.getTime());
        return dates;
    }

    /**
     * 检测日期格式是否正确
     *
     * @param source 日期【2000－2099年】
     * @return
     */
    public static boolean checkDateFormat(String source) {
        if (source.getBytes().length != 8) {
            return false;
        }
        Pattern pattern = Pattern.compile("[2][0][0-9][0-9][0-1][0-9][0-3][0-9]");
        Matcher matcher = pattern.matcher(source);
        if (!matcher.find()) {
            return false;
        }
        int year = Integer.parseInt(source.substring(0, 4));
        int month = Integer.parseInt(source.substring(4, 6));
        int day = Integer.parseInt(source.substring(6, 8));
        if (month > 12 || month < 1) {
            return false;
        }
        if (year % 4 == 0 && year % 100 != 0) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    if (day > 31 && day < 1)
                        return false;
                    break;
                case 2:
                    if (day > 29 && day < 1)
                        return false;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    if (day > 30 && day < 1)
                        return false;
                    break;
            }
        } else {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    if (day > 31 && day < 1)
                        return false;
                    break;
                case 2:
                    if (day > 28 && day < 1)
                        return false;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    if (day > 30 && day < 1)
                        return false;
                    break;
            }
        }
        return true;

    }

    /**
     * 判断当前日期是星期几<br>
     *
     * @param pTime修要判断的时间<br>
     * @return dayForWeek 判断结果<br>
     * @Exception 发生异常<br>
     */
    public static int dayForWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 默认同步开始时间
     *
     * @return time
     */
    public static String getStartSyncDate() {
        String time = "";
        Date date = new Date();
        time = dateToString(date, DATEFORMAT2);
        time = time + " 00:00:00";
        return time;
    }

    /**
     * 默认同步结束时间
     *
     * @return time
     */
    public static String getEndSyncDate() {
        String time = "";
        Date date = new Date();
        time = dateToString(date, DATEFORMAT2);
        time = time + " 23:59:59";
        return time;

    }

    /**
     * 获取时间差
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static String getStartEndTime(String startTime, String endTime) {
        SimpleDateFormat df = new SimpleDateFormat(DATEFORMAT3);
        String str = "";
        try {
            Date startDate = df.parse(startTime);
            Date endDate = df.parse(endTime);
            long time = endDate.getTime() - startDate.getTime();
            long day = time / (24 * 60 * 60 * 1000);
            long hour = (time / (60 * 60 * 1000) - day * 24);
            long min = ((time / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (time / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            str = day + "天" + hour + "时" + min + "分" + s + "秒";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取时间差（天）
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static Integer getStartEndDay(Date startDate, Date endDate) {
        try {
            long time = endDate.getTime() - startDate.getTime();
            Long day = time / (24 * 60 * 60 * 1000);
            return day.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取时间差（秒）
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static Integer getStartEndS(Date startDate, Date endDate) {
        try {
            long time = endDate.getTime() - startDate.getTime();
            Long s = time / 1000;
            return s.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前日期+num天
     *
     * @param num
     * @return
     */
    public static String getSysDatePlusDay(int num) {
        SimpleDateFormat sf = new SimpleDateFormat(DATEFORMAT2);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, num);
        return sf.format(c.getTime());
    }

    /**
     * 获取指定日期+num天
     *
     * @param num
     * @return
     */
    public static String getSysDatePlusDay(String str, int num) {
        SimpleDateFormat sf = new SimpleDateFormat(DATEFORMAT2);
        Date date = null;
        try {
            date = sf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, num);
        return sf.format(c.getTime());
    }

    /**
     * 获取当前日期+num小时
     *
     * @param num
     * @return
     */
    public static String getSysDatePlusHour(int num) {
        SimpleDateFormat sf = new SimpleDateFormat(DATEFORMAT3);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, num);
        return sf.format(c.getTime());
    }

    /**
     * 获取指定日期+num小时
     *
     * @param num
     * @return
     */
    public static String getSysDatePlusHour(String str, int num) {
        SimpleDateFormat sf = new SimpleDateFormat(DATEFORMAT3);
        Date date = null;
        try {
            date = sf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, num);
        return sf.format(c.getTime());
    }

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return
     */
    public static String timeStamp() {
        long time = System.currentTimeMillis();
        String t = String.valueOf(time / 1000);
        return t;
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStampDate(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String dateTimeStamp(String date_str, String format) {
        try {
            if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static void main(String[] args) {
//        getOntDate(new Date());
//        long l = new Date().getTime() - StringToDate("2018-11-12 19:00:00", null).getTime();
//        System.out.println(l/1000/60);
//
//    }
}
