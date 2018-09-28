package com.system2override.yoke.Utilities;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.util.Calendar;
import java.util.List;

public class RandomUtilities {

    public static int getTotalMinutes(long millis) {
        return (int) (millis / (1000 * 60));
    }

    public static int getMinuteField(long millis) {
        return (int) (millis / (1000 * 60)) % 60;

    }

    public static int getSecondsField(long millis) {
        return (int) ((millis / 1000) % 60);
    }

    public static int getHourField(long millis) {
        return (int) ((millis / (1000 * 60 * 60)) % 24);
    }

    public static String formatMillisecondsToMMSS(long millis) {
        return String.format("%dm %ds",
                getTotalMinutes(millis), getSecondsField(millis));
    }

    public static String formatMillisecondsToMinutes(long millis) {
        if ((int) millis == 0) {
            return "0 m";
        }
        return String.format("%d m",
                getTotalMinutes(millis));
    }

    public static String formatMillisecondsToHHMM(long millis) {
        return String.format("%dh %dm",
                getHourField(millis), getMinuteField(millis));
    }

    public static String formatMSToHHMMSS(long millis) {
        return String.format("%dh %dm %ds",
                getHourField(millis), getMinuteField(millis), getSecondsField(millis));
    }

    public static long getNextMidnight() {
        Calendar calendar = Calendar.getInstance();
        long time = System.currentTimeMillis();
        calendar.setTimeInMillis(time);
        // ensure this fires for the next upcoming midnight
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        return calendar.getTimeInMillis();
    }

    public static long getNext15Seconds() {
        Calendar calendar = Calendar.getInstance();
        long time = System.currentTimeMillis();
        calendar.setTimeInMillis(time);
        // ensure this fires for the next upcoming midnight
        calendar.add(Calendar.SECOND, 15);
        return calendar.getTimeInMillis();
    }
}
