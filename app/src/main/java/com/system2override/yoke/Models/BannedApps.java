package com.system2override.yoke.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.system2override.yoke.Models.SharedPreferencesHelper;
import com.system2override.yoke.MyApplication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BannedApps extends SharedPreferencesModel {
    private final String TAG = "BannedApps";
    private final String FILE = "BANNED_FILE";
    private final String BANNED_APPS_KEY = "BANNED_APPS_KEY";

    public BannedApps(Context context) {
        super(context);
    }

    public Set<String> getApps() {
        return this.prefs.getStringSet(BANNED_APPS_KEY, new HashSet<String>());
    }


    public void setApps(Set<String> newApps) {
        this.editor.putStringSet(BANNED_APPS_KEY, newApps);
        this.editor.apply();
    }

    public void removeApp(String app) {
        Set<String> apps = getApps();
        apps.remove(app);
        setApps(apps);
    }

    public void addApp(String newApp) {
        Set<String> apps = getApps();
        apps.add(newApp);
        setApps(apps);
    }

    public void clearApps() {
        setApps(new HashSet<String>());
    }

    public List<ApplicationInfo> getApplicationInfoObjects() {
        List<ApplicationInfo> objs = new ArrayList<>();
        List<String> appNames = new ArrayList<>(getApps());

        for (int i = 0; i < appNames.size(); i++) {
            try {
                ApplicationInfo app = this.context.getPackageManager().getApplicationInfo(appNames.get(i), 0);
                objs.add(app);
            } catch (PackageManager.NameNotFoundException e) {
                // I don't care
            }
        }

        return objs;
    }


    public List<ApplicationInfo> getApplicationInfoObjectsWithNullPadding() {
        List<ApplicationInfo> objs = new ArrayList<>();
        List<String> appNames = new ArrayList<>(getApps());
        Log.d(TAG, "getApplicationInfoObjectsWithNullPadding: appNames.size() " + Integer.toString(appNames.size()));

        for (int i = 0; i < appNames.size(); i++) {
            try {
                ApplicationInfo app = this.context.getPackageManager().getApplicationInfo(appNames.get(i), 0);
                objs.add(app);
            } catch (PackageManager.NameNotFoundException e) {
                // I don't care
            }
        }

        for (int i = 0; i < MyApplication.BANNED_APPS_LIMIT - appNames.size(); i++) {
            objs.add(null);
        }
        Log.d(TAG, "getApplicationInfoObjectsWithNullPadding: objs.size() " + Integer.toString(objs.size()));
        return objs;
    }

    public void printBannedApps() {
        Log.d(TAG, "printBannedApps: ");
        Set<String> apps = getApps();
        Iterator<String> it = apps.iterator();
        while (it.hasNext()) {
            Log.d(TAG, "onClick: bannedapp " + it.next());

        }
    }
}
