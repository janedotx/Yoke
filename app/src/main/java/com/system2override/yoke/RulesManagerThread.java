package com.system2override.yoke;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class RulesManagerThread extends Thread {
    private static final String TAG = "RulesManagerThread";
    private volatile List<UsageStats> stats;
    Context context;

    public RulesManagerThread(Context context) {
        this.context = context;
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
        UsageStatsManager manager = getUsageStatsManager();
        Log.d(TAG, "run: foo");
        Calendar start = getStart();
        Calendar end = getEnd();
        while(true) {
            try {
                ///*
                stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start.getTimeInMillis(), end.getTimeInMillis());

                Thread.sleep(1000);

                    /*
                    for (int i = 0; i < stats.size(); i++) {
                        UsageStats stat = stats.get(i);
                        Log.d(TAG, "onStartCommand: a usage stat " + stat.getPackageName());
                        double timeInForeground = ((double) stat.getTotalTimeInForeground()) / 60000.0;
                        Log.d(TAG, "onStartCommand: a usage stat " + Double.toString(timeInForeground));
                    }
                    //*/
                Thread.sleep(200);
                long curTime = System.currentTimeMillis();
                UsageEvents events = manager.queryEvents(curTime - 5000, curTime);
                while(events.hasNextEvent()) {
                    UsageEvents.Event event = new UsageEvents.Event();
                    events.getNextEvent(event);
                    Log.d(TAG, "run: running packagename "+ event.getPackageName());
                    Log.d(TAG, "run: running event type "+ Integer.toString(event.getEventType()));

//                        /*
                    if (event.getPackageName() == "com.android.contacts") {
                        Log.d(TAG, "run: packagename on point");
                    }

                    if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        Log.d(TAG, "run: " + event.getPackageName() + " moved to foreground");
                    }
                    if ((event.getPackageName().equals("com.android.contacts")) && (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND)) {
                        Log.d(TAG, "run: contacts moved to foreground");

                            /*
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                            */
                    }
//                        */
                }
            } catch (InterruptedException e) { }
                /*
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                */

        }
    }

    public List<UsageStats> getStats() {
        return stats;
    }
}
