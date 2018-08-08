package com.system2override.yoke.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.system2override.yoke.Models.SharedPreferencesHelper;

import java.util.HashSet;
import java.util.Set;

public class BannedApps {
    private static final String TAG = "BannedApps";
    private static final String BANNED_APPS_FILE = "BANNED_FILE";
    private static final String BANNED_APPS_KEY = "BANNED_APPS_KEY";

    private static SharedPreferencesHelper helper;

    private static SharedPreferencesHelper getSharedPreferencesHelper() {
        if (helper == null) {
            helper = new SharedPreferencesHelper(BANNED_APPS_FILE);
        }
        return helper;
    }

    public static Set<String> getApps(Context context) {
        SharedPreferences prefs = getSharedPreferencesHelper().getSharedPreferences(context);
        return prefs.getStringSet(BANNED_APPS_KEY, new HashSet<String>());
    }


    public static void setApps(Context context, Set<String> newApps) {
        SharedPreferences.Editor editor = getSharedPreferencesHelper().getSharedPreferencesEditor(context);
        editor.putStringSet(BANNED_APPS_KEY, newApps);
        editor.apply();
    }

    public static void addApp(Context context, String newApp) {
        Set<String> apps = getApps(context);
        apps.add(newApp);
        setApps(context, apps);
    }

    public static void clearApps(Context context) {
        setApps(context, new HashSet<String>());
    }

}
