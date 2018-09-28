package com.system2override.yoke.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.squareup.otto.Bus;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.OttoMessages.TimeBankEarnedTime;
import com.system2override.yoke.OttoMessages.TimeBankUnearnedTime;

public class TimeBank extends SharedPreferencesModel {
    private static final String TAG = "TimeBank";
    public static final String RESET_ACTION = MyApplication.packageName + ".RESET";

    // all these are in milliseconds
    public static final long MAX_INITIAL_TIME = 2 * 60 * 60 * 1000;
    public static final long MAX_REFRESH_DAILY_TIME = 15 * 60 * 1000;
    public static final long MAX_REFRESH_ONE_OFF_TIME = 5 * 60 * 1000;

    private final String INITIAL_TIME_GRANT_KEY = "INITIAL_TIME_GRANT_KEY";
    private final String REWARD_GRANT_TIME_KEY = "REWARD_GRANT_TIME_KEY";
    private final String DAILY_GRANT_TIME_KEY = "DAILY_GRANT_TIME_KEY";
    private final String ONEOFF_GRANT_TIME_KEY = "ONEOFF_GRANT_TIME_KEY";
    private final String TIME_SPENT_KEY = "TIME_SPENT_BANNED_APPS_KEY";
    private final String TIME_EARNED_KEY = "TIME_EARNED_KEY";
    public final String FILE = "TIME";

    private Bus bus;

    public TimeBank(Context context, Bus bus) {
        super(context);
        this.bus = bus;
    }

    // convenience methods for toppig up daily ration and all that
    // so we need a separate variable that account for total allowable time
    // so at the end of each day, this variable is what's cleared
    // whenever the user gets more time, this variable is increased

    // intended for use when tracking amount of time spent in foreground for a bad app
    public long addSpentTime(long time) {
        long currentTime = this.prefs.getLong(TIME_SPENT_KEY, 0L);
        currentTime += time;
        this.editor.putLong(TIME_SPENT_KEY, currentTime);
        this.editor.apply();
        return currentTime;
    }

    public long getSpentTime() {
        return this.prefs.getLong(TIME_SPENT_KEY, 0L);
    }

    public long getEarnedTime() {
        return prefs.getLong(TIME_EARNED_KEY, 0L);
    }

    public long earnTime(ToDoInterface todo) {
        long earnedTime = this.prefs.getLong(TIME_EARNED_KEY, 0L);
        if (todo.getIsDailyHabit()) {
            earnedTime += getDailyTimeGrant();
        } else {
            earnedTime += getOneoffTimeGrant();
        }
        this.editor.putLong(TIME_EARNED_KEY, earnedTime);
        this.editor.apply();
        Log.d(TAG, "earnTime: about to post");

        this.bus.post(new TimeBankEarnedTime());

        return earnedTime;
    }
    public void setAvailableTime(long time) {
    }

    // careful with this method, don't let current time get negative--though does it really matter?
    public long unearnTime(ToDoInterface todo) {
        long currentTime = this.prefs.getLong(TIME_EARNED_KEY, 0L);
        if (todo.getIsDailyHabit()) {
            currentTime -= getDailyTimeGrant();
        } else {
            currentTime -= getOneoffTimeGrant();
        }
        this.editor.putLong(TIME_EARNED_KEY, currentTime);
        this.editor.apply();

        this.bus.post(new TimeBankUnearnedTime());

        return currentTime;
    }

    public void resetTime() {
        // reset spent time
        this.editor.putLong(TIME_SPENT_KEY, 0);
        this.editor.apply();

        this.editor.putLong(TIME_EARNED_KEY, 0);
        this.editor.apply();


    }

    public void setInitialTime(long initialTime) {
        this.editor.putLong(INITIAL_TIME_GRANT_KEY, initialTime);
        this.editor.apply();
    }

    public long getInitialTime() {
        return this.prefs.getLong(INITIAL_TIME_GRANT_KEY, 0L);
    }

    public void setRewardTimeGrant(long time) {
        this.editor.putLong(REWARD_GRANT_TIME_KEY, time);
        this.editor.apply();
    }

    public long getDailyTimeGrant() {
        return this.prefs.getLong(DAILY_GRANT_TIME_KEY, 0L);
    }

    public void putDailyHabitTimeGrant(long time) {
        this.editor.putLong(DAILY_GRANT_TIME_KEY, time);
        this.editor.apply();
    }

    public long getOneoffTimeGrant() {
        return this.prefs.getLong(ONEOFF_GRANT_TIME_KEY, 0L);
    }

    public void putOneoffTimeGrant(long time) {
        this.editor.putLong(ONEOFF_GRANT_TIME_KEY, time);
        this.editor.apply();
    }

    public long getTimeRemaining() {
        return getEarnedTime() + getInitialTime() - getSpentTime();
    }

    public long getTotalTimeForToday() {
        return getEarnedTime() + getInitialTime();
    }

}
