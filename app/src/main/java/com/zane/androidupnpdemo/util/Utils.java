package com.zane.androidupnpdemo.util;

import java.util.Formatter;
import java.util.Locale;

/**
 * 说明：
 * 作者：zhouzhan
 * 日期：17/6/29 10:47
 */

public class Utils {

    public static boolean isNull(Object obj){
        return obj == null;
    }

    public static boolean isNotNull(Object obj){
        return !isNull(obj);
    }

    /**
     * 把时间戳转换成 00:00:00 格式
     * @param timeMs    时间戳
     * @return  00:00:00 时间格式
     */
    public static String stringForTime(int timeMs){
        StringBuilder formatBuilder = new StringBuilder();
        Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());

        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds  / 60) % 60;
        int hours = totalSeconds / 3600;

        formatBuilder.setLength(0);
        return formatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }
}
