package com.system2override.yoke;

import android.app.usage.UsageEvents;

import java.util.HashMap;

public class InProcessAppDataCache {
    private static UsageEvents.Event lastEvent = null;
    // key: package name
    // val: cumulative time so far
    private static HashMap<String, Long> counters = new HashMap<String, Long>();

    public static void addTime(String key, long time) {
        Long val = counters.get(key);
        Long newTime = Long.valueOf(time);
        if (val == null || val == Long.valueOf(0)) {
            counters.put(key, newTime);
        } else {
            counters.put(key, val + newTime);
        }
    }

    public static UsageEvents.Event getLastEvent() {
        return lastEvent;
    }

    public static void setLastEvent(UsageEvents.Event event) {
        InProcessAppDataCache.lastEvent = event;
    }
}
