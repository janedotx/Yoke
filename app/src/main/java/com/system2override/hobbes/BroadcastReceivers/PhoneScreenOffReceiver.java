package com.system2override.hobbes.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.system2override.hobbes.ForegroundAppObserverThread;
import com.system2override.hobbes.ManagerService;
import com.system2override.hobbes.MyApplication;

public class PhoneScreenOffReceiver extends BroadcastReceiver{
    private static final String TAG = "MSScreenOffReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: phone went off");
        MyApplication.getBus().post(Integer.toString(ForegroundAppObserverThread.DO_NOT_OBSERVE));
    }
}