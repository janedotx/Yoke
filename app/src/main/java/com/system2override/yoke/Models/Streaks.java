package com.system2override.yoke.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.OttoMessages.StreakUpdateEvent;
import com.system2override.yoke.OttoMessages.ToDoCreated;

import java.util.List;

// i don't seem to need this.editor.clear() to get things to work as i expect, e.g. the streak
// information survives a force stop/restart, though this is hard to test ATM since i don't
// use a persistent database
// just tried the midnight reset stuff (by setting the alarm to go off 15s after deployment)
// and it seems to be working as expected.
// i hate this.
// i do not understand why the banned apps stuff wasn't working properly.
public class Streaks extends SharedPreferencesModel {
    private static final String FILE = "STREAKS_FILE";
    private static final String CURRENT_STREAKS_KEY = "CURRENT_STREAKS_KEY";
    private static final String LONGEST_STREAKS_KEY = "LONGEST_STREAKS_KEY";
    private static final String STREAK_COMPLETED_TODAY = "STREAK_COMPLETED_TODAY";
    private Bus bus;

    private SharedPreferences getSharedPrefs() {
        return this.context.getSharedPreferences(FILE, 0);
    }

    private SharedPreferences.Editor getEditor() {
        return this.context.getSharedPreferences(FILE, 0).edit();
    }

    public Streaks(Context c, Bus bus) {
        super(c);
        this.bus = bus;
        this.bus.register(this);
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

    // it is unfortunate we have this variable, but we need it in case the user checks off everything,
    // and then unchecks one item. we need some way to distinguish between the case where unchecking
    // an item undoes a streak, and the case where the user is checking off one more item but has not
    // yet completed a streak. without this variable, the only knowledge we have is whether we're
    // eligible for a streak completion. without this variable, we can't store any state about the day
    public boolean getStreakCompletedToday() {
        return this.prefs.getBoolean(STREAK_COMPLETED_TODAY, false);
    }

    // TODO
    // create new editor object each time, perhaps?
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
        int longestStreak = getLongestStreak();

        if (canAddStreak(habits)) {
            currentStreak += 1;
            setCurrentStreak(currentStreak);
            setStreakCompletedToday(true);

            if (currentStreak > longestStreak) {
                setLongestStreak(currentStreak);

            }
        } else {
            // this case is for when the user checks off all their dailies, and then unchecks one or more
            if (getStreakCompletedToday()) {
                setStreakCompletedToday(false);
                setCurrentStreak(currentStreak - 1);
                if (currentStreak == longestStreak) {
                    setLongestStreak(currentStreak - 1);
                }
            }
        }
        this.bus.post(new StreakUpdateEvent());
    }

    public void endStreakDay() {
        boolean streakCompletedToday = getStreakCompletedToday();

        if (!streakCompletedToday) {
            setCurrentStreak(0);
        } else {
            setStreakCompletedToday(false);
        }

        this.bus.post(new StreakUpdateEvent());
    }

    @Subscribe
    public void onNewToDoCreation(ToDoCreated e) {
        int currentStreak = getCurrentStreak();
        int longestStreak = getLongestStreak();

        if (getStreakCompletedToday()) {
            setStreakCompletedToday(false);
            if (longestStreak == currentStreak) {
                setLongestStreak(longestStreak - 1);
            }
            setCurrentStreak(currentStreak - 1);
        }
        this.bus.post(new StreakUpdateEvent());
    }

}
