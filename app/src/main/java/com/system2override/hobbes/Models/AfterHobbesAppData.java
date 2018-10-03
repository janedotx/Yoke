package com.system2override.hobbes.Models;

import android.content.Context;

public class AfterHobbesAppData extends SharedPreferencesModel {
    private final String FILE = "AFTER_HOBBES_FILE";

    public AfterHobbesAppData(Context context) {
        super(context);
    }

    public long getTimeForApp(String appName) {
        return this.prefs.getLong(appName,0);
    }

    public void setTimeForApp(String appName, long time) {
        this.editor.putLong(appName, time);
    }
}
