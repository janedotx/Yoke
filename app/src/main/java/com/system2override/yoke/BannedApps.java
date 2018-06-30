package com.system2override.yoke;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class BannedApps {
    private static final String TAG = "BannedApps";
    private static final String bannedAppsFile = "BANNED";
    private static final String bannedAppsKey = "BANNED_APPS";
    private static final String bannedAppsTime = "BANNED_APPS_TIME";

    private static Set<String> apps = null;

    public static Set<String> getApps(Context context) {
        Log.d(TAG, "getApps: ");
        if (apps == null) {
            Log.d(TAG, "getApps: hello null");
            SharedPreferences prefs = context.getSharedPreferences(fullPath(), Context.MODE_PRIVATE);
            apps = prefs.getStringSet(bannedAppsKey, new HashSet<String>());
        }
        return apps;
    }

    public static void setApps(Context context, Set<String> newApps) {
        apps = newApps;
        SharedPreferences.Editor editor = context.getSharedPreferences(fullPath(), Context.MODE_PRIVATE).edit();
        editor.putStringSet(bannedAppsKey, newApps);
        editor.apply();

    }

    public static void addApp(Context context, String newApp) {
        Set<String> apps = getApps(context);
        apps.add(newApp);
        setApps(context, apps);
    }

    public static long addTime(Context context, long time) {
        SharedPreferences.Editor editor = context.getSharedPreferences(fullPath(), Context.MODE_PRIVATE).edit();
        SharedPreferences prefs = context.getSharedPreferences(fullPath(), Context.MODE_PRIVATE);
        Long currentTime = prefs.getLong(bannedAppsTime, 0L);
        currentTime += time;
        editor.putLong(bannedAppsTime, currentTime);
        editor.apply();
        return currentTime;
    }

    public static long getTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(fullPath(), Context.MODE_PRIVATE);
        return prefs.getLong(bannedAppsTime, 0L);
    }

    private static String fullPath() {
        return MyApplication.packageName + "." + bannedAppsFile;
    }

}
