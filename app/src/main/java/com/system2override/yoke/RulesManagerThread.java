package com.system2override.yoke;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class RulesManagerThread extends Thread {
    private static final String TAG = "RulesManagerThread";
    private volatile List<UsageStats> stats;
    Context context;
    UsageStatsManager manager;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    List<TodoRule> rules;
    HashMap<String, List<TodoRule>> packageNameToRulesMap;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RulesManagerThread(Context context) {
        this.context = context;
        this.manager = getUsageStatsManager();
        this.sharedPrefs = context.getSharedPreferences(context.getString(R.string.rules_manager_file), Context.MODE_PRIVATE);
        this.editor = sharedPrefs.edit();
        this.packageNameToRulesMap = new HashMap<String, List<TodoRule>>();

        HarnessDatabase db = Room.databaseBuilder(context,
                HarnessDatabase.class, "db").allowMainThreadQueries().build();
        this.rules = db.todoRuleDao().loadAllTodoRules();
        db.close();
        for (int i = 0; i < rules.size(); i++) {
            TodoRule rule = rules.get(i);
            Log.d(TAG, "RulesManagerThread: rule " + rule.toString());
            Log.d(TAG, "RulesManagerThread: rule " + rule.getPackageName());
            if (packageNameToRulesMap.get(rule.getPackageName()) == null) {
                packageNameToRulesMap.put(rule.getPackageName(), new ArrayList<TodoRule>());
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void run()  {
        Log.d(TAG, "run: foo");
        Calendar start = getStart();
        Calendar end = getEnd();
        while(true) {
//            Log.d(TAG, "run: ");
            try {

                /*
                stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start.getTimeInMillis(), System.currentTimeMillis());


                for (int i = 0; i < stats.size(); i++) {
                    UsageStats stat = stats.get(i);
                    Log.d(TAG, "onStartCommand: a usage stat " + stat.getPackageName() + Long.toString(stat.getTotalTimeInForeground()));
                }
                */
                ////
                ///*
                // default value is time.now
                long curTime = System.currentTimeMillis();
                long lastEventCheck = sharedPrefs.getLong("lastEventCheck", curTime);

                // + 1, otherwise we'll double count the last seen event
                UsageEvents events = manager.queryEvents(lastEventCheck + 1, curTime);
                while(events.hasNextEvent()) {
                    UsageEvents.Event event = new UsageEvents.Event();
                    events.getNextEvent(event);
                    long eventTime = event.getTimeStamp();
                    if (eventTime >= lastEventCheck) {
                        lastEventCheck = eventTime;
                    }
                   Log.d(TAG, "run: " + event.getPackageName() + " " + Integer.toString(event.getEventType()));
                    Log.d(TAG, "run: " + Long.toString(event.getTimeStamp()));
                }

                storeEventCheck(editor, lastEventCheck);

                Thread.sleep(2000);

            } catch (InterruptedException e) {
                Log.d(TAG, "run: this shouldn't happen since i have not written any other threads to interrupt this thread yet");
                Log.d(TAG, "run: " + e.getStackTrace());
            }
                /*
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                */

        }
    }

    private void storeEventCheck(SharedPreferences.Editor editor, long lastEventCheck) {
        editor.putLong("lastEventCheck", lastEventCheck);
        editor.apply();
    }

    public List<UsageStats> getStats() {
        return stats;
    }
}
