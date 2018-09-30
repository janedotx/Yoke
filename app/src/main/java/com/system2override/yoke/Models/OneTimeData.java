package com.system2override.yoke.Models;

import android.content.Context;

public class OneTimeData extends SharedPreferencesModel{
    private static final String TAG = "OneTimeData";
    private final String FILE = "ONE_TIME_FILE";

    private final String HAS_DONE_TUTORIAL_KEY = "HAS_DONE_TUTORIAL_KEY";
    private final String TIME_INSTALLED_KEY = "TIME_INSTALLED_KEY";

    public OneTimeData(Context context) {
        super(context);
    }

    public boolean getHasDoneTutorialKey() {
        return this.prefs.getBoolean(HAS_DONE_TUTORIAL_KEY, false);
    }

    public void setHasDoneTutorialKey(boolean tutorial) {
        this.editor.putBoolean(HAS_DONE_TUTORIAL_KEY, tutorial);
        this.editor.apply();
    }

    public void setTimeOfHobbesInstall(long time) {

    }
}
