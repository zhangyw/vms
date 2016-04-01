package com.nanjing.vms.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final SimpleDateFormat SDF_TIME_DEFAULT = new SimpleDateFormat(
            "yyyyMMddHHmmss", Locale.CHINA);

    public static final SimpleDateFormat SDF_YYYYMMDD = new SimpleDateFormat(
            "yyyyMMdd", Locale.CHINA);

    public static final SimpleDateFormat SDF_TIME_DISPLAY = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm", Locale.CHINA);

    public static String formatTime(String timestamp) {
        if (!TextUtils.isEmpty(timestamp) && TextUtils.isDigitsOnly(timestamp)) {
            long temp = Long.parseLong(timestamp) * 1000;
            return SDF_TIME_DISPLAY.format(new Date(temp));
        } else {
            return "";
        }

    }

    public static long formatTimeToLong(String time) {
        try {
            return SDF_TIME_DEFAULT.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }


}
