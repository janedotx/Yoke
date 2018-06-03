package com.system2override.yoke;

import android.app.usage.UsageEvents;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class InProcessAppDataCache {
    private static final String TAG = "InProcessAppDataCache";
    private static UsageEvents.Event lastEvent = null;
    private static long lastLoopEventTime;
    // key: package name
    // val: an array of longs.
    /*
    * by summing up the two longs, we'll get the cumulative time that an app has been seen. the reason
    * it has to be split is because if an app is currently active, we need to kill it as soon as the
    * cumulative time spent in it > the rule. we never want to miss an event, so we always save the most
    * recently seen event, and query the manager for the events in the time interval between the MRSE
    * and current time.
    *
    * Now imagine that an activity is in the foreground and this thread is running. we need to keep
    * adding time to the counter. because threads are not guaranteed to sleep for the exact time
    * configured, we gain the most accuracy by adding (Time.now - MRSE.time) to the counter. that
    * basically means updating the counter, not adding time--every time we take a measurement, we
    * replace the last measurement with the most up to date one. and we don't want to clobber any
    * time tracked from earlier periods when the activity was in the foreground.
    * does super time accuracy matter though? i mean what's wrong with just incrementing the counter
    * by the amount of time the thread sleeps and leaving it at that, even if the thread oversleeps
    * sometimes? keeping track of the time before the current interval, and the time since the
    * current interval began separately and then adding them together seems terrible
     */
    private static HashMap<String, Long> counters = new HashMap<String, Long>();

    public static void addTime(String key, long time) {
        Long val = counters.get(key);
        Long newTime = Long.valueOf(time);
        if (val == null || val == Long.valueOf(0)) {
            counters.put(key, newTime);
            Log.d(TAG, "addTime: adding time for the first time to " + key);
        } else {
            counters.put(key, val + newTime);
        }
        Log.d(TAG, "addTime: " + key + " " + Long.toString(counters.get(key)));
    }

    public long getTime(String key) {
        if (counters.get(key) == null) {
            return 0;
        }
        return Long.valueOf(counters.get(key));
    }

    public static UsageEvents.Event getLastEvent() {
        return lastEvent;
    }

    public static boolean hasLastEvent() {
        return lastEvent != null;
    }

    public static String getLastEventPackageName() {
        return lastEvent.getPackageName();
    }

    public static void setLastEvent(UsageEvents.Event event) {
        InProcessAppDataCache.lastEvent = event;
    }

    public static long getLastLoopEventTime() {
        return lastLoopEventTime;
    }

    public static void setLastLoopEventTime(long lastLoopEventTime) {
        InProcessAppDataCache.lastLoopEventTime = lastLoopEventTime;
    }

    public static HashMap<String, Long> getCounters() {
        return counters;
    }

}
