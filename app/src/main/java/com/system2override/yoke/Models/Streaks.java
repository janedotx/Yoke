package com.system2override.yoke.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.MyApplication;

import java.util.List;

public class Streaks extends SharedPreferencesModel {
    private static final String FILE = "STREAKS_FILE";
    private static final String CURRENT_STREAKS_KEY = "CURRENT_STREAKS_KEY";
    private static final String LONGEST_STREAKS_KEY = "LONGEST_STREAKS_KEY";
    private static final String STREAK_COMPLETED_TODAY = "STREAK_COMPLETED_TODAY";

    public Streaks(Context c) {
        super(c);
    }

    public boolean canAddStreak(List<Habit> habits) {
        boolean allDone = true;
        for (Habit h: habits) {
            if (!h.completedOn(MyApplication.getTodayCalObj())) {
                allDone = false;
            }
        }
        return allDone;
    }

    public boolean getStreakCompletedToday() {
        return this.prefs.getBoolean(STREAK_COMPLETED_TODAY, false);
    }

    public void setStreakCompletedToday(boolean completion) {
        this.editor.putBoolean(STREAK_COMPLETED_TODAY, completion);
        this.editor.apply();
    }

    public int getCurrentStreak() {
        return this.prefs.getInt(CURRENT_STREAKS_KEY, 0);
    }

    public void setCurrentStreak(int streak) {
        this.editor.putInt(CURRENT_STREAKS_KEY, streak);
        this.editor.apply();
    }

    public int getLongestStreak() {
        return this.prefs.getInt(LONGEST_STREAKS_KEY, 0);
    }

    public void setLongestStreak(int newStreak) {
        this.editor.putInt(LONGEST_STREAKS_KEY, newStreak);
        this.editor.apply();
    }

    public void updateStreakInformation(List<Habit> habits) {
        int currentStreak = getCurrentStreak();
        if (canAddStreak(habits)) {
            currentStreak += 1;
            setCurrentStreak(currentStreak);
            setStreakCompletedToday(true);
        } else {
            if (getStreakCompletedToday()) {
                setStreakCompletedToday(false);
                setCurrentStreak(currentStreak - 1);
            }
        }
    }

    public void endStreakDay(List<Habit> habits) {
        boolean streakCompletedToday = getStreakCompletedToday();

        if (!streakCompletedToday) {
            setCurrentStreak(0);
        } else {
            setStreakCompletedToday(false);
        }

        int longestStreak = getLongestStreak();
        int currentStreak = getCurrentStreak();

        if (currentStreak > longestStreak) {
            setLongestStreak(currentStreak);

        }
    }

}
