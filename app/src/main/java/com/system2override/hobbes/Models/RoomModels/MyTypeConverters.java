package com.system2override.hobbes.Models.RoomModels;

import android.arch.persistence.room.TypeConverter;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MyTypeConverters {
    @TypeConverter
    public static boolean fromInt(int i) {
        if (i == 0) { return false; }
        return true;
    }

    @TypeConverter
    public static int fromBoolean(boolean b) {
        if (b) { return 1; }
        return 0;
    }


    @TypeConverter
    public static String fromLong(long l) {
        Calendar todayCalObj = new GregorianCalendar();
        todayCalObj.setTimeInMillis(System.currentTimeMillis());
        String year = Integer.toString(todayCalObj.get(Calendar.YEAR));
        String month = Integer.toString(todayCalObj.get(Calendar.MONTH));
        String day = Integer.toString(todayCalObj.get(Calendar.DAY_OF_MONTH));
        return year + "-" + month + "-" + day;
    }

    @TypeConverter
    public static long fromString(String date) {
        Calendar calObj = new GregorianCalendar();
        String[] s = new String[3];
        s = date.split("-");
        calObj.set(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
        return calObj.getTimeInMillis();
    }
}
