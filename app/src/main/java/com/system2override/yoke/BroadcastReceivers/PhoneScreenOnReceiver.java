package com.system2override.yoke.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.system2override.yoke.ForegroundAppObserverThread;
import com.system2override.yoke.ManagerService;
import com.system2override.yoke.MyApplication;

public class PhoneScreenOnReceiver extends BroadcastReceiver {
    private static final String TAG = "ManagerServicePhoneScre";

    public PhoneScreenOnReceiver() {
        Log.d(TAG, "PhoneScreenOnReceiver: i got made");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: phone went on");
        MyApplication.getBus().post(Integer.toString(ForegroundAppObserverThread.OBSERVE));
    }
}
