package com.system2override.yoke;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.system2override.yoke.AppLimit.AppLimitScreen;
import com.system2override.yoke.Models.BannedApps;
import com.system2override.yoke.Models.TimeBank;
import com.system2override.yoke.OttoMessages.CurrentAppMessage;

public class RulesManager {
    private static final String TAG = "RulesManager";
    private Context context;
    private Intent launcherIntent;

    public RulesManager(Context context) {
        this.context = context;
//        this.launcherIntent = new Intent("android.intent.action.MAIN");
//        this.launcherIntent.addCategory("android.intent.category.HOME");
        this.launcherIntent = new Intent(this.context, AppLimitScreen.class);

        MyApplication.getBus().register(this);
    }

    @com.squareup.otto.Subscribe
    public void processForegroundMessage(CurrentAppMessage currentAppMessage) {
 //       Log.d(TAG, "processForegroundMessage: called me!");
        boolean inBadApp = MyApplication.getBannedApps().getApps().contains(currentAppMessage.getCurrentApp());
        TimeBank timeBank = MyApplication.getTimeBank();
        long availableTime = timeBank.getAvailableTime();
        long timeSpent = timeBank.getSpentTime(this.context);

        if (timeSpent >= availableTime && inBadApp) {
            this.context.startActivity(this.launcherIntent);
            return;
        }

        if (inBadApp) {
            timeBank.addSpentTime(this.context, ForegroundAppObserverThread.SLEEP_LENGTH);
        }
//        Log.d(TAG, "processForegroundMessage: spentTime " + Long.toString(timeSpent));

    }

}
