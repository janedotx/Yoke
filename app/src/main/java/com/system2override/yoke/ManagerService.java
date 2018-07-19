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
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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
import java.util.Arrays;
import java.util.Calendar;

import static com.system2override.yoke.TodoAppConstants.GTASKS_SCOPES;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.squareup.otto.Subscribe;
import com.system2override.yoke.BroadcastReceivers.DailyResetReceiver;
import com.system2override.yoke.BroadcastReceivers.PhoneScreenOffReceiver;
import com.system2override.yoke.BroadcastReceivers.PhoneScreenOnReceiver;

public class ManagerService extends Service {
    private static final String TAG = "ManagerService";
    private final int MANAGER_SERVICE_ID = 1;
    private long APP_TICK_INTERVAL = 1000;

    private ForegroundAppObserverThread appObserverThread;
    private PhoneScreenOffReceiver screenOffReceiver;
    private PhoneScreenOnReceiver screenOnReceiver;
    private DailyResetReceiver dailyResetReceiver;

    public ManagerService() {
        super();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
        MyApplication.getBus().register(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.util.Log.d(TAG, "onCreate: about to make this notification");
            if (Build.VERSION.SDK_INT >= 26) {
                foregroundForOreoAndUp();
            }
//            */
        }

        registerReceivers();
        setDailyResetAlarm();
        appObserverThread = new ForegroundAppObserverThread(this);
        appObserverThread.start();
        appObserverThread.getHandler().sendEmptyMessage(ForegroundAppObserverThread.OBSERVE);

    }

    private void setDailyResetAlarm() {
        /*
        IntentFilter resetFilter = new IntentFilter(TimeBank.RESET_ACTION);
        dailyResetReceiver = new DailyResetReceiver();
        this.registerReceiver(dailyResetReceiver, resetFilter);
        */

        Calendar calendar = Calendar.getInstance();
        long time = System.currentTimeMillis();
        calendar.setTimeInMillis(time + 65000);
        Log.d(TAG, "setDailyResetAlarm: curtime " + Long.toString(time));
/*        calendar.setTimeInMillis(time);
        // ensure this fires for the next upcoming midnight
        // will this _always_ work?
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        */

        Log.d(TAG, "setDailyResetAlarm: calendar.getTimeInMillis " + Long.toString(calendar.getTimeInMillis()));
        Log.d(TAG, "setDailyResetAlarm: action " + TimeBank.RESET_ACTION);

        Intent resetIntent = new Intent(this, DailyResetReceiver.class);
        resetIntent.setAction(TimeBank.RESET_ACTION);

        PendingIntent pendingResetIntent = PendingIntent.getBroadcast(this,
                                                                     0,
                                                                      resetIntent,
                                                                    PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000 * 60 * 1, pendingResetIntent);
        am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingResetIntent);
    }

    private void registerReceivers() {
        IntentFilter filterOn = new IntentFilter("android.intent.action.SCREEN_ON");
        screenOnReceiver = new PhoneScreenOnReceiver();
        IntentFilter filterOff = new IntentFilter("android.intent.action.SCREEN_OFF");
        screenOffReceiver = new PhoneScreenOffReceiver();

        this.registerReceiver(screenOnReceiver, filterOn);
        this.registerReceiver(screenOffReceiver, filterOff);
        Log.d(TAG, "registerReceivers: these fucking receivers were registered");
    }

    @com.squareup.otto.Subscribe
    public void processPhoneEvent(String phoneEvent) {
        boolean shouldObserve = true;
        Log.d(TAG, "processEvent: " + phoneEvent);
        Log.d(TAG, "processPhoneEvent: " + Boolean.toString(shouldObserve));
        int event = Integer.parseInt(phoneEvent);

        appObserverThread.getHandler().sendEmptyMessage(event);
   //     */
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
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: start");
        appObserverThread.getHandler().getLooper().quit();
        this.unregisterReceiver(screenOffReceiver);
        this.unregisterReceiver(screenOnReceiver);
//        this.unregisterReceiver(dailyResetReceiver);
        super.onDestroy();
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

}
