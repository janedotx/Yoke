package com.system2override.yoke.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.MyApplication;

import java.util.List;

public class Streaks {
    private static final String STREAKS_FILE = "STREAKS_FILE";
    private static final String CURRENT_STREAKS_KEY = "CURRENT_STREAKS_KEY";
    private static final String LONGEST_STREAKS_KEY = "LONGEST_STREAKS_KEY";
    private static final String STREAK_COMPLETED_TODAY = "STREAK_COMPLETED_TODAY";

    private static SharedPreferencesHelper helper;

    private static SharedPreferencesHelper getSharedPreferencesHelper() {
        if (helper == null) {
            helper = new SharedPreferencesHelper(STREAKS_FILE);
        }
        return helper;
    }

    public static boolean canAddStreak(HarnessDatabase db) {
        List<Habit> habits = db.habitDao().loadAllHabits();

        boolean allDone = true;
        for (Habit h: habits) {
            if (!h.completedOn(MyApplication.getTodayCalObj())) {
                allDone = false;
            }
        }
        return allDone;
    }

    public static boolean getStreakCompletedToday(Context context) {
        SharedPreferences prefs = getSharedPreferencesHelper().getSharedPreferences(context);
        return prefs.getBoolean(STREAK_COMPLETED_TODAY, false);
    }

    public static void setStreakCompletedToday(Context context, boolean completion) {
        SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
        editor.putBoolean(STREAK_COMPLETED_TODAY, completion);
        editor.apply();
    }

    public static int getCurrentStreak(Context context) {
        SharedPreferences prefs = getSharedPreferencesHelper().getSharedPreferences(context);
        return prefs.getInt(CURRENT_STREAKS_KEY, 0);
    }

    public static void setCurrentStreak(Context context, int streak) {
        SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
        editor.putInt(CURRENT_STREAKS_KEY, streak);
        editor.apply();
    }

    public static int getLongestStreak(Context context) {
        SharedPreferences prefs = getSharedPreferencesHelper().getSharedPreferences(context);
        return prefs.getInt(LONGEST_STREAKS_KEY, 0);
    }

    public static void setLongestStreak(Context context, int newStreak) {
        SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
        editor.putInt(LONGEST_STREAKS_KEY, newStreak);
        editor.apply();
    }

    public static void updateStreakInformation(Context context, HarnessDatabase db) {
        if (canAddStreak(db)) {
            SharedPreferences prefs = getSharedPreferencesHelper().getSharedPreferences(context);
            int currentStreak = getCurrentStreak(context);
            currentStreak += 1;
            SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
            editor.putInt(CURRENT_STREAKS_KEY, currentStreak);
            editor.apply();
            setStreakCompletedToday(context, true);

            int longestStreak = getLongestStreak(context);
            if (currentStreak > longestStreak) {
                longestStreak += 1;
                setLongestStreak(context, longestStreak);

            }
        }
    }

    public static void resetStreakInformation(Context context) {
        boolean streakCompletedToday = getStreakCompletedToday(context);

        if (!streakCompletedToday) {
            setCurrentStreak(context, 0);
        } else {
            setStreakCompletedToday(context, false);
        }
    }
}
