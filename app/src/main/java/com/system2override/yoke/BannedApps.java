package com.system2override.yoke;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class BannedApps {
    private static final String TAG = "BannedApps";
    private static final String BANNED_APPS_FILE = "BANNED";
    private static final String BANNED_APPS_KEY = "BANNED_APPS";

    private static Set<String> apps = null;

    public static Set<String> getApps(Context context) {
        Log.d(TAG, "getApps: ");
        if (apps == null) {
            Log.d(TAG, "getApps: hello null");
            SharedPreferences prefs = context.getSharedPreferences(fullPath(), Context.MODE_PRIVATE);
            apps = prefs.getStringSet(BANNED_APPS_KEY, new HashSet<String>());
        }
        return apps;
    }

    public static void setApps(Context context, Set<String> newApps) {
        apps = newApps;
        SharedPreferences.Editor editor = context.getSharedPreferences(fullPath(), Context.MODE_PRIVATE).edit();
        editor.putStringSet(BANNED_APPS_KEY, newApps);
        editor.apply();

    }

    public static void addApp(Context context, String newApp) {
        Set<String> apps = getApps(context);
        apps.add(newApp);
        setApps(context, apps);
    }

    private static String fullPath() {
        return MyApplication.packageName + "." + BANNED_APPS_FILE;
    }

}
