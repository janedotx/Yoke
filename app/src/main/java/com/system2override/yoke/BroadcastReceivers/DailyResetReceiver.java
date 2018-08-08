package com.system2override.yoke.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.system2override.yoke.Models.TimeBank;
import com.system2override.yoke.MyApplication;

public class DailyResetReceiver extends BroadcastReceiver{
    private static final String TAG = "DailyResetReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        TimeBank timeBank = MyApplication.getTimeBank();
        if (timeBank.RESET_ACTION.equals(intent.getAction())) {
            Log.d(TAG, "onReceive: doing a reset now");
            timeBank.resetTime(context);
            Toast.makeText(context, "Your toast message",
                    Toast.LENGTH_LONG).show();
        }
    }
}
