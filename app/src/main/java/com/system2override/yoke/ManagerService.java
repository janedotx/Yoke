package com.system2override.yoke;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ManagerService extends Service {
    private static final String TAG = "ManagerService";
    private List<UsageStats> usageStats;
    private final int MANAGER_SERVICE_ID = 1;

    public ManagerService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.util.Log.d(TAG, "onCreate: about to make this notification");
            //           /*
            String CHANNEL_ID = "StepSensorService";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "StepSensorServiceNotificationChannel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);


            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.placeholder)
                    .setContentTitle("StepSensor Service in the house")
                    .setTicker("StepSensorService ticker")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setWhen(System.currentTimeMillis())
                    .setContentText("here i yam").build();


            startForeground(MANAGER_SERVICE_ID, notification);
//            */
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // update with startForegroundService for Oreo services

            try {
                RulesManagerThread rulesManagerThread = new RulesManagerThread(this);
                rulesManagerThread.start();

            } catch (Exception e) {
                Log.d(TAG, "onStartCommand: stats not gottten");
            }
        Log.d(TAG, "onStartCommand: returning out of startcommand");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: i got called");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(fd, writer, args);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UsageStatsManager getUsageStatsManager() {

        if (android.os.Build.VERSION.SDK_INT == 21) {
            return (UsageStatsManager) this.getSystemService("usagestats");

        } else {
            return (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        }

    }

    class GetManagerThread extends Thread {
        private volatile List<UsageStats> stats;

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
    class Killmonger extends Thread {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void run() {
            while(true) {

            }
        }

    }
}
