package com.system2override.yoke.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.system2override.yoke.Models.SharedPreferencesHelper;

import java.util.HashSet;
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

}
