package com.system2override.yoke.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.system2override.yoke.ForegroundAppObserverThread;
import com.system2override.yoke.ManagerService;
import com.system2override.yoke.MyApplication;

public class PhoneScreenOffReceiver extends BroadcastReceiver{
    private static final String TAG = "MSScreenOffReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            /*
            Intent i = new Intent(context, ManagerService.class);
            android.util.Log.d(TAG, "about to stop service cause phone screen off");
            context.stopService(i);
            */
        }
        Log.d(TAG, "onReceive: phone went off");
        MyApplication.getBus().post(Integer.toString(ForegroundAppObserverThread.DO_NOT_OBSERVE));
    }
}
