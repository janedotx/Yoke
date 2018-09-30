package com.system2override.yoke.Models;

import android.content.Context;

public class BeforeHobbesAppData extends SharedPreferencesModel {
    private final String FILE = "BEFORE_HOBBES_FILE";

    public BeforeHobbesAppData(Context context) {
        super(context);
    }

    public long getTimeForApp(String appName) {
        return this.prefs.getLong(appName,0);
    }

    public void setTimeForApp(String appName, long time) {
        this.editor.putLong(appName, time);
    }
}
