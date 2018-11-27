package com.example.shlim.baseandroidproject.utils;

import android.util.Log;

public class LogUtil {
    // TODO 상용 (로그 사용 유무)
    private static boolean isEnable;    // 로그 사용 플래그

    /**
     * 로그파일 사용 여부 설정
     * @param enable
     * @return
     */
    public static void setLogEnable(boolean enable) {
        isEnable = enable;
    }

    /**
     * 로그파일 사용 여부 리턴
     * @return
     */
    public static boolean getLogEnable() {
        return isEnable;
    }

    // Information
    public static void i(String tag, String msg) { if (isEnable && null != msg) Log.i(tag, msg); }

    // Debug
    public static void d(String tag, String msg) { if (isEnable && null != msg) Log.d(tag, msg); }

    // Warning
    public static void w(String tag, String msg) {
        if (isEnable && null != msg) Log.w(tag, msg);
    }

    // Error
    public static void e(String tag, String msg) {
        if (isEnable && null != msg) Log.e(tag, msg);
    }

    // Verbose
    public static void v(String tag, String msg) {
        if (isEnable && null != msg) Log.v(tag, msg);
    }
}