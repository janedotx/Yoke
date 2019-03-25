package com.system2override.hobbes.Models;

import android.content.Context;
import android.util.Log;

public class OneTimeData extends SharedPreferencesModel{
    private static final String TAG = "OneTimeData";

    private final String FILE = "ONE_TIME_FILE";

    private final String HAS_DONE_ONBOARDING_KEY = "HAS_DONE_ONBOARDING_KEY";
    private final String HAS_DONE_TUTORIAL_KEY = "HAS_DONE_TUTORIAL_KEY";
    private final String TIME_INSTALLED_KEY = "TIME_INSTALLED_KEY";
    private final String AVERAGE_DAILY_USE_BEFORE_INSTALL = "AVERAGE_DAILY_USE_BEFORE_INSTALL";
    private final String FIRST_INSTALL_COMPLETE = "FIRST_INSTALL_COMPLETE";
    private final String FIRST_DAILY_COMPLETED = "FIRST_DAILY_COMPLETED";
    private final String FIRST_RECURRING_COMPLETED = "FIRST_RECURRING_COMPLETED";

    public OneTimeData(Context context) {
        super(context);
    }

    public boolean getHasDoneOnboardingKey() {
        return this.prefs.getBoolean(HAS_DONE_ONBOARDING_KEY, false);
    }

    public void setHasDoneOnboardingKey(boolean tutorial) {
        Log.d(TAG, "setHasDoneOnboardingKey: " + Boolean.toString(tutorial));
        this.editor.putBoolean(HAS_DONE_ONBOARDING_KEY, tutorial);
        this.editor.apply();
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

    public void setAverageDailyUsageBeforeHobbes(long time) {
        Log.d(TAG, "setAverageDailyUsageOverall: time " + Long.toString(time));
        this.editor.putLong(AVERAGE_DAILY_USE_BEFORE_INSTALL, time);
        this.editor.apply();
    }

    public long getAverageDailyUsageBeforeHobbes() {
        return this.prefs.getLong(AVERAGE_DAILY_USE_BEFORE_INSTALL, 0);
    }

    public long getTimeOfHobbesInstall() {
        return this.prefs.getLong(TIME_INSTALLED_KEY, 0);
    }

    public boolean getFirstInstallIncomplete() {
        return this.prefs.getBoolean(FIRST_INSTALL_COMPLETE, true);
    }

    public void setFirstInstallIncomplete(boolean b) {
        this.editor.putBoolean(FIRST_INSTALL_COMPLETE, b);
        this.editor.apply();
    }

    public boolean getFirstDailyCompleted() {
        return this.prefs.getBoolean(FIRST_DAILY_COMPLETED, false);
    }

    public void setFirstDailyCompleted(boolean b) {
        this.editor.putBoolean(FIRST_DAILY_COMPLETED, b);
        this.editor.apply();
    }

    public boolean getFirstRecurringCompleted() {
        return this.prefs.getBoolean(FIRST_RECURRING_COMPLETED, false);
    }

    public void setFirstRecurringCompleted(boolean b) {
        this.editor.putBoolean(FIRST_RECURRING_COMPLETED, b);
        this.editor.apply();
    }
}
