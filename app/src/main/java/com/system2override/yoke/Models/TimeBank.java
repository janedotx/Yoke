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
    public static final long MAX_REFRESH_ONE_OFF_TIME = 10 * 60 * 1000;

    private final String INITIAL_TIME_GRANT_KEY = "INITIAL_TIME_GRANT_KEY";
    private final String REWARD_GRANT_TIME_KEY = "REWARD_GRANT_TIME_KEY";
    private final String TIME_SPENT_KEY = "TIME_SPENT_BANNED_APPS_KEY";
    private final String TIME_AVAILABLE_KEY = "TIME_AVAILABLE_KEY";
    public final String FILE = "TIME";

    private Bus bus;
    private long availableTime;
    private long spentTime;
    private long initialTime;
    private long rewardGrantTime;

    public TimeBank(Context context, Bus bus) {
        super(context);
        this.bus = bus;

        this.availableTime = this.prefs.getLong(TIME_AVAILABLE_KEY, 0L);
        this.spentTime = this.prefs.getLong(TIME_SPENT_KEY, 0L);
        this.initialTime = this.prefs.getLong(INITIAL_TIME_GRANT_KEY, 0L);
        this.rewardGrantTime = this.prefs.getLong(REWARD_GRANT_TIME_KEY, 0L);
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

    public long getAvailableTime() {
        return prefs.getLong(TIME_AVAILABLE_KEY, 0L);
    }

    public long earnTime() {
        long refreshTime = this.prefs.getLong(REWARD_GRANT_TIME_KEY, 0l);
        long currentTime = this.prefs.getLong(TIME_AVAILABLE_KEY, 0L);
        currentTime += refreshTime;
        this.editor.putLong(TIME_AVAILABLE_KEY, currentTime);
        this.editor.apply();
        Log.d(TAG, "earnTime: about to post");

        this.bus.post(new TimeBankEarnedTime());

        return currentTime;
    }
    public void setAvailableTime(long time) {
    }

    // careful with this method, don't let current time get negative--though does it really matter?
    public long unearnTime() {
        long refreshTime = this.prefs.getLong(REWARD_GRANT_TIME_KEY, 0l);
        long currentTime = this.prefs.getLong(TIME_AVAILABLE_KEY, 0L);
        currentTime -= refreshTime;
        this.editor.putLong(TIME_AVAILABLE_KEY, currentTime);
        this.editor.apply();

        this.bus.post(new TimeBankUnearnedTime());

        return currentTime;
    }

    public void resetTime() {
        // reset spent time
        this.editor.putLong(TIME_SPENT_KEY, 0);
        this.editor.apply();

        // reset total available time to just the initial time

        long initialGrantTime = this.prefs.getLong(INITIAL_TIME_GRANT_KEY, 0L);
        this.editor.putLong(TIME_AVAILABLE_KEY, initialGrantTime);
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

    public long getRewardTimeGrant() {
        return this.prefs.getLong(REWARD_GRANT_TIME_KEY, 0L);
    }

    public long getTotalEarnedTimeToday() {
        return getAvailableTime() - getInitialTime();
    }

    public long getTimeRemaining() {
        return getAvailableTime() - getSpentTime();
    }

}
