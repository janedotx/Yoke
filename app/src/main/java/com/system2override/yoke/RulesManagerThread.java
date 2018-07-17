package com.system2override.yoke;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.api.client.util.DateTime;

import com.system2override.yoke.integrations.GoogleSnapshot;
import com.system2override.yoke.models.PerAppTodoRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

// i am going to keep not worrying about this being restarted halfway through
// so going on the assumption that this is only ever called in the case of the app successfully starting either onboot
// or when triggered by the user...
// not interested in events that last only half a second

public class RulesManagerThread extends Thread {
    private com.google.api.services.tasks.Tasks mService;
    private static final String TAG = "RulesManagerThread";
    private volatile List<UsageStats> stats;
    private final long SLEEP_LENGTH = 4000;
    private long MAX_LOOKBACK = 14400 * 1000;
    Context context;
    UsageStatsManager manager;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    List<PerAppTodoRule> rules;
    HashMap<String, List<PerAppTodoRule>> packageNameToRulesMap;
    PowerManager powerManager;
    HarnessDatabase db;
    Display display;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RulesManagerThread(Context context) {
        this.context = context;
        this.manager = getUsageStatsManager();
        this.sharedPrefs = context.getSharedPreferences(context.getString(R.string.rules_manager_file), Context.MODE_PRIVATE);
        this.editor = sharedPrefs.edit();
        this.packageNameToRulesMap = new HashMap<String, List<PerAppTodoRule>>();
        this.powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        this.db = Room.databaseBuilder(context,
                HarnessDatabase.class, "db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        this.rules = this.db.perAppTodoRuleDao().loadAllPerAppTodoRules();

        for (int i = 0; i < rules.size(); i++) {
            PerAppTodoRule rule = rules.get(i);
            Log.d(TAG, "RulesManagerThread: rule " + rule.toString());
            Log.d(TAG, "RulesManagerThread: rule " + rule.getPackageName());
            if (packageNameToRulesMap.get(rule.getPackageName()) == null) {
                packageNameToRulesMap.put(rule.getPackageName(), new ArrayList<PerAppTodoRule>());
            }
            packageNameToRulesMap.get(rule.getPackageName()).add(rule);
        }
        Log.d(TAG, "RulesManagerThread: hashmap " + packageNameToRulesMap.toString());

    }

    private Calendar getStart() {

        Calendar date = new GregorianCalendar();
// reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        Log.d(TAG, "getStart: date start " + date.toString());
        return date;
    }

    private Calendar getEnd() {
        Calendar date = new GregorianCalendar();
// reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        date.add(Calendar.DAY_OF_MONTH, 1);
        return date;
    }

    private Calendar getHourPlus() {
        Calendar date = new GregorianCalendar();
// reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 1);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UsageStatsManager getUsageStatsManager() {

        if (android.os.Build.VERSION.SDK_INT == 21) {
            return (UsageStatsManager) context.getSystemService("usagestats");

        } else {
            return (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        }

    }

    // this works...
    //
    private String getForegroundTask() {
        String currentApp = "NULL";
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_BEST,  time - 5000, time);
            if (appList != null && appList.size() > 0) {
//                Log.d(TAG, "getForegroundTask: applist not null");
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }

        Log.d("adapter", "Current App in foreground is: " + currentApp);
        return currentApp;
    }

    private void addTime(String packageName, long time) {
        long val = this.sharedPrefs.getLong(packageName, 0);
        this.editor.putLong(packageName, val + time);
        this.editor.apply();
        Log.d(TAG, "addTime: " + packageName + " " + Long.toString(this.sharedPrefs.getLong(packageName, 0)));
    }

    private void resetTime(String packageName) {
        this.editor.putLong(packageName, 0);
        this.editor.apply();
    }

    private void killApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.startActivity(startMain);
    }

    public void setGoogleService(com.google.api.services.tasks.Tasks service) {
        this.mService = service;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void run()  {
        Log.d(TAG, "run: foo");
        Calendar start = getStart();
        Calendar end = getEnd();
        while(true) {
//            if (display.getState() != Display.STATE_ON) {
 //               Log.d(TAG, "run: display state was not on");
  //              continue;
   //         }

            String packageName = getForegroundTask();
           PerAppTodoRule rule = db.perAppTodoRuleDao().getStrictestRuleForPackageName(packageName);
/*
            for (PerAppTodoRule rule: rules) {
                Log.d(TAG, "run: rule " + rule.toString());
            }
            */
            // don't bother time tracking for apps that we have no rules for
            // save the expense of hitting SharedPreferences

//            List<String> str = new ArrayList<>();
//            str.get(1);
            if (rule != null) {
                if (rule.getTime() <= this.sharedPrefs.getLong(packageName, 0)) {
                    // TODO if integration with relevant app shows a failure, boot them!
                    // otherwise, reset counter
                    // TODO what's a good way to go from the rule to the snapshot class...
                    DateTime date = new DateTime(0);
                    try {
                        GoogleSnapshot.getTasks(mService, date.toStringRfc3339());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    killApp();
                } else {
                    addTime(packageName, SLEEP_LENGTH);
                }
            }

            /*
                get foreground task
                if rule has been broken for this task?
                  see if todo list item has been crossed off
                    if it has not, kill foreground task
                    if it has, reset timer

                else
                    add timer for foreground task

                put counters into SharedPrefs
             */


            try {

                Thread.sleep(SLEEP_LENGTH);

            } catch (InterruptedException e) {
                Log.d(TAG, "run: this shouldn't happen since i have not written any other threads to interrupt this thread yet");
                Log.d(TAG, "run: " + e.getStackTrace());
            }
                /*
                */

        }
    }

}
