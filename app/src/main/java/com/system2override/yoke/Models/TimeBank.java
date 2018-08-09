package com.system2override.yoke.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.squareup.otto.Bus;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.OttoMessages.TimeBankEarnedTime;

public class TimeBank extends BaseObservable {
    private static final String TAG = "TimeBank";
    public static final String RESET_ACTION = MyApplication.packageName + ".RESET";

    private final String INITIAL_TIME_GRANT_KEY = "INITIAL_TIME_GRANT_KEY";
    private final String REWARD_GRANT_TIME_KEY = "REWARD_GRANT_TIME_KEY";
    private final String TIME_SPENT_KEY = "TIME_SPENT_BANNED_APPS_KEY";
    private final String TIME_AVAILABLE_KEY = "TIME_AVAILABLE_KEY";
    private final String TIME_FILE = "TIME";

    private SharedPreferencesHelper helper;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;
    private Bus bus;
    private long availableTime;
    private long spentTime;
    private long initialTime;
    private long rewardGrantTime;

    public TimeBank(Context context, Bus bus) {
        this.context = context;
        this.bus = bus;
        this.prefs = getSharedPreferencesHelper().getSharedPreferences(this.context);
        this.editor = getSharedPreferencesHelper().getSharedPreferencesEditor(this.context);

        this.availableTime = this.prefs.getLong(TIME_AVAILABLE_KEY, 0L);
        this.spentTime = this.prefs.getLong(TIME_SPENT_KEY, 0L);
        this.initialTime = this.prefs.getLong(INITIAL_TIME_GRANT_KEY, 0L);
        this.rewardGrantTime = this.prefs.getLong(REWARD_GRANT_TIME_KEY, 0L);
    }

    private SharedPreferencesHelper getSharedPreferencesHelper(){
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
    public long addSpentTime(Context context, long time) {
        SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
        SharedPreferences prefs = getSharedPreferencesHelper().getSharedPreferences(context);
        long currentTime = prefs.getLong(TIME_SPENT_KEY, 0L);
        currentTime += time;
        editor.putLong(TIME_SPENT_KEY, currentTime);
        editor.apply();
        return currentTime;
    }

    public long getSpentTime(Context context) {
        SharedPreferences prefs = getSharedPreferencesHelper().getSharedPreferences(context);
        return prefs.getLong(TIME_SPENT_KEY, 0L);
    }

    public long getAvailableTime() {
        return prefs.getLong(TIME_AVAILABLE_KEY, 0L);
    }

    public long earnTime(Context context) {
        SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
        SharedPreferences prefs = getSharedPreferencesHelper().getSharedPreferences(context);

        long refreshTime = prefs.getLong(REWARD_GRANT_TIME_KEY, 0l);
        long currentTime = prefs.getLong(TIME_AVAILABLE_KEY, 0L);
        currentTime += refreshTime;
        editor.putLong(TIME_AVAILABLE_KEY, currentTime);
        editor.apply();
        this.availableTime = currentTime;
        Log.d(TAG, "earnTime: about to post");

        this.bus.post(new TimeBankEarnedTime());

        return currentTime;
    }
    public void setAvailableTime(long time) {
    }

    // careful with this method, don't let current time get negative--though does it really matter?
    public long unearnTime(Context context) {
        SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
        SharedPreferences prefs = getSharedPreferencesHelper().getSharedPreferences(context);

        long refreshTime = prefs.getLong(REWARD_GRANT_TIME_KEY, 0l);
        long currentTime = prefs.getLong(TIME_AVAILABLE_KEY, 0L);
        currentTime -= refreshTime;
        editor.putLong(TIME_AVAILABLE_KEY, currentTime);
        editor.apply();

        return currentTime;
    }

    public void resetTime(Context context) {
        // reset spent time
        SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
        editor.putLong(TIME_SPENT_KEY, 0);
        editor.apply();

        // reset total available time to just the initial time

        SharedPreferences sharedPreferences = getSharedPreferencesHelper().getSharedPreferences(context);
        long initialGrantTime = sharedPreferences.getLong(INITIAL_TIME_GRANT_KEY, 0L);
        editor.putLong(TIME_AVAILABLE_KEY, initialGrantTime);
        editor.apply();

    }

    public void setInitialTime(Context context, long initialTime) {
        SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
        editor.putLong(INITIAL_TIME_GRANT_KEY, initialTime);
        editor.apply();
    }

    public long getInitialTime(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferencesHelper().getSharedPreferences(context);
        return sharedPreferences.getLong(INITIAL_TIME_GRANT_KEY, 0L);
    }

    public void setRewardTimeGrant(Context context, long time) {
        SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
        editor.putLong(REWARD_GRANT_TIME_KEY, time);
        editor.apply();
    }

    public long getRewardTimeGrant(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferencesHelper().getSharedPreferences(context);
        return sharedPreferences.getLong(REWARD_GRANT_TIME_KEY, 0L);
    }

    public long getTotalEarnedTimeToday(Context context) {
        return getAvailableTime() - getInitialTime(context);
    }

    public long getTimeRemaining(Context context) {
        return getAvailableTime() - getSpentTime(context);
    }

}
