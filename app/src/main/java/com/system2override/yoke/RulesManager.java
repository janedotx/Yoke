package com.system2override.yoke;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.system2override.yoke.OttoMessages.CurrentAppMessage;
import com.system2override.yoke.OttoMessages.ForegroundMessage;

public class RulesManager {
    private static final String TAG = "RulesManager";
    private Context context;
    private Intent launcherIntent;

    public RulesManager(Context context) {
        this.context = context;
        this.launcherIntent = new Intent("android.intent.action.MAIN");
        this.launcherIntent.addCategory("android.intent.category.HOME");

        MyApplication.getBus().register(this);
    }

    @com.squareup.otto.Subscribe
    public void processForegroundMessage(CurrentAppMessage currentAppMessage) {
        Log.d(TAG, "processForegroundMessage: called me!");
        boolean inBadApp = BannedApps.getApps(this.context).contains(currentAppMessage.getCurrentApp());
        long availableTime = TimeBank.getAvailableTime(this.context);
        long timeSpent = TimeBank.getSpentTime(this.context);

        if (timeSpent >= availableTime && inBadApp) {
            this.context.startActivity(this.launcherIntent);
            return;
        }

        if (inBadApp) {
            TimeBank.addSpentTime(this.context, ForegroundAppObserverThread.SLEEP_LENGTH);
        }
        Log.d(TAG, "processForegroundMessage: spentTime " + Long.toString(timeSpent));

    }

}
