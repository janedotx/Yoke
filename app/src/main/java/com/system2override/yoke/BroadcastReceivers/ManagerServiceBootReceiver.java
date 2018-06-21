package com.system2override.yoke.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.system2override.yoke.ManagerService;

public class ManagerServiceBootReceiver extends BroadcastReceiver {
    private static final String TAG = "ManagerServiceBootRecei";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent i = new Intent(context, ManagerService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                android.util.Log.d(TAG, "subscribeToSensor: about to start foreground service " + Long.toString(System.currentTimeMillis()));
                context.startForegroundService(i);
            } else {
                android.util.Log.d(TAG, "subscribeToSensor: about to start service " + Long.toString(System.currentTimeMillis()));
                context.startService(i);
            }
        }
    }
}
