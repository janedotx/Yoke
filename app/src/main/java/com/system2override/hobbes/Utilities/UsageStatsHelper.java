package com.system2override.hobbes.Utilities;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class UsageStatsHelper {
    // 1000 * 60 * 60 * 24 * 7
    public static final long WEEK_IN_MS = 604800000;
    public static final long DAY_IN_MS = 86400000;
    public static final long TWELVE_HOURS_IN_MS = 1000 * 60 * 60 * 12;
    public static final long EIGHT_HOURS_IN_MS = 1000 * 60 * 60 * 8;
    public static final long SIX_HOURS_IN_MS = 1000 * 60 * 60 * 6;

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
    public static Map<String, Long> getAppsByTotalTime(Context context, long interval, long end) {
        UsageStatsManager usageStatsManager = getUsageStatsManager(context);
        Log.d(TAG, "getAppsByTotalTime: ");
        ArrayList<String>  arrayList = new ArrayList<>();
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            List<UsageStats> appList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,  end - interval, end);
            if (appList != null && appList.size() > 0) {
                Map<String, Long> packageTimesMap = new HashMap<String, Long>();
                for (UsageStats usageStats : appList) {
                    String packageName = usageStats.getPackageName();
                    Long curValue = packageTimesMap.get(packageName);
                    long timeInForeground = usageStats.getTotalTimeInForeground();
                    if (curValue == null) {
                        packageTimesMap.put(usageStats.getPackageName(), timeInForeground);
                    } else {
                        long oldTime = curValue.longValue();
                        oldTime += timeInForeground;
                        Log.d(TAG, "getAppsByTotalTime: " + packageName + " " + oldTime);
                        packageTimesMap.put(packageName, oldTime);
                    }
                }
                Log.d(TAG, "getAppsByTotalTime: app lisst was null");
                return packageTimesMap;
            }
        }

        return new HashMap<String, Long>();

    }

    public static long sumTotalTimeOverInterval(Map<String, Long> map) {
        long total = 0;
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry pair = (Map.Entry) iter.next();
            total += ((Long)pair.getValue()).longValue();
        }
        return total;
    }

    public static SortedMap<Long, String> sortAppsByTime(Map<String, Long> apps) {

        SortedMap<Long, String> mySortedMap = new TreeMap<Long, String>();
        for (String key: apps.keySet()) {
            // multiply by -1 so it sorts from largest to smallest
            mySortedMap.put(apps.get(key) * -1, key);
        }
        return mySortedMap;
    }

    public static List<Map.Entry<Long, String>> convertSortedMapToList(Map<Long, String> map) {
        List<Map.Entry<Long, String>> list = new ArrayList<Map.Entry<Long, String>>();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry pair = (Map.Entry) iter.next();
            list.add(pair);
        }
        return list;
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
