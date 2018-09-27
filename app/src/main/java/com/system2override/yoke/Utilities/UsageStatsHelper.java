package com.system2override.yoke.Utilities;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class UsageStatsHelper {

    private static final String TAG = "UsageStatsHelper";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static UsageStatsManager getUsageStatsManager(Context context) {

        if (android.os.Build.VERSION.SDK_INT == 21) {
            return (UsageStatsManager) context.getSystemService("usagestats");

        } else {
            return (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        }

    }
    // 1000 * 60 * 60 * 24 * 7
    public static Map<String, Long> getAppsByTotalTime(Context context, long interval) {
        UsageStatsManager usageStatsManager = getUsageStatsManager(context);
        ArrayList<String>  arrayList = new ArrayList<>();
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,  time - interval, time);
            if (appList != null && appList.size() > 0) {
                Map<String, Long> packageTimesMap = new HashMap<String, Long>();
                for (UsageStats usageStats : appList) {
                    String packageName = usageStats.getPackageName();
                    Long curValue = packageTimesMap.get(packageName);
                    long timeInForeground = usageStats.getTotalTimeInForeground();
                    
                    if (curValue == null) {
                        Log.d(TAG, "getAppsByTotalTime: value was null for " + packageName);
                        packageTimesMap.put(usageStats.getPackageName(), timeInForeground);
                    } else {
                        long oldTime = curValue.longValue();
                        oldTime += timeInForeground;
                        packageTimesMap.put(packageName, oldTime);
                    }
                }
                return packageTimesMap;
            }
        }

        return new HashMap<String, Long>();

    }

    public static SortedMap<Long, String> sortAppsByTime(Map<String, Long> apps) {

        SortedMap<Long, String> mySortedMap = new TreeMap<Long, String>();
        for (String key: apps.keySet()) {
            mySortedMap.put(apps.get(key), key);
        }
        return mySortedMap;
    }

    /*
    public static UsageStats getUsageStatsForAppList(Context context, List<String> app) {

    }
    */

    public static ArrayList<String> getFirstEntries(int max, SortedMap<Long, UsageStats> source, List<String> appStrings) {
        int count = 0;
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<Long, UsageStats> entry:source.entrySet()) {
            if (count >= max) break;

            String packageName = entry.getValue().getPackageName();
            if (appStrings.contains(packageName)) {
                list.add(packageName);
            } else {
                continue;
            }
            count++;
        }
        return list;
    }
}
