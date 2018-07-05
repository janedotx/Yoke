package com.system2override.yoke;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;

public class TimeBank {

    private static final String TIME_SPENT_KEY = "TIME_SPENT_BANNED_APPS";
    private static final String TIME_AVAILABLE_KEY = "TIME_AVAILABLE_KEY";
    private static final String TIME_FILE = "TIME";

    public static final String RESET_ACTION = MyApplication.packageName + ".RESET";

    // intended for use when tracking amount of time spent in foreground for a bad app
    public static long addSpentTime(Context context, long time) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        SharedPreferences prefs = getSharedPreferences(context);
        Long currentTime = prefs.getLong(TIME_SPENT_KEY, 0L);
        currentTime += time;
        editor.putLong(TIME_SPENT_KEY, currentTime);
        editor.apply();
        return currentTime;
    }

    public static long getSpentTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(fullPath(), Context.MODE_PRIVATE);
        return prefs.getLong(TIME_SPENT_KEY, 0L);
    }

    public static long getAvailableTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(fullPath(), Context.MODE_PRIVATE);
        return prefs.getLong(TIME_AVAILABLE_KEY, 0L);
    }

    public static long addRefreshGrant(Context context) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        SharedPreferences prefs = getSharedPreferences(context);
        HarnessDatabase db = MyApplication.getDb(context);
        long refreshTime = db.todoRuleDao().getTodoRule().getRefreshGrantTime();
        Long currentTime = prefs.getLong(TIME_AVAILABLE_KEY, 0L);
        currentTime += refreshTime;
        editor.putLong(TIME_AVAILABLE_KEY, currentTime);
        editor.apply();
        db.close();

        return currentTime;
    }

    public static void resetTime(Context context) {
        // reset spent time
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putLong(TIME_SPENT_KEY, 0);

        // reset permitted ration of time

        HarnessDatabase db = MyApplication.getDb(context);
        long baseTimeAvailable = db.todoRuleDao().getTodoRule().getInitialTimeGrant();

        editor.putLong(TIME_AVAILABLE_KEY, baseTimeAvailable);

        db.close();
        editor.apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(fullPath(), Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
        return context.getSharedPreferences(fullPath(), Context.MODE_PRIVATE).edit();
    }


    // convenience methods for toppig up daily ration and all that
    // so we need a separate variable that account for total allowable time
    // so at the end of each day, this variable is what's cleared
    // whenever the user gets more time, this variable is increased

    private static String fullPath() {
        return MyApplication.packageName + "." + TIME_FILE;
    }
}
