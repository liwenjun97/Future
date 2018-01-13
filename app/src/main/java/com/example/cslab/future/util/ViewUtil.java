package com.example.cslab.future.util;

import android.content.Context;

/**
 * Created by CSLab on 2017/7/20.
 */

public class ViewUtil {

    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}