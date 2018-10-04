package com.system2override.hobbes.Models;

import android.content.Context;
import android.util.Log;

public class OneTimeData extends SharedPreferencesModel{
    private static final String TAG = "OneTimeData";

    private final String FILE = "ONE_TIME_FILE";

    private final String HAS_DONE_TUTORIAL_KEY = "HAS_DONE_TUTORIAL_KEY";
    private final String TIME_INSTALLED_KEY = "TIME_INSTALLED_KEY";
    private final String AVERAGE_DAILY_USE = "AVERAGE_DAILY_USE";

    public OneTimeData(Context context) {
        super(context);
    }

    public boolean getHasDoneTutorialKey() {
        return this.prefs.getBoolean(HAS_DONE_TUTORIAL_KEY, false);
    }

    public void setHasDoneTutorialKey(boolean tutorial) {
        Log.d(TAG, "setHasDoneTutorialKey: " + Boolean.toString(tutorial));
        this.editor.putBoolean(HAS_DONE_TUTORIAL_KEY, tutorial);
        this.editor.apply();
    }

    public void setTimeOfHobbesInstall(long time) {
        this.editor.putLong(TIME_INSTALLED_KEY, time);
        this.editor.apply();
    }

    public void setAverageDailyUsageOverall(long time) {
        Log.d(TAG, "setAverageDailyUsageOverall: time " + Long.toString(time));
        this.editor.putLong(AVERAGE_DAILY_USE, time);
        this.editor.apply();
    }

    public long getAverageDailyUsageOverall() {
        return this.prefs.getLong(AVERAGE_DAILY_USE, 0);
    }

    public long getTimeOfHobbesInstall() {
        return this.prefs.getLong(TIME_INSTALLED_KEY, 0);
    }

    public boolean firstInstallIncomplete() {
        return (this.prefs.getLong(TIME_INSTALLED_KEY, 0) == 0);
    }
}
