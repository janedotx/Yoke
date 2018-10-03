package com.system2override.hobbes.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.system2override.hobbes.ManagerService;

public class ManagerServiceBootReceiver extends BroadcastReceiver {
    private static final String TAG = "ManagerServiceBootRecei";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent i = new Intent(context, ManagerService.class);
            Log.d(TAG, "onReceive: about to start service on boot");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(i);
            } else {
                context.startService(i);
            }
        }
    }
}
