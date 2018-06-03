package com.system2override.yoke;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageEvents.Event;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.android.gms.common.wrappers.InstantApps;
import com.squareup.otto.Bus;

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
    private static final String TAG = "RulesManagerThread";
    private volatile List<UsageStats> stats;
    private final long SLEEP_LENGTH = 4000;
    Context context;
    UsageStatsManager manager;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    List<TodoRule> rules;
    HashMap<String, List<TodoRule>> packageNameToRulesMap;
    PowerManager powerManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RulesManagerThread(Context context) {
        this.context = context;
        this.manager = getUsageStatsManager();
        this.sharedPrefs = context.getSharedPreferences(context.getString(R.string.rules_manager_file), Context.MODE_PRIVATE);
        this.editor = sharedPrefs.edit();
        this.packageNameToRulesMap = new HashMap<String, List<TodoRule>>();
        this.powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

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

    // this works...
    //
    private String printForegroundTask() {
        String currentApp = "NULL";
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
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
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }

        Log.e("adapter", "Current App in foreground is: " + currentApp);
        return currentApp;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void run()  {
        Log.d(TAG, "run: foo");
        Calendar start = getStart();
        Calendar end = getEnd();
        while(true) {

            printForegroundTask();
//            Log.d(TAG, "run: ");
            /*
            this just kills Yoke faster and OxygenOS won't even restart it
            if (!powerManager.isInteractive()) {
                continue;
            }
            */
//            /*
            try {

//                /*
                long curTime = System.currentTimeMillis();
                Event lastEvent = InProcessAppDataCache.getLastEvent();
                if (lastEvent != null) {
//                    Log.d(TAG, "run: inprocessappdatacache last event " + lastEvent.toString());
                } else {
 //                   Log.d(TAG, "run: the most recently recorded event is null");
                }
                long lastEventCheck;
                if (lastEvent == null) {
                    Log.d(TAG, "run: lastevent is null");
                    lastEventCheck = curTime - 5000;
                } else {
                    lastEventCheck = lastEvent.getTimeStamp();
                }
                // + 1, otherwise we'll double count the last seen event
//        /*
                UsageEvents events = manager.queryEvents(lastEventCheck + 1, System.currentTimeMillis());
//                /*
                List<UsageEvents.Event> eventsList = processAndFilterEventsList(events);
                // if no new events have happened, then whatever app last came into the foreground
                // is still in the foreground, and therefore
                // is racking up time
                if (eventsList.size() == 0 && InProcessAppDataCache.hasLastEvent() && InProcessAppDataCache.getLastEvent().getEventType() == Event.MOVE_TO_FOREGROUND) {
                    InProcessAppDataCache.addTime(InProcessAppDataCache.getLastEventPackageName(), SLEEP_LENGTH);

                } else if (eventsList.size() > 0){
                    Event newLast = eventsList.get(eventsList.size() -1);
                    Log.d(TAG, "run: new lastEvent " + newLast.getPackageName() + " " + Integer.toString(newLast.getEventType()));
                    InProcessAppDataCache.setLastEvent(newLast);

                }
//                */


                Thread.sleep(SLEEP_LENGTH);

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

    private void storeEventCheck(SharedPreferences.Editor editor, long lastEventTime) {
        editor.putLong("lastEventTime", lastEventTime);
        editor.apply();
    }

    public List<UsageStats> getStats() {
        return stats;
    }

    private List<UsageEvents.Event> processAndFilterEventsList(UsageEvents events) {
        List<UsageEvents.Event> eventsList = new ArrayList<UsageEvents.Event>();
        Log.d(TAG, "processAndFilterEventsList: *****");
        while(events.hasNextEvent()) {
            // make sure we always make a fresh new Event to copy into
            UsageEvents.Event curEvent = new UsageEvents.Event();
            events.getNextEvent(curEvent);
 //           Log.d(TAG, "processAndFilterEventsList: in the while loop");
            int type = curEvent.getEventType();
            if ((type == UsageEvents.Event.MOVE_TO_FOREGROUND || type == UsageEvents.Event.MOVE_TO_BACKGROUND)) {
                eventsList.add(curEvent);
            } else {
                continue;
            }
        }
        if (eventsList.size() > 0) {
            for (int i = 0; i < eventsList.size(); i++) {
                Event e = eventsList.get(i);
                Log.d(TAG, "processAndFilterEventsList: " + Integer.toString(e.getEventType()) + " " + e.getPackageName());
            }
            List<Event> filteredlist = filterOutShortLivedIntervals(eventsList);
            for (int i = 0; i < filteredlist.size(); i++) {
                Event e = filteredlist.get(i);
            }
            return filterOutShortLivedIntervals(eventsList);
        }
        return eventsList;
    }

    private List<Event> filterOutShortLivedIntervals(List<Event> eventList) {
        int size = eventList.size();
        if (eventList.size() == 0 || eventList.size() == 1) {
            return eventList;
        }

        List<Event> filteredList = new ArrayList<Event>();
        if (eventList.get(0).getEventType() == Event.MOVE_TO_BACKGROUND) {
            filteredList.add(eventList.get(0));
        }
        if ((eventList.get(size - 1).getEventType() == Event.MOVE_TO_FOREGROUND)) {
            filteredList.add(eventList.get(size - 1));
        }

        return filteredList;
    }
}
