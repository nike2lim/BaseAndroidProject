package com.example.shlim.baseandroidproject.utils;

import android.content.Context;

/**
 * Created by innochal on 2017-10-11.
 */

public class ViewUtil {
    private static final String TAG = ViewUtil.class.getSimpleName();	// 디버그 태그

    public static int convertDpToPixels(float dp, Context context) {
        int px = 0;
        px = (int) (dp * context.getResources().getDisplayMetrics().density);
        return px;
    }

}
