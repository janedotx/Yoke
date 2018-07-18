package com.system2override.yoke;

import android.content.Context;
import android.content.SharedPreferences;

public class TimeSettingsManager {

    // intended for use when tracking amount of time spent in foreground for a bad app
    public static long addSpentTime(Context context, long time) {
        /*
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        SharedPreferences prefs = getSharedPreferences(context);
        Long currentTime = prefs.getLong(TIME_SPENT_KEY, 0L);
        currentTime += time;
        editor.putLong(TIME_SPENT_KEY, currentTime);
        editor.apply();
        return currentTime;
        */
        return 0L;
    }
}
