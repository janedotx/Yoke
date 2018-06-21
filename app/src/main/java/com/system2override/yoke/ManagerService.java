package com.system2override.yoke;

import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.ExponentialBackOff;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.system2override.yoke.TodoAppConstants.GTASKS_ACCCOUNT_NAME;
import static com.system2override.yoke.TodoAppConstants.GTASKS_SCOPES;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class ManagerService extends Service {
    private static final String TAG = "ManagerService";
    private List<UsageStats> usageStats;
    private final int MANAGER_SERVICE_ID = 1;

    public ManagerService() {
        super();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.util.Log.d(TAG, "onCreate: about to make this notification");
            //           /*

            if (Build.VERSION.SDK_INT >= 26) {
                foregroundForOreoAndUp();
            }
//            */
        }
        launchManagerThread();
    }

    @RequiresApi(api = 26)
    private void foregroundForOreoAndUp() {
        String CHANNEL_ID = getString(R.string.manager_service_name);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                getString(R.string.manager_service_notification_channel),
                NotificationManager.IMPORTANCE_DEFAULT);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.placeholder)
                .setContentTitle("Manager Service in the house")
                .setTicker("Yoke is watching you")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setWhen(System.currentTimeMillis())
                .setContentText("here i yam").build();

        startForeground(MANAGER_SERVICE_ID, notification);

    }

    //todo add threadgroup and other thread checking code...
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void launchManagerThread() {
        try {
            Log.d(TAG, "launchManagerThread: trying to launch this fucking threads");

            SharedPreferences settings =
                    this.getApplicationContext().getSharedPreferences(TodoAppConstants.ACCOUNTS_FILE, Context.MODE_PRIVATE);
            String accountName = settings.getString(TodoAppConstants.GTASKS_ACCCOUNT_NAME, null);

            GoogleAccountCredential mCredential = GoogleAccountCredential.usingOAuth2(
                    getApplicationContext(), Arrays.asList(GTASKS_SCOPES))
                    .setBackOff(new ExponentialBackOff());

            // should ask for authentication somehow...
            mCredential.setSelectedAccountName(accountName);

            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            com.google.api.services.tasks.Tasks mService = new com.google.api.services.tasks.Tasks.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("Google Tasks API Android Quickstart")
                    .build();

            RulesManagerThread rulesManagerThread = new RulesManagerThread(this);
            rulesManagerThread.setGoogleService(mService);
            Thread.UncaughtExceptionHandler handler = rulesManagerThread.getUncaughtExceptionHandler();
            if (!(handler instanceof DefaultUncaughtExceptionHandler)) {
                rulesManagerThread.setUncaughtExceptionHandler(
                        new DefaultUncaughtExceptionHandler(handler, this));
            }
            rulesManagerThread.start();

        } catch (Exception e) {
            Log.d(TAG, "onStartCommand: stats not gottten");
            Log.d(TAG, "onStartCommand: " + Log.getStackTraceString(e));
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: start");
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

    @Override
    public void onTaskRemoved(Intent rootIntent){
        ///*
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());


        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);
        //*/
        super.onTaskRemoved(rootIntent);
    }

    class Killmonger extends Thread {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void run() {
            while(true) {

            }
        }

    }
}
