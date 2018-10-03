package com.system2override.hobbes.Models;

import android.content.Context;

public class BeforeHobbesAppData extends SharedPreferencesModel {
    private final String FILE = "BEFORE_HOBBES_FILE";

    private final String AVERAGE_TIME_KEY = "AVERAGE_TIME_KEY";

    public BeforeHobbesAppData(Context context) {
        super(context);
    }

    public long getTimeForApp(String appName) {
        return this.prefs.getLong(appName,0);
    }

    public void setTimeForApp(String appName, long time) {
        this.editor.putLong(appName, time);
        this.editor.apply();
    }

    public void setAverageTime(long time) {
        this.editor.putLong(AVERAGE_TIME_KEY, time);
        this.editor.apply();
    }
}
