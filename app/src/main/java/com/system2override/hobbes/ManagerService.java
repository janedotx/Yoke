package com.system2override.hobbes;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import java.util.Arrays;
import java.util.Calendar;

import static com.system2override.hobbes.TodoAppConstants.GTASKS_SCOPES;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.system2override.hobbes.BroadcastReceivers.DailyResetReceiver;
import com.system2override.hobbes.BroadcastReceivers.PhoneScreenOffReceiver;
import com.system2override.hobbes.BroadcastReceivers.PhoneScreenOnReceiver;
import com.system2override.hobbes.Models.TimeBank;
import com.system2override.hobbes.Utilities.RandomUtilities;

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
        long nextMidnight = RandomUtilities.getNextMidnight();

        Log.d(TAG, "setDailyResetAlarm: calendar.getTimeInMillis " + Long.toString(nextMidnight));
        Log.d(TAG, "setDailyResetAlarm: action " + TimeBank.RESET_ACTION);

        Intent resetIntent = new Intent(this, DailyResetReceiver.class);
        resetIntent.setAction(MyApplication.MIDNIGHT_RESET_ACTION);

        PendingIntent pendingResetIntent = PendingIntent.getBroadcast(this,
                                                                     0,
                                                                      resetIntent,
                                                                    PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC, nextMidnight, 1000 * 60 * 60 * 24, pendingResetIntent);
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
