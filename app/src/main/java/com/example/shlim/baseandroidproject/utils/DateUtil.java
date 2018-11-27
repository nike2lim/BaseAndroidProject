package com.example.shlim.baseandroidproject.utils;

import android.content.Context;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dongnam on 2017. 5. 23..
 */

public class DateUtil {
    private static final String TAG = DateUtil.class.getSimpleName();   // 디버그 태그

    /**
     * 현재 날짜
     *
     * @return
     */
    public static int[] getDate() {
        LogUtil.i(TAG, "getDate() -> Start !!!");
        Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH) + 1;
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int w = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        int[] result = {yy, mm, dd, w, hour, min};
        return result;
    }

    /**
     * String -> Timestamp 변환
     *
     * @param string
     * @return
     */
    public static long stringToTimestamp(String string) {
        LogUtil.i(TAG, "stringToTimestamp() -> Start !!!");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("a hh:mm");
        long timestamp = 0;
        try {
            Date date = simpleDateFormat.parse(string);
            timestamp = date.getTime();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return timestamp;
    }

    /**
     * String -> Date 변환
     * @param string
     * @return
     */
    public static int[] stringToDate(String string) {
        LogUtil.i(TAG, "stringToDate() -> Start !!!");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("a hh:mm", Locale.KOREA);
        try {
            calendar.setTime(simpleDateFormat.parse(string));
        } catch (ParseException e) {
            LogUtil.e(TAG, e.getMessage());
        }
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH) + 1;
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int w = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int[] result = {yy, mm, dd, w, hour, min};
        return result;
    }

    /**
     * 안심귀가 시간
     * @param hourOfDay
     * @param minute
     * @return
     */
    @SuppressWarnings("WrongConstant")
    public static String getSetTime(Context cxt, int hourOfDay, int minute) {
        LogUtil.i(TAG, "getSetTime() -> Start !!!");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        String ampm = "";

        Locale systemLocale = cxt.getResources().getConfiguration().locale;
        String strLanguage = systemLocale.getLanguage();

        if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
            if(strLanguage.equals("ja")) {
                ampm = "午前";
            }else if(strLanguage.equalsIgnoreCase("en")) {
                ampm = "AM";
            }else if(strLanguage.equals("zh_CN") || strLanguage.equals("zh_TW") || strLanguage.equals("zh_HK")) {
                ampm = "上午";
            }else {
                ampm = "오전";
            }
        } else if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
            if(strLanguage.equals("ja")) {
                ampm = "午後";
            }else if(strLanguage.equals("en")) {
                ampm = "PM";
            }else if(strLanguage.equals("zh_CN") || strLanguage.equals("zh_TW") || strLanguage.equals("zh_HK")) {
                ampm = "下午";
            }else {
                ampm = "오후";
            }
            if (hourOfDay > 12)
                hourOfDay = hourOfDay - 12;
        }
        String stringMinute;
        if (minute < 10) {
            stringMinute = String.valueOf("0" + minute);
        } else {
            stringMinute = String.valueOf(minute);
        }
        return ampm + " " + hourOfDay + ":" + stringMinute;
    }

    /**
     * 현재 시간 (안심귀가 시간 비교용)
     * @return
     */
    @SuppressWarnings("WrongConstant")
    public static int[] getCurrentTime() {
        LogUtil.i(TAG, "getCurrentTime() -> Start !!!");
        int[] date = new int[3];
        Calendar calendar = Calendar.getInstance();
        int ampm = 0;
        if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
            ampm = 0;
        } else if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
            ampm = 1;
        }
        date[0] = ampm;
        date[1] = calendar.get(Calendar.HOUR_OF_DAY);
        date[2] = calendar.get(Calendar.MINUTE);
        return date;
    }

    /**
     * 로그 시간
     * @return
     */
    public static String getLogTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 서버 날짜 변환
     * @param input
     * @return
     */
    public static long getServerTimestamp(String input) {
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
            Date date = simpleDateFormat.parse(input);
            Timestamp timestamp = new java.sql.Timestamp(date.getTime());
            return timestamp.getTime();
        } catch(Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return 0;
    }

    /**
     * 시간 변환
     * @param input
     * @return
     */
    public static long converterTime(String input) {
        LogUtil.i(TAG, "stringToTimestamp() -> Start !!!");
        LogUtil.d(TAG, "stringToTimestamp() -> input : " + input);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long timestamp = 0;
        try {
            Date date = simpleDateFormat.parse(input);
            timestamp = date.getTime();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return timestamp;
    }

    /**
     * 언어설정에 따라 저장된 오전/오후를 한글로 변경한다.
     * @param convert
     * @return
     */
    public static String amPmConvertKorean(String convert) {
        String converStr = convert;

        if(convert.contains("午前")) {
            converStr = convert.replace("午前", "오전");
        }else  if(convert.contains("午後")) {
            converStr = convert.replace("午後", "오후");
        }else if(convert.contains("上午")) {
            converStr = convert.replace("上午", "오전");
        }else if(convert.contains("下午")) {
            converStr = convert.replace("下午", "오후");
        }else if(convert.contains("AM")) {
            converStr = convert.replace("AM", "오전");
        }else if(convert.contains("PM")) {
            converStr = convert.replace("PM", "오후");
        }else {

        }
        return converStr;
    }

    /**
     * 각 언어 설정에 맞는 오전/오후에 대한 String 을 리턴한다.
     * @param convert
     * @return
     */
    public static String amPmConvertCountryLan(Context cxt, String convert) {
        String converStr = convert;

        Locale systemLocale = cxt.getResources().getConfiguration().locale;
        String strLanguage = systemLocale.getLanguage();

        if(convert.contains("午前")) {
            if(strLanguage.equals("en")) {
                converStr = convert.replace("午前", "AM");
            }else if(strLanguage.equalsIgnoreCase("CN") || strLanguage.equals("zh") || strLanguage.equals("zh_CN")) {
                converStr = convert.replace("午前", "上午");
            }else if(strLanguage.equals("ko") ) {
                converStr = convert.replace("午前", "오전");
            }else {
                //nothing
            }
        }else  if(convert.contains("午後")) {
            if(strLanguage.equals("en")) {
                converStr = convert.replace("午前", "PM");
            }else if(strLanguage.equalsIgnoreCase("CN") || strLanguage.equals("zh") || strLanguage.equals("zh_CN")) {
                converStr = convert.replace("午前", "下午");
            }else if(strLanguage.equals("ko") ) {
                converStr = convert.replace("午後", "오후");
            }else {
                //nothing
            }
        }else if(convert.contains("上午")) {
            if(strLanguage.equals("en")) {
                converStr = convert.replace("上午", "AM");
            }else if(strLanguage.equals("ja")) {
                converStr = convert.replace("上午", "午前");
            }else if(strLanguage.equals("ko") ) {
                converStr = convert.replace("上午", "오전");
            }else {
                //nothing
            }
        }else if(convert.contains("下午")) {
            if(strLanguage.equals("en")) {
                converStr = convert.replace("下午", "PM");
            }else if(strLanguage.equals("ja")) {
                converStr = convert.replace("下午", "午後");
            }else if(strLanguage.equals("ko") ) {
                converStr = convert.replace("下午", "오후");
            }else {
                //nothing
            }
        }else if(convert.contains("AM")) {
            if(strLanguage.equals("ja")) {
                converStr = convert.replace("AM", "午前");
            }else if(strLanguage.equalsIgnoreCase("CN") || strLanguage.equals("zh") || strLanguage.equals("zh_CN")) {
                converStr = convert.replace("AM", "上午");
            }else if(strLanguage.equals("ko") ) {
                converStr = convert.replace("AM", "오전");
            }else {
                //nothing
            }
        }else if(convert.contains("PM")) {
            if(strLanguage.equals("ja")) {
                converStr = convert.replace("PM", "午後");
            }else if(strLanguage.equalsIgnoreCase("CN") || strLanguage.equals("zh") || strLanguage.equals("zh_CN")) {
                converStr = convert.replace("PM", "下午");
            }else if(strLanguage.equals("ko") ) {
                converStr = convert.replace("PM", "오후");
            }else {
                //nothing
            }
        }else if(convert.contains("오전")) {
            if(strLanguage.equals("ja")) {
                converStr = convert.replace("오전", "午前");
            }else if(strLanguage.equalsIgnoreCase("CN") || strLanguage.equals("zh") || strLanguage.equals("zh_CN")) {
                converStr = convert.replace("오전", "上午");
            }else if(strLanguage.equals("en") ) {
                converStr = convert.replace("오전", "AM");
            }else {
                //nothing
            }
        }else if(convert.contains("오후")) {
            if(strLanguage.equals("ja")) {
                converStr = convert.replace("오후", "午後");
            }else if(strLanguage.equalsIgnoreCase("CN") || strLanguage.equals("zh") || strLanguage.equals("zh_CN")) {
                converStr = convert.replace("오후", "下午");
            }else if(strLanguage.equals("en") ) {
                converStr = convert.replace("오후", "PM");
            }else {
                //nothing
            }
        }else {

        }
        return converStr;
    }
}
