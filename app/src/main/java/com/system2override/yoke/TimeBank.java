package com.system2override.yoke;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;

public class TimeBank {
    private static final String INITIAL_TIME_GRANT_KEY = "INITIAL_TIME_GRANT_KEY";
    private static final String REWARD_GRANT_TIME_KEY = "REWARD_GRANT_TIME_KEY";
    private static final String TIME_SPENT_KEY = "TIME_SPENT_BANNED_APPS_KEY";
    private static final String TIME_AVAILABLE_KEY = "TIME_AVAILABLE_KEY";
    private static final String TIME_FILE = "TIME";

    public static final String RESET_ACTION = MyApplication.packageName + ".RESET";
    private static SharedPreferencesHelper helper;

    private static SharedPreferencesHelper getSharedPreferencesHelper(){
        if (helper == null) {
            helper = new SharedPreferencesHelper(TIME_FILE);
        }
        return helper;
    }

    // convenience methods for toppig up daily ration and all that
    // so we need a separate variable that account for total allowable time
    // so at the end of each day, this variable is what's cleared
    // whenever the user gets more time, this variable is increased

    // intended for use when tracking amount of time spent in foreground for a bad app
    public static long addSpentTime(Context context, long time) {
        SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
        SharedPreferences prefs = getSharedPreferencesHelper().getSharedPreferences(context);
        long currentTime = prefs.getLong(TIME_SPENT_KEY, 0L);
        currentTime += time;
        editor.putLong(TIME_SPENT_KEY, currentTime);
        editor.apply();
        return currentTime;
    }

    public static long getSpentTime(Context context) {
        SharedPreferences prefs = getSharedPreferencesHelper().getSharedPreferences(context);
        return prefs.getLong(TIME_SPENT_KEY, 0L);
    }

    public static long getAvailableTime(Context context) {
        SharedPreferences prefs = getSharedPreferencesHelper().getSharedPreferences(context);
        return prefs.getLong(TIME_AVAILABLE_KEY, 0L);
    }

    public static long earnTime(Context context) {
        SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
        SharedPreferences prefs = getSharedPreferencesHelper().getSharedPreferences(context);

        long refreshTime = prefs.getLong(REWARD_GRANT_TIME_KEY, 0l);
        long currentTime = prefs.getLong(TIME_AVAILABLE_KEY, 0L);
        currentTime += refreshTime;
        editor.putLong(TIME_AVAILABLE_KEY, currentTime);
        editor.apply();

        return currentTime;
    }

    public static void resetTime(Context context) {
        // reset spent time
        SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
        editor.putLong(TIME_SPENT_KEY, 0);
        editor.apply();

        // reset permitted ration of time

        SharedPreferences sharedPreferences = getSharedPreferencesHelper().getSharedPreferences(context);
        long initialGrantTime = sharedPreferences.getLong(INITIAL_TIME_GRANT_KEY, 0L);

        editor.putLong(TIME_AVAILABLE_KEY, initialGrantTime);
        editor.apply();

    }

    public static void setInitialTime(Context context, long initialTime) {
        SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
        editor.putLong(INITIAL_TIME_GRANT_KEY, initialTime);
        editor.apply();
    }

    public static long getInitialTime(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferencesHelper().getSharedPreferences(context);
        return sharedPreferences.getLong(INITIAL_TIME_GRANT_KEY, 0L);
    }

    public static void setRewardTimeGrant(Context context, long time) {
        SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
        editor.putLong(REWARD_GRANT_TIME_KEY, time);
        editor.apply();
    }

    public static long getRewardTimeGrant(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferencesHelper().getSharedPreferences(context);
        return sharedPreferences.getLong(REWARD_GRANT_TIME_KEY, 0L);
    }

    public static long getTotalEarnedTimeToday(Context context) {
        return getAvailableTime(context) - getInitialTime(context);
    }

}
