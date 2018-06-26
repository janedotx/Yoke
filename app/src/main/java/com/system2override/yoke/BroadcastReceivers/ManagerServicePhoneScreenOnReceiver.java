package com.system2override.yoke.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.system2override.yoke.ManagerService;

public class ManagerServicePhoneScreenOnReceiver extends BroadcastReceiver {
    private static final String TAG = "ManagerServicePhoneScre";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            Intent i = new Intent(context, ManagerService.class);
            android.util.Log.d(TAG, "about to start service cause phone screen on");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(i);
            } else {
                context.startService(i);
            }
        }
    }
}
