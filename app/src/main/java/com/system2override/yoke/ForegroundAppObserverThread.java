package com.system2override.yoke;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.squareup.otto.Subscribe;
import com.system2override.yoke.OttoMessages.CurrentAppMessage;
import com.system2override.yoke.OttoMessages.ForegroundMessage;

import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ForegroundAppObserverThread extends Thread {
    private static final String TAG = "ForegroundAppObserverTh";
    public static final long SLEEP_LENGTH = 2000;
    // yoke last at least an hour
//    private final long LOOK_BACK_INTERVAL = 60 * 60 * 1000;
    // i've seen yoke last for at least a couple hours
//    private final long LOOK_BACK_INTERVAL = 60 * 1000;
    // yoke has gone strong for 2.5+ hours
    private final long LOOK_BACK_INTERVAL = 12 * 60 * 60 * 1000;

    public final String NULL = "NULL";
    Context context;
    UsageStatsManager usageStatsManager;
    ActivityManager activityManager;
    Handler handler;
    String lastApp;
    AppChangeEventHandler appChangeEventHandler;
    RulesManager rulesEnforcer;

    public static final int OBSERVE = 1;
    public static final int DO_NOT_OBSERVE = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ForegroundAppObserverThread(Context context) {
        MyApplication.getBus().register(this);

        this.context = context;
        this.rulesEnforcer = new RulesManager(this.context);
        this.lastApp = NULL;
        this.usageStatsManager = getUsageStatsManager();
        this.activityManager = (ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE);
        this.handler = new Handler() {
            public void handleMessage(Message message) {
//                Log.d(TAG, "handleMessage: " + Integer.toString(message.what));
                switch(message.what) {
                    case OBSERVE:
//                        Log.d(TAG, "handleMessage: enqueuing next message");
                        MyApplication.getBus().post(new ForegroundMessage(getForegroundApp()));
                        this.sendEmptyMessageDelayed(OBSERVE, SLEEP_LENGTH);
                        break;
                    case DO_NOT_OBSERVE:
                        this.removeMessages(OBSERVE);
                        break;
                }
            }
        };

//        Log.d(TAG, "ForegroundAppObserverThread: thread made");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void run() {
        Looper.prepare();
        Looper.loop();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UsageStatsManager getUsageStatsManager() {

        if (android.os.Build.VERSION.SDK_INT == 21) {
            return (UsageStatsManager) context.getSystemService("usagestats");

        } else {
            return (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        }

    }

    private String getForegroundApp() {
        String currentApp = NULL;
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            long time = System.currentTimeMillis();
            List<UsageStats> appList = this.usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,  time - LOOK_BACK_INTERVAL, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            this.activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = this.activityManager.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }
//        Log.d(TAG, "getForegroundApp: " + currentApp);

        return currentApp;
    }

    public Handler getHandler() {
        return handler;
    }

    // publish app changes, because if the user stays in an app for a long enough time, eventually the
    // current foreground app will be NULL, since we look at the most recently used package in a
    // relatively short window of time
    @com.squareup.otto.Subscribe
    public void publishAppChanges(ForegroundMessage message) {
        if (message.getApp() == NULL) {
            if (lastApp != NULL) {
//                Log.d(TAG, "publishAppChanges: currentApp is " + lastApp);
                MyApplication.getBus().post(new CurrentAppMessage(lastApp));
 //               BannedApps.addTime(this.context, SLEEP_LENGTH);
            }
        } else {
            lastApp = message.getApp();
//            Log.d(TAG, "publishAppChanges: currentApp is " + lastApp);
//            BannedApps.addTime(this.context, SLEEP_LENGTH);
            MyApplication.getBus().post(new CurrentAppMessage(lastApp));
        }
//        Log.d(TAG, "publishAppChanges: amount of time is " + Long.toString(BannedApps.getTime(this.context)));
    }
}
